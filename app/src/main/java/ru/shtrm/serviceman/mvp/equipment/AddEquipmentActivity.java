package ru.shtrm.serviceman.mvp.equipment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.util.MainUtil;

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
            Bundle b = new Bundle();
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