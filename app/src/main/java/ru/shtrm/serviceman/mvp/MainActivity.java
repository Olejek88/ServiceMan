package ru.shtrm.serviceman.mvp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.Toast;

import io.realm.Realm;
import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.AuthorizedUser;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.data.source.UsersRepository;
import ru.shtrm.serviceman.data.source.local.UsersLocalDataSource;
import ru.shtrm.serviceman.mvp.profile.UserDetailFragment;
import ru.shtrm.serviceman.mvp.profile.UserDetailPresenter;
import ru.shtrm.serviceman.ui.PrefsActivity;
import ru.shtrm.serviceman.util.MainUtil;
import ru.shtrm.serviceman.util.SettingsUtil;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int REQUEST_WRITE_STORAGE = 2;
    private static final String TAG = "Main";

    private Realm realmDB;

    private Toolbar toolbar;
    private BottomNavigationView navigation;
    public static boolean isLogged = false;

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private UserDetailFragment profileFragment;

    private static final String KEY_NAV_ITEM = "CURRENT_NAV_ITEM";
    private static final String CURRENT_FILTERING_KEY = "CURRENT_FILTERING_KEY";

    private int selectedNavItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (PreferenceManager.getDefaultSharedPreferences(this).
                getBoolean("navigation_bar_tint", true)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setNavigationBarColor(
                        ContextCompat.getColor(this, R.color.colorPrimaryDark));
            }
        }

        if (savedInstanceState != null) {
            isLogged = savedInstanceState.getBoolean("isLogged");
            AuthorizedUser aUser = AuthorizedUser.getInstance();
            aUser.setToken(savedInstanceState.getString("token"));
            aUser.setId(savedInstanceState.getString("userId"));
        }
        else {
            User user = UsersLocalDataSource.getInstance().getAuthorisedUser();
            if (user==null) {
                user = UsersLocalDataSource.getInstance().getLastUser();
                if (user != null)
                    AuthorizedUser.getInstance().setId(user.get_id());
            }
        }

        // обнуляем текущего активного пользователя
        AuthorizedUser.getInstance().reset();

        if (!initDB()) {
            // принудительное обновление приложения
            finish();
        }

        if (isLogged) {
            initViews();
        } else {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        }

        if (savedInstanceState != null) {
            profileFragment = (UserDetailFragment) getSupportFragmentManager().
                    getFragment(savedInstanceState, "UserDetailFragment");
            selectedNavItem = savedInstanceState.getInt(KEY_NAV_ITEM);
        } else {
            profileFragment = (UserDetailFragment) getSupportFragmentManager().
                    findFragmentById(R.id.content_main);
            if (profileFragment == null) {
                profileFragment = UserDetailFragment.newInstance();
            }
        }

        if (profileFragment!=null && !profileFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_main, profileFragment, "UserDetailFragment")
                    .commit();
        }
        CheckPermission();

/*
        QuestionsRepository.destroyInstance();
        // Init the presenters.
        questionsPresenter = new QuestionsPresenter(questionsFragment,
                QuestionsRepository.getInstance(
                        QuestionsRemoteDataSource.getInstance(),
                        QuestionsLocalDataSource.getInstance()));
*/
        new UserDetailPresenter(profileFragment,
                UsersRepository.getInstance(UsersLocalDataSource.getInstance()),
                "");

        // Show the default fragment.
        if (selectedNavItem == 0) {
            //showAlarmFragment();
        } else if (selectedNavItem == 1) {
            //showMapFragment();
        } else if (selectedNavItem == 2) {
            //showCheckinFragment();
        } else if (selectedNavItem == 3) {
            //showReferencesFragment();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //sendBroadcast(AppWidgetProvider.getRefreshBroadcastIntent(getApplicationContext()));
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
                //showQuestionsFragment();
                break;
            case R.id.nav_users:
                //showUsersFragment();
                break;
            case R.id.nav_alarms:
                //showGalleryFragment();
                break;
            case R.id.nav_checkin:
                //showGalleryFragment();
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
        if (profileFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "ProfileFragment", profileFragment);
        }
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

        navigation = findViewById(R.id.bottomNavigationView);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_alarms:
                        break;
                    case R.id.nav_users:
                        break;
                    case R.id.nav_checkin:
                        break;
                    case R.id.nav_map:
                        break;
                }
                return true;
            }
        });

    }

    public void showAlarmsFragment() {
    }

    private void showProfileFragment() {
        changeFragment(profileFragment);
        toolbar.setTitle(getResources().getString(R.string.nav_profile));
        navigationView.setCheckedItem(R.id.nav_profile);
    }

    private void showReferencesFragment() {
    }

    private void showCheckinFragment() {
    }

    private void showMapFragment() {
    }

    void changeFragment(Fragment selectedFragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.hide(profileFragment);

        if (selectedFragment==profileFragment)
            fragmentTransaction.show(profileFragment);
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
            realmDB = Realm.getDefaultInstance();
            //LoadTestData.LoadAllTestData();
            Log.d(TAG, "Realm DB schema version = " + realmDB.getVersion());
            Log.d(TAG, "db.version=" + realmDB.getVersion());
            if (realmDB.getVersion() == 0) {
                Toast toast = Toast.makeText(this, "База данных не актуальна!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 0, 0);
                toast.show();
                success = true;
            } else {
                Toast toast = Toast.makeText(this, "База данных актуальна!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 0);
                toast.show();
                success = true;
            }
        } catch (Exception e) {
            Toast toast = Toast.makeText(this,
                    "Не удалось открыть/обновить базу данных!",
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.show();
        }

        return success;
    }

}
