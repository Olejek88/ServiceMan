package ru.shtrm.serviceman.mvp.object;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.Contragent;
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.Photo;
import ru.shtrm.serviceman.data.ZhObject;
import ru.shtrm.serviceman.data.source.local.ContragentLocalDataSource;
import ru.shtrm.serviceman.data.source.local.ObjectLocalDataSource;
import ru.shtrm.serviceman.interfaces.OnRecyclerViewItemClickListener;
import ru.shtrm.serviceman.mvp.MainActivity;
import ru.shtrm.serviceman.mvp.equipment.EquipmentActivity;
import ru.shtrm.serviceman.mvp.equipment.EquipmentAdapter;
import ru.shtrm.serviceman.rfid.RfidDialog;
import ru.shtrm.serviceman.rfid.RfidDriverBase;
import ru.shtrm.serviceman.rfid.Tag;
import ru.shtrm.serviceman.util.DensityUtil;
import ru.shtrm.serviceman.util.MainUtil;

import static ru.shtrm.serviceman.mvp.object.ObjectActivity.OBJECT_UUID;
import static ru.shtrm.serviceman.rfid.RfidDialog.TAG;

public class ObjectFragment extends Fragment implements ObjectContract.View {
    private Activity mainActivityConnector = null;

    private ObjectContract.Presenter presenter;
    private ZhObject object;
    private Contragent contragent;
    private Photo photo;
    private File photoFile;
    private String photoUuid;

    private EquipmentAdapter equipmentAdapter;
    private RecyclerView recyclerView;
    private CircleImageView circleImageView;
    private TextView textViewStatus;
    private RfidDialog rfidDialog;

    private SharedPreferences sp;

    public ObjectFragment() {
    }

    public static ObjectFragment newInstance() {
        return new ObjectFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flat, container, false);
        sp = PreferenceManager.getDefaultSharedPreferences(mainActivityConnector);

        FloatingActionButton photoButton = view.findViewById(R.id.add_photo);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                String photoUuid = java.util.UUID.randomUUID().toString().toUpperCase();
                Context context = getContext();
                Activity activity = getActivity();
                if (context == null || activity == null) {
                    return;
                }

