package ru.shtrm.serviceman.mvp.flat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.FlatStatus;
import ru.shtrm.serviceman.data.source.EquipmentRepository;
import ru.shtrm.serviceman.data.source.FlatRepository;
import ru.shtrm.serviceman.data.source.FlatStatusRepository;
import ru.shtrm.serviceman.data.source.UsersRepository;
import ru.shtrm.serviceman.data.source.local.EquipmentLocalDataSource;
import ru.shtrm.serviceman.data.source.local.FlatLocalDataSource;
import ru.shtrm.serviceman.data.source.local.FlatStatusLocalDataSource;
import ru.shtrm.serviceman.data.source.local.UsersLocalDataSource;
import ru.shtrm.serviceman.util.MainUtil;

public class FlatActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private FlatFragment fragment;

    public static final String FLAT_ID = "FLAT_ID";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);

        MainUtil.setToolBarColor(this, this);

        if (savedInstanceState != null) {
            fragment = (FlatFragment) getSupportFragmentManager().
                    getFragment(savedInstanceState, "FlatFragment");
        } else {
            fragment = FlatFragment.newInstance();
        }

        if (!fragment.isAdded()) {
            Bundle b = new Bundle();
            b.putString(FLAT_ID, getIntent().getStringExtra(FLAT_ID));
            fragment.setArguments(b);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.view_pager, fragment, "FlatFragment")
                    .commit();
        }

        new FlatPresenter(
                fragment,
                FlatRepository.getInstance(FlatLocalDataSource.getInstance()),
                EquipmentRepository.getInstance(EquipmentLocalDataSource.getInstance()),
                FlatStatusRepository.getInstance(FlatStatusLocalDataSource.getInstance()),
                getIntent().getStringExtra(FLAT_ID));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (fragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "FlatFragment", fragment);
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