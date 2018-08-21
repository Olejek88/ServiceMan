package ru.shtrm.serviceman.mvp.trickdetails;

import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.source.TricksRepository;
import ru.shtrm.serviceman.data.source.local.TricksLocalDataSource;
import ru.shtrm.serviceman.data.source.remote.TricksRemoteDataSource;

public class TrickDetailsActivity extends AppCompatActivity{

    private TrickDetailsFragment fragment;

    public static final String Trick_ID = "Trick_ID";

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

        // Restore the status.
        if (savedInstanceState != null) {
            fragment = (TrickDetailsFragment) getSupportFragmentManager().
                    getFragment(savedInstanceState, "TrickDetailsFragment");
        } else {
            fragment = TrickDetailsFragment.newInstance();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.view_pager, fragment)
                .commit();

        // Create the presenter.
        new TrickDetailsPresenter(
                getIntent().getStringExtra(Trick_ID),
                TricksRepository.getInstance(
                        TricksRemoteDataSource.getInstance(),
                        TricksLocalDataSource.getInstance()),
                fragment);

    }

    // Save the fragment state to bundle.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "TrickDetailsFragment", fragment);
    }

}