                File photoFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), photoUuid + ".jpg");
                if (!photoFile.getParentFile().exists()) {
                    if (!photoFile.getParentFile().mkdirs()) {
                        Log.e(TAG, "can`t create \"" + photoFile.getAbsolutePath() + "\" path.");
                        return;
                    }
                }

                // запоминаем данные необходимые для создания записи Photo в onActivityResult
                ObjectActivity.photoFile = photoFile.getAbsolutePath();
                ObjectActivity.objectUuid = object.getUuid();
                ObjectActivity.photoUuid = photoUuid;

                try {
                    Uri photoURI = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    activity.startActivityForResult(intent, MainActivity.PHOTO_RESULT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle b = getArguments();
        if (b != null) {
            String flatUuid = b.getString(OBJECT_UUID);
            if (flatUuid != null)
                object = ObjectLocalDataSource.getInstance().getObject(flatUuid);
            if (object != null) {
                contragent = ContragentLocalDataSource.getInstance().getContragentByObject(flatUuid);
                initViews(view);
                presenter.loadEquipmentsByObject(object);
                setHasOptionsMenu(true);
                return view;
            }
        }
        if (getFragmentManager() != null)
            getFragmentManager().popBackStack();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.subscribe();
            presenter.loadEquipmentsByObject(object);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (presenter != null)
            presenter.unsubscribe();
    }

    @Override
    public void initViews(View view) {
        TextView textViewAbonent = view.findViewById(R.id.textViewFlatAbonent);
        TextView textViewFlat = view.findViewById(R.id.textViewFlat);
        Toolbar mToolbar = view.findViewById(R.id.toolbar);

        textViewStatus = view.findViewById(R.id.textViewStatus);
        circleImageView = view.findViewById(R.id.imageViewFlat);

        if (mToolbar != null) {
            mToolbar.setTitle(object.getFullTitle());
            if (object.getHouse().getHouseType() != null) {
                if (DensityUtil.getScreenHeight(mainActivityConnector) > 1280) {
                    mToolbar.setSubtitle(object.getObjectType().getTitle());
                } else {
                    mToolbar.setTitle(object.getFullTitle().concat(" - ").
                            concat(object.getObjectType().getTitle()));
                }
            }
        }

        if (contragent != null) {
            textViewAbonent.setText(contragent.getTitle());
        }

        // TODO заменить когда будут реальные значения
        textViewFlat.setText(object.getTitle().substring(0, 1));
        textViewStatus.setText(object.getObjectStatus().getTitle());
        if (photo != null) {
            String sDate = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.US).
                    format(photo.getCreatedAt());
            circleImageView.setImageBitmap(MainUtil.getBitmapByPath(
                    MainUtil.getPicturesDirectory(mainActivityConnector),
                    photo.getUuid().concat(".jpg")));
        } else {
            circleImageView.setImageResource(R.drawable.flat);
        }

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void setPresenter(@NonNull ObjectContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivityConnector = getActivity();
        // TODO решить что делать если контекст не приехал
        if (mainActivityConnector == null)
            onDestroyView();
    }

    /**
     * Show flats with recycler view.
     *
     * @param list The data.
     */
    @Override
    public void showEquipments(@NonNull final List<Equipment> list) {
        if (equipmentAdapter == null) {
            equipmentAdapter = new EquipmentAdapter(mainActivityConnector, list);
            recyclerView.setAdapter(equipmentAdapter);
        } else {
            equipmentAdapter.updateData(list);
            recyclerView.setAdapter(equipmentAdapter);
        }
        equipmentAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void OnItemClick(View v, int position) {
                Equipment equipment = list.get(position);
                String uuid = equipment.getUuid();
                final String expectedTagId = equipment.getTag();
                boolean ask_tags = sp.getBoolean("without_tags_mode", true);
                if (!ask_tags && expectedTagId != null && !expectedTagId.equals("")) {
                    runRfidDialog(expectedTagId, uuid);
                } else {
                    Intent intent = new Intent(getContext(), EquipmentActivity.class);
                    intent.putExtra("EQUIPMENT_UUID", String.valueOf(uuid));
                    startActivity(intent);
                }
            }
        });
        //showEmptyView(list.isEmpty());
    }

    private void runRfidDialog(String expectedTagId, final String uuid) {
        Tag tag = new Tag();
        tag.loadData(expectedTagId);
        final String expectedTag = tag.getTagId();
        final Activity activity = getActivity();

        if (activity == null) {
            return;
        }

        Log.d(TAG, "Ожидаемая метка: " + expectedTagId);
        Handler handler = new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(android.os.Message message) {
                if (message.what == RfidDriverBase.RESULT_RFID_SUCCESS) {
                    String[] tagIds = (String[]) message.obj;
                    if (tagIds == null) {
                        Toast.makeText(getContext(), "Не верное оборудование!", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    String tagId = tagIds[0].substring(4);
                    Log.d(TAG, "Ид метки получили: " + tagId);
                    if (!expectedTag.equals(tagId)) {
                        Toast.makeText(getContext(), "Не верное оборудование!", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(getContext(), EquipmentActivity.class);
                        intent.putExtra("EQUIPMENT_UUID", String.valueOf(uuid));
                        startActivity(intent);
                    }
                } else {
                    Log.d(TAG, "Ошибка чтения метки!");
                    Toast.makeText(getContext(), "Ошибка чтения метки.", Toast.LENGTH_SHORT).show();
                }
                // закрываем диалог
                rfidDialog.dismiss();
                return false;
            }
        });
        rfidDialog = new RfidDialog();
        rfidDialog.setHandler(handler);
        rfidDialog.readMultiTagId(tag.getTagDriver(getContext()), tag.getTagId());
        rfidDialog.show(activity.getFragmentManager(), TAG);
    }
}
