package ru.shtrm.serviceman.mvp.addtrick;

import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.source.TricksRepository;
import ru.shtrm.serviceman.data.source.local.ImagesLocalDataSource;
import ru.shtrm.serviceman.data.source.local.TricksLocalDataSource;
import ru.shtrm.serviceman.data.source.local.UsersLocalDataSource;
import ru.shtrm.serviceman.data.source.remote.TricksRemoteDataSource;

public class AddTrickActivity extends AppCompatActivity {

    private AddTrickFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);

        // Set the navigation bar color
        if (PreferenceManager.getDefaultSharedPreferences(this).
                getBoolean("navigation_bar_tint", true)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setNavigationBarColor(
                        ContextCompat.getColor(this, R.color.colorPrimaryDark));
            }
        }

        if (savedInstanceState != null) {
            fragment = (AddTrickFragment) getSupportFragmentManager().
                    getFragment(savedInstanceState, "AddTrickFragment");
        } else {
            fragment = AddTrickFragment.newInstance();
        }

        if (!fragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.view_pager, fragment, "AddTrickFragment")
                    .commit();
        }

        // Create the presenter.
        new AddTrickPresenter(TricksRepository.getInstance(
                TricksRemoteDataSource.getInstance(),
                TricksLocalDataSource.getInstance()),
                UsersLocalDataSource.getInstance(),
                ImagesLocalDataSource.getInstance(),
                fragment);

    }

    // Save the fragment state to bundle.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "AddTrickFragment", fragment);
    }

}
