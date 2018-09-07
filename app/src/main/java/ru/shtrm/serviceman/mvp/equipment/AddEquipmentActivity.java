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

import static ru.shtrm.serviceman.mvp.flat.FlatActivity.FLAT_UUID;
import static ru.shtrm.serviceman.mvp.flat.FlatActivity.HOUSE_UUID;

public class AddEquipmentActivity extends AppCompatActivity {
    private AddEquipmentFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);

        MainUtil.setToolBarColor(this, this);

        if (savedInstanceState != null) {
            fragment = (AddEquipmentFragment) getSupportFragmentManager().
                    getFragment(savedInstanceState, "AddEquipmentFragment");
        } else {
            fragment = AddEquipmentFragment.newInstance();
        }

        if (!fragment.isAdded()) {
            String house_id = getIntent().getStringExtra(HOUSE_UUID);
            String flat_id = getIntent().getStringExtra(FLAT_UUID);
            Bundle b = new Bundle();
            b.putString(HOUSE_UUID, house_id);
            b.putString(FLAT_UUID, flat_id);
            fragment.setArguments(b);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.view_pager, fragment, "AddEquipmentFragment")
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (fragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "AddEquipmentFragment", fragment);
        }
    }
}