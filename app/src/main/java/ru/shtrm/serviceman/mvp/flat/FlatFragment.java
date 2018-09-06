package ru.shtrm.serviceman.mvp.flat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.Alarm;
import ru.shtrm.serviceman.data.AuthorizedUser;
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.Flat;
import ru.shtrm.serviceman.data.PhotoFlat;
import ru.shtrm.serviceman.data.Resident;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.data.source.PhotoFlatDataSource;
import ru.shtrm.serviceman.data.source.local.FlatLocalDataSource;
import ru.shtrm.serviceman.data.source.local.PhotoFlatLocalDataSource;
import ru.shtrm.serviceman.data.source.local.ResidentLocalDataSource;
import ru.shtrm.serviceman.data.source.local.UsersLocalDataSource;
import ru.shtrm.serviceman.interfaces.OnRecyclerViewItemClickListener;
import ru.shtrm.serviceman.mvp.abonents.FlatAdapter;
import ru.shtrm.serviceman.mvp.alarm.AlarmAdapter;
import ru.shtrm.serviceman.mvp.equipment.EquipmentActivity;
import ru.shtrm.serviceman.mvp.equipment.EquipmentAdapter;
import ru.shtrm.serviceman.mvp.images.ImageGridAdapter;
import ru.shtrm.serviceman.util.MainUtil;

import static ru.shtrm.serviceman.mvp.flat.FlatActivity.FLAT_ID;

public class FlatFragment extends Fragment implements FlatContract.View {
    private Activity mainActivityConnector = null;

    private FlatContract.Presenter presenter;
    private Flat flat;
    private Resident resident;
    private PhotoFlat photoFlat;

    private EquipmentAdapter equipmentAdapter;
    private RecyclerView recyclerView;

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
        View view;
        Bundle b = getArguments();
        if (b!=null) {
            String flatUuid = b.getString(FLAT_ID);
            if (flatUuid!=null)
                flat = FlatLocalDataSource.getInstance().getFlat(flatUuid);
        }
        view = inflater.inflate(R.layout.fragment_flat, container, false);
        if (flat!=null) {
            resident = ResidentLocalDataSource.getInstance().getResidentByFlat(flat.getUuid());
            photoFlat = PhotoFlatLocalDataSource.getInstance().getLastPhotoByFlat(flat);
            initViews(view);
        }

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter!=null)
            presenter.subscribe();
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
        TextView textViewStatus = view.findViewById(R.id.textViewFlatStatus);
        TextView textViewTitle = view.findViewById(R.id.textViewFlatTitle);
        TextView textViewPhotoDate = view.findViewById(R.id.textViewPhotoDate);
        TextView textViewFlat = view.findViewById(R.id.textViewFlat);
        CircleImageView circleImageView = view.findViewById(R.id.imageViewFlat);
        GridView gridView = view.findViewById(R.id.gridview);

        if (resident!=null) {
            textViewInn.setText(resident.getInn());
            textViewAbonent.setText(resident.getOwner());
        }
        textViewStatus.setText(flat.getFlatStatus().getTitle());
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

        // TODO когда определимся с фото здесь будет грид с последними фото
        //gridView.setAdapter(new ImageGridAdapter(mainActivityConnector, photoFlat));
        //gridView.invalidateViews();
        gridView.setVisibility(View.INVISIBLE);

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
                Equipment equipment = list.get(position);
                Intent intent = new Intent(getContext(), EquipmentActivity.class);
                intent.putExtra(EquipmentActivity.EQUIPMENT_ID, list.get(position).get_id());
                startActivity(intent);
            }
        });
        //showEmptyView(list.isEmpty());
    }

}
