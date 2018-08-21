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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.appwidget.AppWidgetProvider;
import ru.shtrm.serviceman.data.AuthorizedUser;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.data.source.ImagesRepository;
import ru.shtrm.serviceman.data.source.QuestionsRepository;
import ru.shtrm.serviceman.data.source.TricksRepository;
import ru.shtrm.serviceman.data.source.UsersRepository;
import ru.shtrm.serviceman.data.source.local.ImagesLocalDataSource;
import ru.shtrm.serviceman.data.source.local.QuestionsLocalDataSource;
import ru.shtrm.serviceman.data.source.local.TricksLocalDataSource;
import ru.shtrm.serviceman.data.source.local.UsersLocalDataSource;
import ru.shtrm.serviceman.data.source.remote.QuestionsRemoteDataSource;
import ru.shtrm.serviceman.data.source.remote.TricksRemoteDataSource;
import ru.shtrm.serviceman.mvp.images.ImagesFragment;
import ru.shtrm.serviceman.mvp.images.ImagesPresenter;
import ru.shtrm.serviceman.mvp.profile.UserDetailFragment;
import ru.shtrm.serviceman.mvp.profile.UserDetailPresenter;
import ru.shtrm.serviceman.mvp.questionedit.QuestionEditFragment;
import ru.shtrm.serviceman.mvp.questions.QuestionFilterType;
import ru.shtrm.serviceman.mvp.questions.QuestionsFragment;
import ru.shtrm.serviceman.mvp.questions.QuestionsPresenter;
import ru.shtrm.serviceman.mvp.tricks.TricksFragment;
import ru.shtrm.serviceman.mvp.tricks.TricksPresenter;
import ru.shtrm.serviceman.mvp.users.UsersFragment;
import ru.shtrm.serviceman.mvp.users.UsersPresenter;
import ru.shtrm.serviceman.ui.PrefsActivity;
import ru.shtrm.serviceman.util.MainUtil;
import ru.shtrm.serviceman.util.PushUtil;
import ru.shtrm.serviceman.util.SettingsUtil;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int REQUEST_WRITE_STORAGE = 2;

    private Toolbar toolbar;
    private BottomNavigationView navigation;

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private ImagesFragment imagesFragment;
    private QuestionsFragment questionsFragment;
    private QuestionEditFragment questionEditFragment;
    private UsersFragment usersFragment;
    private UserDetailFragment profileFragment;
    private TricksFragment tricksFragment;

    private QuestionsPresenter questionsPresenter;

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
            AuthorizedUser aUser = AuthorizedUser.getInstance();
            aUser.setToken(savedInstanceState.getString("token"));
            aUser.setId(savedInstanceState.getString("userId"));
        }
        else {
            User user = UsersLocalDataSource.getInstance().getAuthorisedUser();
            if (user==null) {
                user = UsersLocalDataSource.getInstance().getLastUser();
                if (user != null)
                    AuthorizedUser.getInstance().setId(user.getId());
            }
        }

        initViews();

        if (savedInstanceState != null) {
            questionsFragment = (QuestionsFragment) getSupportFragmentManager().
                    getFragment(savedInstanceState, "QuestionsFragment");
            usersFragment = (UsersFragment) getSupportFragmentManager().
                    getFragment(savedInstanceState, "UsersFragment");
            profileFragment = (UserDetailFragment) getSupportFragmentManager().
                    getFragment(savedInstanceState, "UserDetailFragment");
            imagesFragment = (ImagesFragment) getSupportFragmentManager().
                    getFragment(savedInstanceState, "ImagesFragment");
            tricksFragment = (TricksFragment) getSupportFragmentManager().
                    getFragment(savedInstanceState, "TricksFragment");
/*
            questionEditFragment = (QuestionEditFragment) getSupportFragmentManager().
                    getFragment(savedInstanceState,"QuestionEditFragment");
*/
            selectedNavItem = savedInstanceState.getInt(KEY_NAV_ITEM);
        } else {
            questionsFragment = (QuestionsFragment) getSupportFragmentManager().
                    findFragmentById(R.id.content_main);
            if (questionsFragment == null) {
                questionsFragment = QuestionsFragment.newInstance();
            }

/*
            questionEditFragment = (QuestionEditFragment) getSupportFragmentManager().
                    findFragmentById(R.id.content_main);
            if (questionEditFragment == null) {
                questionEditFragment = QuestionEditFragment.newInstance();
            }
*/

            usersFragment = (UsersFragment) getSupportFragmentManager().
                    findFragmentById(R.id.content_main);
            if (usersFragment == null) {
                usersFragment = UsersFragment.newInstance();
            }

            profileFragment = (UserDetailFragment) getSupportFragmentManager().
                    findFragmentById(R.id.content_main);
            if (profileFragment == null) {
                profileFragment = UserDetailFragment.newInstance();
            }

            imagesFragment = (ImagesFragment) getSupportFragmentManager().
                    findFragmentById(R.id.content_main);
            if (imagesFragment == null) {
                imagesFragment = ImagesFragment.newInstance();
            }

            tricksFragment = (TricksFragment) getSupportFragmentManager().
                    findFragmentById(R.id.content_main);
            if (tricksFragment == null) {
                tricksFragment = TricksFragment.newInstance();
            }
        }

        // Add the fragments.
        if (!questionsFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_main, questionsFragment, "QuestionsFragment")
                    .commit();
        }

