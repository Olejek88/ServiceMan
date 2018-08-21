package ru.shtrm.serviceman.mvp.images;

import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.source.QuestionsRepository;
import ru.shtrm.serviceman.data.source.local.ImagesLocalDataSource;
import ru.shtrm.serviceman.data.source.local.QuestionsLocalDataSource;
import ru.shtrm.serviceman.data.source.remote.QuestionsRemoteDataSource;

public class ImagesActivity extends AppCompatActivity {

    private ImagesFragment fragment;
    public static final String IMAGE_ID = "IMAGE_ID";

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
            fragment = (ImagesFragment) getSupportFragmentManager().
                    getFragment(savedInstanceState, "ImagesFragment");
        } else {
            fragment = ImagesFragment.newInstance();
        }

        if (!fragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.view_pager, fragment, "ImagesFragment")
                    .commit();
        }

        // Create the presenter.
        new ImagesPresenter(QuestionsRepository.getInstance(
                QuestionsRemoteDataSource.getInstance(),
                QuestionsLocalDataSource.getInstance()),
                ImagesLocalDataSource.getInstance(),
                fragment);

    }

    // Save the fragment state to bundle.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "ImagesFragment", fragment);
    }

}
