package ru.shtrm.serviceman.mvp.equipment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.source.EquipmentRepository;
import ru.shtrm.serviceman.data.source.EquipmentStatusRepository;
import ru.shtrm.serviceman.data.source.GpsTrackRepository;
import ru.shtrm.serviceman.data.source.local.EquipmentLocalDataSource;
import ru.shtrm.serviceman.data.source.local.EquipmentStatusLocalDataSource;
import ru.shtrm.serviceman.data.source.local.GpsTrackLocalDataSource;
import ru.shtrm.serviceman.util.MainUtil;

public class EquipmentActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private EquipmentFragment fragment;

    public static final String EQUIPMENT_ID = "EQUIPMENT_ID";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);

        MainUtil.setToolBarColor(this, this);

        if (savedInstanceState != null) {
            fragment = (EquipmentFragment) getSupportFragmentManager().
                    getFragment(savedInstanceState, "EquipmentFragment");
        } else {
            fragment = EquipmentFragment.newInstance();
        }

        if (!fragment.isAdded()) {
            String equipment_id = getIntent().getStringExtra(EQUIPMENT_ID);
            Bundle b = new Bundle();
            b.putString(EQUIPMENT_ID, equipment_id);
            fragment.setArguments(b);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.view_pager, fragment, "EquipmentFragment")
                    .commit();
        }

        new EquipmentPresenter(
                fragment,
                EquipmentRepository.getInstance(EquipmentLocalDataSource.getInstance()),
                EquipmentStatusRepository.getInstance(EquipmentStatusLocalDataSource.getInstance()),
                GpsTrackRepository.getInstance(GpsTrackLocalDataSource.getInstance()),
                getIntent().getStringExtra(EQUIPMENT_ID));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (fragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "EquipmentFragment", fragment);
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