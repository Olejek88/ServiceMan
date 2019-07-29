package ru.shtrm.serviceman.mvp.map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ru.shtrm.serviceman.BuildConfig;
import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.Task;
import ru.shtrm.serviceman.data.source.local.TaskLocalDataSource;
import ru.shtrm.serviceman.gps.TaskItemizedOverlay;
import ru.shtrm.serviceman.interfaces.OnRecyclerViewItemClickListener;
import ru.shtrm.serviceman.mvp.abonents.HouseAdapter;

import static android.content.Context.LOCATION_SERVICE;

public class MapFragment extends Fragment implements MapContract.View {
    ArrayList<OverlayItem> aOverlayItemArray;
    ItemizedIconOverlay<OverlayItem> positionItemizedIconOverlay;
    private Activity mainActivityConnector = null;
    private RecyclerView recyclerView;
    private HouseAdapter houseAdapter;
    private MapContract.Presenter presenter;
    private IMapController mapController;
    private MapView mapView;

    private Timer timer;
    private Handler mTimerHandler = new Handler();

    public MapFragment() {
    }

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Set a presenter for this fragment(View),
     *
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
    public void onDestroy() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    /**
     * Init the views by findViewById.
     *
     * @param view The container view.
     */
    @Override
    public void initViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mapView = view.findViewById(R.id.gps_mapview);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);

        mapController = mapView.getController();
        mapController.setZoom(17.0);
        aOverlayItemArray = new ArrayList<>();
        addTasksOverlay();
        // Добавляем несколько слоев
        CompassOverlay compassOverlay = new CompassOverlay(mainActivityConnector, mapView);
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
        Location bestLocation = null;
        LocationManager mLocationManager = (LocationManager) mainActivityConnector.getSystemService(LOCATION_SERVICE);
        int permission = ContextCompat.checkSelfPermission(mainActivityConnector, Manifest.permission.ACCESS_COARSE_LOCATION);
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
                    if (house != null) {
                        GeoPoint point = new GeoPoint(house.getLatitude(), house.getLongitude());
                        mapView.getController().animateTo(point);
                    }
                }
            });

            recyclerView.setAdapter(houseAdapter);
        } else {
            houseAdapter.updateData(list);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivityConnector = getActivity();
        // TODO решить что делать если контекст не приехал
        if (mainActivityConnector == null)
            onDestroyView();
        if (timer == null) {
            timer = new Timer();
            TimerTask mTimerTask = new TimerTask() {
                public void run() {
                    mTimerHandler.post(new Runnable() {
                        public void run() {
                            setPositionMarker();
                        }
                    });
                }
            };
            timer.schedule(mTimerTask, 1, 20000);
        }
    }

    private void addTasksOverlay() {
        double curLatitude, curLongitude;
        final ArrayList<OverlayItem> taskOverlayItemArray = new ArrayList<>();
        List<Task> tasks = TaskLocalDataSource.getInstance().getNewTasks();
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            curLatitude = task.getEquipment().getObject().getHouse().getLatitude();
            curLongitude = task.getEquipment().getObject().getHouse().getLongitude();

            OverlayItem olItem = new OverlayItem(task.getTaskTemplate().getTitle()
                    .concat(" / ")
                    .concat(task.getEquipment().getObject().getFullTitle()),
                    "Задача", new GeoPoint(curLatitude, curLongitude));
            Drawable newMarker;
            newMarker = this.getResources().getDrawable(R.drawable.task_marker_orange);
            olItem.setMarker(newMarker);
            taskOverlayItemArray.add(olItem);
        }
        ItemizedIconOverlay<OverlayItem> aIconOverlay = new ItemizedIconOverlay<>(
                mainActivityConnector, taskOverlayItemArray, null);
        mapView.getOverlays().add(aIconOverlay);

        TaskItemizedOverlay overlay = new TaskItemizedOverlay(mainActivityConnector, taskOverlayItemArray) {
            @Override
            protected boolean onLongPressHelper(int index, OverlayItem item) {
                Task task = ((TaskOverlayItem) item).task;
                String markerText = task.getEquipment().getObject().getFullTitle();
                Toast.makeText(mainActivityConnector, markerText, Toast.LENGTH_SHORT).show();
                return super.onLongPressHelper(index, item);
            }
        };
        mapView.getOverlays().add(overlay);
    }

    private void setPositionMarker() {
        Location location = getLastKnownLocation();
        if (location != null) {
            GeoPoint point2 = new GeoPoint(location.getLatitude(), location.getLongitude());
            if (mapController != null && mapView != null && aOverlayItemArray != null) {
                Drawable newMarker;
                newMarker = this.getResources().getDrawable(R.drawable.worker_marker_green);

                if (mainActivityConnector.getPreferences(Context.MODE_PRIVATE).getBoolean("gps_center", true))
                    mapController.setCenter(point2);
                OverlayItem overlayItem = new OverlayItem("Вы здесь", "WAH",
                        new GeoPoint(location.getLatitude(), location.getLongitude()));
                overlayItem.setMarker(newMarker);
                if (positionItemizedIconOverlay == null)
                    positionItemizedIconOverlay = new ItemizedIconOverlay<>(
                            mainActivityConnector, aOverlayItemArray, null);

                positionItemizedIconOverlay.removeAllItems();
                positionItemizedIconOverlay.addItem(overlayItem);
                mapView.getOverlays().add(positionItemizedIconOverlay);
            }
        }
    }

    private class TaskOverlayItem extends OverlayItem {
        public Task task;
        TaskOverlayItem(String a, String b, GeoPoint p) {
            super(a, b, p);
        }
    }

}

