package ru.shtrm.serviceman.mvp.profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.util.MainUtil;

public class UserDetailActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private UserDetailFragment fragment;

    public static final String USER_ID = "USER_ID";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);

        MainUtil.setToolBarColor(this, this);

        if (savedInstanceState != null) {
            fragment = (UserDetailFragment) getSupportFragmentManager().
                    getFragment(savedInstanceState, "UserDetailFragment");
        } else {
            fragment = UserDetailFragment.newInstance();
        }

        if (!fragment.isAdded()) {
            Bundle b = new Bundle();
            b.putString(USER_ID, getIntent().getStringExtra(USER_ID));
            fragment.setArguments(b);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.view_pager, fragment, "UserDetailFragment")
                    .commit();
        }

        new UserDetailPresenter(fragment);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (fragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "UserDetailFragment", fragment);
        }
    }

    /**
     * Handle different items of the navigation drawer
     *
     * @param item The selected item.
     * @return Selected or not.
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }
}