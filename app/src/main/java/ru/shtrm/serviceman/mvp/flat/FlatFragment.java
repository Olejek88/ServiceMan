package ru.shtrm.serviceman.mvp.flat;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.Flat;
import ru.shtrm.serviceman.data.FlatStatus;
import ru.shtrm.serviceman.data.PhotoFlat;
import ru.shtrm.serviceman.data.Resident;
import ru.shtrm.serviceman.data.source.local.FlatLocalDataSource;
import ru.shtrm.serviceman.data.source.local.PhotoFlatLocalDataSource;
import ru.shtrm.serviceman.data.source.local.ResidentLocalDataSource;
import ru.shtrm.serviceman.interfaces.OnRecyclerViewItemClickListener;
import ru.shtrm.serviceman.mvp.abonents.WorkFragment;
import ru.shtrm.serviceman.mvp.equipment.AddEquipmentActivity;
import ru.shtrm.serviceman.mvp.equipment.EquipmentActivity;
import ru.shtrm.serviceman.mvp.equipment.EquipmentAdapter;
import ru.shtrm.serviceman.util.MainUtil;

import static ru.shtrm.serviceman.mvp.flat.FlatActivity.FLAT_UUID;
import static ru.shtrm.serviceman.mvp.flat.FlatActivity.HOUSE_UUID;

public class FlatFragment extends Fragment implements FlatContract.View {
    private Activity mainActivityConnector = null;
    private final static int ACTIVITY_PHOTO = 100;

    private FlatContract.Presenter presenter;
    private Flat flat;
    private Resident resident;
    private PhotoFlat photoFlat;

    private EquipmentAdapter equipmentAdapter;
    private RecyclerView recyclerView;
    private CircleImageView circleImageView;
    private TextView textViewPhotoDate;

    private FloatingActionButton new_flat;
    private FloatingActionButton make_photo;

    public FlatFragment() {}

    public static FlatFragment newInstance() {
        return new FlatFragment();
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
        Bundle b = getArguments();
        if (b != null) {
            String flatUuid = b.getString(FLAT_UUID);
            if (flatUuid != null)
                flat = FlatLocalDataSource.getInstance().getFlat(flatUuid);
            if (flat != null) {
                resident = ResidentLocalDataSource.getInstance().getResidentByFlat(flat.getUuid());
                photoFlat = PhotoFlatLocalDataSource.getInstance().getLastPhotoByFlat(flat);
                initViews(view);
                presenter.loadEquipmentsByFlat(flat);
                setHasOptionsMenu(true);
                return view;
            }
        }
        if (getFragmentManager()!=null)
            getFragmentManager().popBackStack();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter!=null) {
            presenter.subscribe();
            presenter.loadEquipmentsByFlat(flat);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (presenter!=null)
            presenter.unsubscribe();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mainActivityConnector.onBackPressed();
        }
        return true;
    }

    @Override
    public void initViews(View view) {
        TextView textViewInn = view.findViewById(R.id.textViewFlatInn);
        TextView textViewAbonent = view.findViewById(R.id.textViewFlatAbonent);
        //TextView textViewStatus = view.findViewById(R.id.textViewFlatStatus);
        TextView textViewTitle = view.findViewById(R.id.textViewFlatTitle);
        TextView textViewFlat = view.findViewById(R.id.textViewFlat);
        //GridView gridView = view.findViewById(R.id.gridview);
        Toolbar mToolbar = view.findViewById(R.id.toolbar);
        Spinner statusSpinner = view.findViewById(R.id.spinnerFlatStatus);

        textViewPhotoDate = view.findViewById(R.id.textViewPhotoDate);
        circleImageView = view.findViewById(R.id.imageViewFlat);
        make_photo = view.findViewById(R.id.add_photo);
        new_flat = view.findViewById(R.id.add_equipment);

        if (mToolbar !=null) {
            mToolbar.setTitle(flat.getFullTitle());
            if (flat.getHouse().getHouseType()!=null)
                mToolbar.setSubtitle(flat.getHouse().getHouseType().getTitle());
        }

        if (resident!=null) {
            textViewInn.setText(resident.getInn());
            textViewAbonent.setText(resident.getOwner());
        }
        //if (flat.getFlatStatus()!=null)
          //  textViewStatus.setText(flat.getFlatStatus().getTitle());

        if (flat.getFlatType()!=null)
            textViewTitle.setText(flat.getFlatType().getTitle());
        else
            textViewTitle.setText(flat.getFullTitle());

        textViewFlat.setText(flat.getTitle().substring(0,1));
        if (photoFlat!=null) {
            String sDate = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.US).
                    format(photoFlat.getCreatedAt());
            textViewPhotoDate.setText(sDate);
            // TODO заменить на ?
            circleImageView.setImageBitmap(MainUtil.getBitmapByPath(
                    MainUtil.getPicturesDirectory(mainActivityConnector),
                    photoFlat.getUuid().concat(".jpg")));
        }
        else {
            circleImageView.setImageResource(R.drawable.flat);
            textViewPhotoDate.setText("нет фото");
        }

        List<FlatStatus> flatStatuses = presenter.loadFlatStatuses();
        final FlatStatusListAdapter adapter = new FlatStatusListAdapter(mainActivityConnector,
                R.layout.simple_spinner_item, flatStatuses, R.color.mdtp_white);
        statusSpinner.setAdapter(adapter);
        for (int pos = 0; pos < flatStatuses.size(); pos++)
            if (flatStatuses.get(pos).equals(flat.getFlatStatus())) {
                statusSpinner.setSelection(pos);
            }

        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (!adapter.getItem(position).equals(flat.getFlatStatus()))
                    presenter.updateFlatStatus(flat,adapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        // TODO когда определимся с фото здесь будет грид с последними фото
        //gridView.setAdapter(new ImageGridAdapter(mainActivityConnector, photoFlat));
        //gridView.invalidateViews();

        make_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, ACTIVITY_PHOTO);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        new_flat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddEquipmentActivity.class);
                intent.putExtra(FLAT_UUID, flat.getUuid());
                intent.putExtra(HOUSE_UUID, flat.getHouse().getUuid());
                startActivity(intent);
            }
        });

        recyclerView =  view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void setPresenter(@NonNull FlatContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivityConnector = getActivity();
        // TODO решить что делать если контекст не приехал
        if (mainActivityConnector==null)
            onDestroyView();
    }

    /**
     * Show flats with recycler view.
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
                Intent intent = new Intent(getContext(), EquipmentActivity.class);
                String uuid = list.get(position).getUuid();
                intent.putExtra("EQUIPMENT_UUID", String.valueOf(uuid));
                startActivity(intent);
            }
        });
        //showEmptyView(list.isEmpty());
    }

    /**
     * Сохраняем фото
     *
     * @param requestCode The request code. See at {@link WorkFragment}.
     * @param resultCode  The result code.
     * @param data        The result.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ACTIVITY_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null && data.getExtras() != null) {
                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                        if (bitmap != null) {
                            String uuid = java.util.UUID.randomUUID().toString();
                            MainUtil.storeNewImage(bitmap, getContext(),
                                    800, uuid.concat(".jpg"));
                            MainUtil.storePhotoFlat(flat,uuid);
                            circleImageView.setImageBitmap(bitmap);
                            String sDate = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.US).
                                    format(new Date());
                            textViewPhotoDate.setText(sDate);
                        }
                    }
                }
                break;
            default:
                break;
        }
    }
}