/*
        if (!questionEditFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_main, questionEditFragment, "QuestionEditFragment")
                    .commit();
        }
*/

        if (!usersFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_main, usersFragment, "UsersFragment")
                    .commit();
        }

        if (profileFragment!=null && !profileFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_main, profileFragment, "UserDetailFragment")
                    .commit();
        }

        if (!imagesFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_main, imagesFragment, "ImagesFragment")
                    .commit();
        }

        if (!tricksFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_main, tricksFragment, "TricksFragment")
                    .commit();
        }

        CheckPermission();

        // Make sure the data in repository is the latest.
        // Also to void the repo only contains a package
        // when user has already gone to detail page
        // by check a notification or widget.
        QuestionsRepository.destroyInstance();
        // Init the presenters.
        questionsPresenter = new QuestionsPresenter(questionsFragment,
                QuestionsRepository.getInstance(
                        QuestionsRemoteDataSource.getInstance(),
                        QuestionsLocalDataSource.getInstance()));
        new UsersPresenter(usersFragment,
                UsersRepository.getInstance(UsersLocalDataSource.getInstance()));

/*
        new QuestionEditPresenter("",
                QuestionsRepository.getInstance(
                        QuestionsRemoteDataSource.getInstance(),
                        QuestionsLocalDataSource.getInstance()), questionEditFragment);
*/

        new UserDetailPresenter(profileFragment,
                UsersRepository.getInstance(UsersLocalDataSource.getInstance()),
                "");

        new ImagesPresenter(QuestionsRepository.getInstance(
                        QuestionsRemoteDataSource.getInstance(),
                        QuestionsLocalDataSource.getInstance()),
                ImagesRepository.getInstance(ImagesLocalDataSource.getInstance()),
                imagesFragment);

        new TricksPresenter(tricksFragment,
                TricksRepository.getInstance(TricksRemoteDataSource.getInstance(),
                TricksLocalDataSource.getInstance()));

        // Get data from Bundle.
        if (savedInstanceState != null) {
            QuestionFilterType currentFiltering = (QuestionFilterType) savedInstanceState.
                    getSerializable(CURRENT_FILTERING_KEY);
            if (currentFiltering!=null)
                questionsPresenter.setFiltering(currentFiltering);
        }

        // Show the default fragment.
        if (selectedNavItem == 0) {
            showProfileFragment();
        } else if (selectedNavItem == 1) {
            showUsersFragment();
        } else if (selectedNavItem == 2) {
            showQuestionsFragment();
        }

        PushUtil.startReminderService(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        sendBroadcast(AppWidgetProvider.getRefreshBroadcastIntent(getApplicationContext()));
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
            case R.id.nav_questions:
                showQuestionsFragment();
                break;
            case R.id.nav_users:
                showUsersFragment();
                break;
            case R.id.nav_gallery:
                showGalleryFragment();
                break;
            case R.id.nav_tricks:
                showTricksFragment();
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
        outState.putSerializable(CURRENT_FILTERING_KEY, questionsPresenter.getFiltering());
        super.onSaveInstanceState(outState);
        Menu menu = navigationView.getMenu();
        if (menu.findItem(R.id.nav_profile).isChecked()) {
            outState.putInt(KEY_NAV_ITEM, 0);
        } else if (menu.findItem(R.id.nav_users).isChecked()) {
            outState.putInt(KEY_NAV_ITEM, 1);
        }
        // Store the fragments' states.
        if (questionsFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "QuestionsFragment", questionsFragment);
        }
        if (usersFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "UsersFragment", usersFragment);
        }
        if (imagesFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "ImagesFragment", imagesFragment);
        }
        if (tricksFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "TricksFragment", tricksFragment);
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
                    case R.id.nav_profile:
                        showProfileFragment();
                        break;
                    case R.id.nav_questions:
                        showQuestionsFragment();
                        break;
                    case R.id.nav_users:
                        showUsersFragment();
                        break;
                    case R.id.nav_tricks:
                        showTricksFragment();
                        break;
                    case R.id.nav_gallery:
                        showGalleryFragment();
                        break;
                }
                return true;
            }
        });

    }

    public void showQuestionsFragment() {
        changeFragment(questionsFragment);
        toolbar.setTitle(getResources().getString(R.string.nav_questions));
        navigationView.setCheckedItem(R.id.nav_profile);
    }

    private void showProfileFragment() {
        changeFragment(profileFragment);
        toolbar.setTitle(getResources().getString(R.string.nav_profile));
        navigationView.setCheckedItem(R.id.nav_profile);
    }

    private void showUsersFragment() {
        changeFragment(usersFragment);
        toolbar.setTitle(getResources().getString(R.string.nav_users));
        navigationView.setCheckedItem(R.id.nav_users);
    }

    private void showGalleryFragment() {
        changeFragment(imagesFragment);
        toolbar.setTitle(getResources().getString(R.string.nav_gallery));
        navigationView.setCheckedItem(R.id.nav_gallery);
    }

    private void showTricksFragment() {
        changeFragment(tricksFragment);
        toolbar.setTitle(getResources().getString(R.string.nav_tricks));
        navigationView.setCheckedItem(R.id.nav_tricks);
    }

    void changeFragment(Fragment selectedFragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.hide(tricksFragment);
        fragmentTransaction.hide(usersFragment);
        fragmentTransaction.hide(questionsFragment);
        fragmentTransaction.hide(profileFragment);
        fragmentTransaction.hide(imagesFragment);

        if (selectedFragment==tricksFragment)
            fragmentTransaction.show(tricksFragment);
        if (selectedFragment==usersFragment)
            fragmentTransaction.show(usersFragment);
        if (selectedFragment==questionsFragment)
            fragmentTransaction.show(questionsFragment);
        if (selectedFragment==profileFragment)
            fragmentTransaction.show(profileFragment);
        if (selectedFragment==imagesFragment)
            fragmentTransaction.show(imagesFragment);
        fragmentTransaction.commit();
    }

    /**
     * Pass the selected question id to fragment.
     * @param id The selected question id.
     */
    public void setSelectedQuestionId(@NonNull String id) {
        questionsFragment.setSelectedQuestion(id);
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
}
