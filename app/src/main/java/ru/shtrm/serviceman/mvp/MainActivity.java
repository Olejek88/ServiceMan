package ru.shtrm.serviceman.mvp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;
import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.AuthorizedUser;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.data.source.AlarmRepository;
import ru.shtrm.serviceman.data.source.FlatRepository;
import ru.shtrm.serviceman.data.source.HouseRepository;
import ru.shtrm.serviceman.data.source.StreetRepository;
import ru.shtrm.serviceman.data.source.UsersRepository;
import ru.shtrm.serviceman.data.source.local.AlarmLocalDataSource;
import ru.shtrm.serviceman.data.source.local.FlatLocalDataSource;
import ru.shtrm.serviceman.data.source.local.HouseLocalDataSource;
import ru.shtrm.serviceman.data.source.local.StreetLocalDataSource;
import ru.shtrm.serviceman.data.source.local.UsersLocalDataSource;
import ru.shtrm.serviceman.db.LoadTestData;
import ru.shtrm.serviceman.gps.GPSListener;
import ru.shtrm.serviceman.mvp.abonents.AbonentsFragment;
import ru.shtrm.serviceman.mvp.abonents.AbonentsPresenter;
import ru.shtrm.serviceman.mvp.abonents.WorkFragment;
import ru.shtrm.serviceman.mvp.alarm.AlarmFragment;
import ru.shtrm.serviceman.mvp.alarm.AlarmPresenter;
import ru.shtrm.serviceman.mvp.map.MapFragment;
import ru.shtrm.serviceman.mvp.map.MapPresenter;
import ru.shtrm.serviceman.mvp.profile.UserDetailFragment;
import ru.shtrm.serviceman.mvp.profile.UserDetailPresenter;
import ru.shtrm.serviceman.ui.PrefsActivity;
import ru.shtrm.serviceman.util.MainUtil;
import ru.shtrm.serviceman.util.SettingsUtil;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int REQUEST_WRITE_STORAGE = 2;
    private static final String TAG = "Main";

    private Toolbar toolbar;
    private BottomNavigationView navigation;
    public static boolean isLogged = false;
    private static final int LOGIN = 0;

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private UserDetailFragment profileFragment;
    private MapFragment mapFragment;
    private AbonentsFragment abonentsFragment;
    private AlarmFragment alarmsFragment;
    private WorkFragment workFragment;
    private Bundle currentSavedInstanceState;
    private static final String KEY_NAV_ITEM = "CURRENT_NAV_ITEM";

    private int selectedNavItem = 0;

    private LocationManager _locationManager;
    private GPSListener _gpsListener;
    private Thread checkGPSThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentSavedInstanceState = savedInstanceState;
        setContentView(R.layout.activity_main);

        if (PreferenceManager.getDefaultSharedPreferences(this).
                getBoolean("navigation_bar_tint", true)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setNavigationBarColor(
                        ContextCompat.getColor(this, R.color.colorPrimaryDark));
            }
        }

        if (!initDB()) {
            // принудительное обновление приложения
            finish();
        }

        if (savedInstanceState != null) {
            isLogged = savedInstanceState.getBoolean("isLogged");
        }
        else {
            User user = UsersLocalDataSource.getInstance().getAuthorisedUser();
            if (user==null) {
                user = UsersLocalDataSource.getInstance().getLastUser();
                if (user != null)
                    AuthorizedUser.getInstance().setId(user.getUuid());
            }
        }

        // обнуляем текущего активного пользователя
        AuthorizedUser.getInstance().reset();
        if (!isLogged) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivityForResult(loginIntent, LOGIN);
        }
        initViews();
        initFragments(currentSavedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN) {
            if (resultCode == RESULT_OK) {
                isLogged = true;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (checkGPSThread != null) {
            checkGPSThread.interrupt();
        }
        //sendBroadcast(AppWidgetProvider.getRefreshBroadcastIntent(getApplicationContext()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AuthorizedUser.getInstance().getId()!=null) {
            User user = UsersLocalDataSource.getInstance().getUser(AuthorizedUser.getInstance().getId());
            if (user != null) {
                TextView profileName = navigationView.getHeaderView(0).findViewById(R.id.name);
                profileName.setText(user.getName());
            }
        }
        CheckRunGPSListener();
    }

    /**
     * Close the drawer when a back click is called.
     */
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    /**
     * Handle different items of the navigation drawer
     * @param item The selected item.
     * @return Selected or not.
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.nav_profile:
                showProfileFragment();
                break;
            case R.id.nav_map:
                showMapFragment();
                break;
            case R.id.nav_users:
                showAbonentsFragment();
                break;
            case R.id.nav_alarms:
                showAlarmsFragment();
                break;
            case R.id.nav_checkin:
                showCheckinFragment();
                break;
            case R.id.nav_switch_theme:
                drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                    }

                    @Override
                    public void onDrawerOpened(@NonNull View drawerView) {
                    }

                    @Override
                    public void onDrawerClosed(@NonNull View drawerView) {
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                        if ((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)
                                == Configuration.UI_MODE_NIGHT_YES) {
                            sp.edit().putBoolean(SettingsUtil.KEY_NIGHT_MODE, false).apply();
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        } else {
                            sp.edit().putBoolean(SettingsUtil.KEY_NIGHT_MODE, true).apply();
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        }
                        getWindow().setWindowAnimations(R.style.WindowAnimationFadeInOut);
                        recreate();
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                    }
                });
                break;
            case R.id.nav_settings:
                intent = new Intent(MainActivity.this, PrefsActivity.class);
                intent.putExtra(PrefsActivity.EXTRA_FLAG, PrefsActivity.FLAG_SETTINGS);
                startActivity(intent);
                break;
            case R.id.nav_about:
                intent = new Intent(MainActivity.this, PrefsActivity.class);
                intent.putExtra(PrefsActivity.EXTRA_FLAG, PrefsActivity.FLAG_ABOUT);
                startActivity(intent);
                break;
        }

        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Store the state when the activity may be recycled.
     * @param outState The state data.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //outState.putSerializable(CURRENT_FILTERING_KEY, questionsPresenter.getFiltering());
        super.onSaveInstanceState(outState);
        Menu menu = navigationView.getMenu();
        if (menu.findItem(R.id.nav_profile).isChecked()) {
            outState.putInt(KEY_NAV_ITEM, 0);
        } else if (menu.findItem(R.id.nav_users).isChecked()) {
            outState.putInt(KEY_NAV_ITEM, 1);
        }
        // Store the fragments' states.
        if (mapFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "MapFragment", mapFragment);
        }
        if (alarmsFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "AlarmFragment", alarmsFragment);
        }
        if (profileFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "UserFragment", profileFragment);
        }
        if (abonentsFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "AbonentFragment", abonentsFragment);
        }
        if (workFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "WorkFragment", workFragment);
        }
    }

    private void initFragments(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            profileFragment = (UserDetailFragment) getSupportFragmentManager().
                    getFragment(savedInstanceState, "UserFragment");
            abonentsFragment = (AbonentsFragment) getSupportFragmentManager().
                    getFragment(savedInstanceState, "AbonentFragment");
            alarmsFragment = (AlarmFragment) getSupportFragmentManager().
                    getFragment(savedInstanceState, "AlarmFragment");
            mapFragment = (MapFragment) getSupportFragmentManager().
                    getFragment(savedInstanceState, "MapFragment");
            selectedNavItem = savedInstanceState.getInt(KEY_NAV_ITEM);
        } else {
            abonentsFragment = (AbonentsFragment) getSupportFragmentManager().
                    findFragmentById(R.id.content_main);
            mapFragment = (MapFragment) getSupportFragmentManager().
                    findFragmentById(R.id.content_main);
            alarmsFragment = (AlarmFragment) getSupportFragmentManager().
                    findFragmentById(R.id.content_main);
            profileFragment = (UserDetailFragment) getSupportFragmentManager().
                    findFragmentById(R.id.content_main);
            workFragment = (WorkFragment) getSupportFragmentManager().
                    findFragmentById(R.id.content_main);

            if (profileFragment == null)
                profileFragment = UserDetailFragment.newInstance();
            if (abonentsFragment == null)
                abonentsFragment = AbonentsFragment.newInstance();
            if (mapFragment == null)
                mapFragment = MapFragment.newInstance();
            if (alarmsFragment == null)
                alarmsFragment = AlarmFragment.newInstance();
            if (workFragment == null)
                workFragment = WorkFragment.newInstance();
        }

        if (profileFragment!=null && !profileFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_main, profileFragment, "UserFragment")
                    .commit();
        }
        if (mapFragment!=null && !mapFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_main, mapFragment, "MapFragment")
                    .commit();
        }
        if (alarmsFragment!=null && !alarmsFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_main, alarmsFragment, "AlarmFragment")
                    .commit();
        }
        if (abonentsFragment!=null && !abonentsFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_main, abonentsFragment, "AbonentFragment")
                    .commit();
        }
        if (workFragment!=null && !workFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_main, workFragment, "WorkFragment")
                    .commit();
        }

        CheckPermission();

        new UserDetailPresenter(profileFragment,
                UsersRepository.getInstance(UsersLocalDataSource.getInstance()),
                "");
        new MapPresenter(mapFragment,
                HouseRepository.getInstance(HouseLocalDataSource.getInstance()));

        new AlarmPresenter(alarmsFragment,
                AlarmRepository.getInstance(AlarmLocalDataSource.getInstance()));

        new AbonentsPresenter(abonentsFragment,
                StreetRepository.getInstance(StreetLocalDataSource.getInstance()),
                HouseRepository.getInstance(HouseLocalDataSource.getInstance()),
                FlatRepository.getInstance(FlatLocalDataSource.getInstance()));

        new AbonentsPresenter(workFragment,
                StreetRepository.getInstance(StreetLocalDataSource.getInstance()),
                HouseRepository.getInstance(HouseLocalDataSource.getInstance()),
                FlatRepository.getInstance(FlatLocalDataSource.getInstance())
        );

        // Show the default fragment.
        if (selectedNavItem == 0) {
            showMapFragment();
        } else if (selectedNavItem == 1) {
            showAbonentsFragment();
        } else if (selectedNavItem == 2) {
            showAlarmsFragment();
        } else if (selectedNavItem == 3) {
            showCheckinFragment();
        }

        toolbar.setTitle(getResources().getString(R.string.nav_map));
    }

    /**
     * Init views by calling findViewById.
     */
    private void initViews() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ImageView profileImage = navigationView.getHeaderView(0).findViewById(R.id.user_image);
        profileImage.setImageResource(R.drawable.user_random_icon_6);

        navigation = findViewById(R.id.bottomNavigationView);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_alarms:
                        showAlarmsFragment();
                        break;
                    case R.id.nav_users:
                        showAbonentsFragment();
                        break;
                    case R.id.nav_checkin:
                        showCheckinFragment();
                        break;
                    case R.id.nav_map:
                        showMapFragment();
                        break;
                }
                return true;
            }
        });

    }

    public void showAlarmsFragment() {
        changeFragment(alarmsFragment);
        toolbar.setTitle(getResources().getString(R.string.nav_alarms));
        navigationView.setCheckedItem(R.id.nav_alarms);
    }

    private void showProfileFragment() {
        changeFragment(profileFragment);
        toolbar.setTitle(getResources().getString(R.string.nav_profile));
        navigationView.setCheckedItem(R.id.nav_profile);
    }

    private void showAbonentsFragment() {
        changeFragment(abonentsFragment);
        toolbar.setTitle(getResources().getString(R.string.nav_users));
        navigationView.setCheckedItem(R.id.nav_users);
    }

    private void showCheckinFragment() {
        changeFragment(workFragment);
        toolbar.setTitle(getResources().getString(R.string.nav_checkin));
        navigationView.setCheckedItem(R.id.nav_checkin);
    }

    private void showMapFragment() {
        changeFragment(mapFragment);
        toolbar.setTitle(getResources().getString(R.string.nav_map));
        navigationView.setCheckedItem(R.id.nav_map);
    }

    void changeFragment(Fragment selectedFragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.hide(mapFragment);
        fragmentTransaction.hide(abonentsFragment);
        fragmentTransaction.hide(alarmsFragment);
        fragmentTransaction.hide(profileFragment);
        fragmentTransaction.hide(workFragment);

        if (selectedFragment==mapFragment)
            fragmentTransaction.show(mapFragment);
        if (selectedFragment==abonentsFragment)
            fragmentTransaction.show(abonentsFragment);
        if (selectedFragment==alarmsFragment)
            fragmentTransaction.show(alarmsFragment);
        if (selectedFragment==profileFragment)
            fragmentTransaction.show(profileFragment);
        if (selectedFragment==workFragment)
            fragmentTransaction.show(workFragment);
        fragmentTransaction.commit();
    }

    private void CheckPermission () {
        // Create the storage directory if it does not exist
        if (MainUtil.isExternalStorageWritable()) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult ( int requestCode,
                                             @NonNull String permissions[], @NonNull int[] grantResults){
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,
                            getResources().getString(R.string.message_no_write_permission),
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public boolean initDB() {
        boolean success = false;
        try {
            // получаем базу realm
            Realm realmDB = Realm.getDefaultInstance();
            Log.d(TAG, "Realm DB schema version = " + realmDB.getVersion());
            Log.d(TAG, "db.version=" + realmDB.getVersion());
            if (realmDB.getVersion() == 0) {
                Toast toast = Toast.makeText(this, "База данных не актуальна!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 0, 0);
                toast.show();
                success = true;
            } else {
/*
                Toast toast = Toast.makeText(this, "База данных актуальна!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 0);
                toast.show();
*/
                success = true;
            }
        } catch (Exception e) {
            Toast toast = Toast.makeText(this,
                    "Не удалось открыть/обновить базу данных!",
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.show();
        }
        //LoadTestData.LoadAllTestData();
        //LoadTestData.LoadAllTestData2();
        return success;
    }
    
    void CheckRunGPSListener() {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                boolean isRun = true;
                while (isRun) {
                    try {
                        Thread.sleep(5000);
                        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        if (!isSkipGPS()) {
                            boolean gpsEnabled = lm != null && lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                            if (!gpsEnabled) {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "GPS должен быть включен",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e) {
                        isRun = false;
                    }
                }
            }
        };
        checkGPSThread = new Thread(run);
        checkGPSThread.start();

        _locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (_locationManager != null && permission == PackageManager.PERMISSION_GRANTED) {
            _gpsListener = new GPSListener();
            _locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, _gpsListener);
        }
    }
    /**
     * Проверка включен ли GPS.
     *
     * @return boolean
     */
    private boolean isGpsOn() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return lm != null && lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * Проверка на необходимость GPS
     */
    private boolean isSkipGPS() {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        return sp.getBoolean(getString(R.string.gps), false);
    }

}
