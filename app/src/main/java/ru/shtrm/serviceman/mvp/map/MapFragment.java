package ru.shtrm.serviceman.mvp.map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.Alarm;
import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.source.local.AlarmLocalDataSource;
import ru.shtrm.serviceman.gps.TaskItemizedOverlay;
import ru.shtrm.serviceman.interfaces.OnRecyclerViewItemClickListener;
import ru.shtrm.serviceman.mvp.MainActivity;
import ru.shtrm.serviceman.mvp.abonents.HouseAdapter;

import static android.content.Context.LOCATION_SERVICE;

public class MapFragment extends Fragment implements MapContract.View {
    private static final String TAG = MapFragment.class.getSimpleName();
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_map, container, false);

        initViews(contentView);

        setHasOptionsMenu(true);

        // пример запуска штатного приложения для получения фотографии привязанной к какой-то модели
        new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // пример запуска активити для получения фотографии
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
                MainActivity.photoFile = photoFile.getAbsolutePath();
                // пока ни к чему реальному фотки не привязываются
                MainActivity.objectUuid = java.util.UUID.randomUUID().toString().toUpperCase();
                MainActivity.photoUuid = photoUuid;

                try {
                    Uri photoURI = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    activity.startActivityForResult(intent, MainActivity.PHOTO_RESULT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

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

        mapController = mapView.getController();
        mapController.setZoom(17.0);
        aOverlayItemArray = new ArrayList<>();
        addAlarmsOverlay();
        addHouseOverlay();
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
//                    House house = list.get(position);
//                    if (house != null) {
//                        PhotoHouse photoHouse = PhotoHouseLocalDataSource.getInstance().
//                                getLastPhotoByHouse(house);
//                        if (photoHouse != null) {
//                            GeoPoint point2 = new GeoPoint(photoHouse.getLattitude(), photoHouse.getLongitude());
//                            mapController.setCenter(point2);
//                        }
//                    }
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
            timer.schedule(mTimerTask, 1, 3000);
        }
    }

    private void addAlarmsOverlay() {
        double curLatitude, curLongitude;
        final ArrayList<OverlayItem> alarmOverlayItemArray = new ArrayList<>();
        List<Alarm> alarms = AlarmLocalDataSource.getInstance().getAlarms();
        for (int i = 0; i < alarms.size(); i++) {
            Alarm alarm = alarms.get(i);
            curLatitude = alarm.getLatitude();
            curLongitude = alarm.getLongitude();

            OverlayItem olItem = new OverlayItem(alarm.getComment(),
                    "Alarm", new GeoPoint(curLatitude, curLongitude));
            Drawable newMarker;
            newMarker = this.getResources().getDrawable(R.drawable.baseline_add_location_black_24dp);
            olItem.setMarker(newMarker);
            alarmOverlayItemArray.add(olItem);
        }
        ItemizedIconOverlay<OverlayItem> aIconOverlay = new ItemizedIconOverlay<>(
                mainActivityConnector, alarmOverlayItemArray, null);
        mapView.getOverlays().add(aIconOverlay);
    }


    private void addHouseOverlay() {
        final ArrayList<OverlayItem> houseOverlayItemArray = new ArrayList<>();
        // TODO выбирать только для текущего пользователя
        //List<House> houses = HouseLocalDataSource.getInstance().getHousesByUser();
//        List<House> houses = HouseLocalDataSource.getInstance().getHouses();
//        for (House house : houses) {
//            PhotoHouse photoHouse = PhotoHouseLocalDataSource.getInstance().getLastPhotoByHouse(house);
//            if (photoHouse != null) {
//                HouseOverlayItem houseItem = new HouseOverlayItem(house.getFullTitle(),
//                        "Дом", new GeoPoint(photoHouse.getLattitude(), photoHouse.getLongitude()));
//                houseItem.house = house;
//                Drawable newMarker;
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    Resources.Theme theme = mainActivityConnector.getTheme();
//                    newMarker = this.getResources().getDrawable(R.drawable.marker_house, theme);
//                } else {
//                    newMarker = this.getResources().getDrawable(R.drawable.marker_house);
//                }
//                houseItem.setMarker(newMarker);
//                houseOverlayItemArray.add(houseItem);
//            }
//        }

        ItemizedIconOverlay<OverlayItem> aIconOverlay = new ItemizedIconOverlay<>(
                mainActivityConnector, houseOverlayItemArray, null);
        mapView.getOverlays().add(aIconOverlay);

        TaskItemizedOverlay overlay = new TaskItemizedOverlay(mainActivityConnector, houseOverlayItemArray) {
            @Override
            protected boolean onLongPressHelper(int index, OverlayItem item) {
                House house = ((HouseOverlayItem) item).house;
                String markerText = house.getFullTitle();
                if (house.getHouseType() != null)
                    markerText = markerText.concat(" - ").concat(house.getHouseType().getTitle());
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
                if (mainActivityConnector.getPreferences(Context.MODE_PRIVATE).getBoolean("gps_center", true))
                    mapController.setCenter(point2);
                OverlayItem overlayItem = new OverlayItem("Вы здесь", "WAH",
                        new GeoPoint(location.getLatitude(), location.getLongitude()));
                if (positionItemizedIconOverlay == null)
                    positionItemizedIconOverlay = new ItemizedIconOverlay<>(
                            mainActivityConnector, aOverlayItemArray, null);

                positionItemizedIconOverlay.removeAllItems();
                positionItemizedIconOverlay.addItem(overlayItem);
                mapView.getOverlays().add(positionItemizedIconOverlay);
            }
        }
    }

    private class HouseOverlayItem extends OverlayItem {
        public House house;

        HouseOverlayItem(String a, String b, GeoPoint p) {
            super(a, b, p);
        }
    }

}

