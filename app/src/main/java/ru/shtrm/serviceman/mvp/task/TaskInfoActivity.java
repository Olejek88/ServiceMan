package ru.shtrm.serviceman.mvp.task;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.mvp.equipment.AddEquipmentFragment;
import ru.shtrm.serviceman.util.MainUtil;

public class TaskInfoActivity extends AppCompatActivity {
    private TaskInfoFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);

        MainUtil.setToolBarColor(this, this);

        if (savedInstanceState != null) {
            fragment = (TaskInfoFragment) getSupportFragmentManager().
                    getFragment(savedInstanceState, "TaskInfoFragment");
        } else {
            fragment = TaskInfoFragment.newInstance();
        }

        if (!fragment.isAdded()) {
            Bundle b = new Bundle();
            fragment.setArguments(b);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.view_pager, fragment, "TaskInfoFragment")
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (fragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "TaskInfoFragment", fragment);
        }
    }
}