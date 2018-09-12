package ru.shtrm.serviceman.mvp.equipment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.util.MainUtil;

import static ru.shtrm.serviceman.mvp.equipment.EquipmentActivity.EQUIPMENT_UUID;

public class EditEquipmentActivity extends AppCompatActivity {
    private EditEquipmentFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);
        MainUtil.setToolBarColor(this, this);

        if (savedInstanceState != null) {
            fragment = (EditEquipmentFragment) getSupportFragmentManager().
                    getFragment(savedInstanceState, "EditEquipmentFragment");
        } else {
            fragment = EditEquipmentFragment.newInstance();
        }

        if (!fragment.isAdded()) {
            String equipment_id = getIntent().getStringExtra(EQUIPMENT_UUID);
            Bundle b = new Bundle();
            b.putString(EQUIPMENT_UUID, equipment_id);
            fragment.setArguments(b);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.view_pager, fragment, "EditEquipmentFragment")
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (fragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "EditEquipmentFragment", fragment);
        }
    }
}