package ru.shtrm.serviceman.mvp.map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;

import java.util.ArrayList;
import java.util.List;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.Alarm;
import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.source.local.AlarmLocalDataSource;
import ru.shtrm.serviceman.interfaces.OnRecyclerViewItemClickListener;
import ru.shtrm.serviceman.mvp.abonents.HouseAdapter;

import static android.content.Context.LOCATION_SERVICE;

public class MapFragment extends Fragment implements MapContract.View {
    private Activity mainActivityConnector = null;
    ArrayList<OverlayItem> aOverlayItemArray;

    // View references
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private LinearLayout emptyView;

    private HouseAdapter houseAdapter;
    private MapContract.Presenter presenter;

    public MapFragment() {}

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Set a presenter for this fragment(View),
     * @param presenter The presenter.
     */
    @Override
    public void setPresenter(@NonNull MapContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_map, container, false);

        initViews(contentView);

        // The function of BottomNavigationView is just as a filter.
        // We need not to build a fragment for each option.
        // Filter the data in presenter and then show it.
/*
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_checkin:
                        break;
                    case R.id.nav_users:
                        break;
                    case R.id.nav_map:
                        break;
                    case R.id.nav_alarms:
                        break;
                }
                return true;
            }
        });
*/

        // Set true to inflate the options menu.
        setHasOptionsMenu(true);

        return contentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unsubscribe();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    /**
     * Init the views by findViewById.
     * @param view The container view.
     */
    @Override
    public void initViews(View view) {
        Location location;
        double curLatitude=55.5, curLongitude=55.5;

        bottomNavigationView = view.findViewById(R.id.bottomNavigationView);
        emptyView =  view.findViewById(R.id.emptyView);
        recyclerView =  view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        LocationManager lm = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        int permission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION);
        if (lm != null && permission == PackageManager.PERMISSION_GRANTED) {
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null) {
                location = getLastKnownLocation();
            }
            if (location != null) {
                curLatitude = location.getLatitude();
                curLongitude = location.getLongitude();
            }
        }

        final MapView mapView = view.findViewById(R.id.gps_mapview);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        IMapController mapController = mapView.getController();
        mapController.setZoom(15.0);
        GeoPoint point2 = new GeoPoint(curLatitude, curLongitude);
        mapController.setCenter(point2);

        OverlayItem overlayItem = new OverlayItem("We are here", "WAH",
                new GeoPoint(curLatitude, curLongitude));
        aOverlayItemArray = new ArrayList<>();
        aOverlayItemArray.add(overlayItem);
        ItemizedIconOverlay<OverlayItem> aItemizedIconOverlay = new ItemizedIconOverlay<>(
                getActivity().getApplicationContext(), aOverlayItemArray, null);
        mapView.getOverlays().add(aItemizedIconOverlay);

        // TODO выводить маркеры домов?
        /*
        final ArrayList<GeoPoint> waypoints = new ArrayList<>();
        GeoPoint currentPoint = new GeoPoint(curLatitude, curLongitude);
        waypoints.add(currentPoint);
        */

        List<Alarm> alarms = AlarmLocalDataSource.getInstance().getAlarms();
        for (int i=0; i<alarms.size(); i++) {
            Alarm alarm = alarms.get(i);
            curLatitude = alarm.getLatitude();
            curLongitude = alarm.getLongitude();

            OverlayItem olItem = new OverlayItem(alarm.getComment(),
                    "Alarm", new GeoPoint(curLatitude, curLongitude));
            Drawable newMarker;
            newMarker = this.getResources().getDrawable(R.drawable.baseline_add_location_black_24dp);
            olItem.setMarker(newMarker);
            aOverlayItemArray.add(olItem);
        }

/*
        houseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                OverlayItem item = overlayItemArray.get(position);
                // Get the new Drawable
                Drawable marker = view.getResources().getDrawable(R.drawable.equipment_32);
                // Set the new marker
                item.setMarker(marker);
                if (LastItemPosition >= 0) {
                    OverlayItem item2 = overlayItemArray.get(LastItemPosition);
                    marker = view.getResources().getDrawable(R.drawable.marker_equip);
                    item2.setMarker(marker);
                }

                LastItemPosition = position;
            }
        });
        houseListView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //equipmentListView.getItemAtPosition();
                return false;
            }
        });

        TaskItemizedOverlay overlay = new TaskItemizedOverlay(getActivity().getApplicationContext(),
                overlayItemArray) {
            @Override
            protected boolean onLongPressHelper(int index, OverlayItem item) {
                Equipment equipment = ((OldMapFragment.EquipmentOverlayItem) item).equipment;
                Toast.makeText(
                        mContext,
                        "UUID оборудования " + equipment.getTitle() + " - "
                                + equipment.getUuid(), Toast.LENGTH_SHORT)
                        .show();


                String equipment_uuid = equipment.getUuid();
                Intent equipmentInfo = new Intent(getActivity(), EquipmentInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("equipment_uuid", equipment_uuid);
                equipmentInfo.putExtras(bundle);
                getActivity().startActivity(equipmentInfo);

                return super.onLongPressHelper(index, item);
            }
        };
        mapView.getOverlays().add(overlay);
*/

        // Добавляем несколько слоев
        CompassOverlay compassOverlay = new CompassOverlay(getActivity()
                .getApplicationContext(), mapView);
        compassOverlay.enableCompass();
        mapView.getOverlays().add(compassOverlay);

        ScaleBarOverlay mScaleBarOverlay = new ScaleBarOverlay(mapView);
        mScaleBarOverlay.setCentred(true);
        mScaleBarOverlay.setScaleBarOffset(200, 10);
        mapView.getOverlays().add(mScaleBarOverlay);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }

    private Location getLastKnownLocation() {
        FragmentActivity activity = getActivity();
        Location bestLocation = null;

        if (activity != null) {
            LocationManager mLocationManager;
            mLocationManager = (LocationManager) activity.getApplicationContext()
                    .getSystemService(LOCATION_SERVICE);
            int permission = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
            if (mLocationManager != null && permission == PackageManager.PERMISSION_GRANTED) {
                List<String> providers = mLocationManager.getProviders(true);
                for (String provider : providers) {
                    Location l = mLocationManager.getLastKnownLocation(provider);
                    if (l == null) {
                        continue;
                    }

                    if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                        bestLocation = l;
                    }
                }
            }
        }
        return bestLocation;
    }

    @Override
    public void showDefaultView(boolean toShow) {

    }

    public void showHouses(@NonNull final List<House> list) {
        if (houseAdapter == null) {
            houseAdapter = new HouseAdapter(mainActivityConnector, list);
            houseAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
                @Override
                public void OnItemClick(View v, int position) {
                    House house = list.get(position);
                }
            });
            recyclerView.setAdapter(houseAdapter);
        } else {
            houseAdapter.updateData(list);
        }
        //showEmptyView(list.isEmpty());
    }

    public void showAlarms(@NonNull final List<Alarm> list) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivityConnector = getActivity();
        // TODO решить что делать если контекст не приехал
        if (mainActivityConnector==null)
            onDestroyView();
    }

}

