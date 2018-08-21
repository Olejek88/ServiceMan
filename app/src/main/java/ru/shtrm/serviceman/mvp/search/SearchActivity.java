package ru.shtrm.serviceman.mvp.search;

import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.source.QuestionsRepository;
import ru.shtrm.serviceman.data.source.UsersRepository;
import ru.shtrm.serviceman.data.source.local.QuestionsLocalDataSource;
import ru.shtrm.serviceman.data.source.local.UsersLocalDataSource;
import ru.shtrm.serviceman.data.source.remote.QuestionsRemoteDataSource;

public class SearchActivity extends AppCompatActivity {

    private SearchFragment fragment;

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
            fragment = (SearchFragment) getSupportFragmentManager().
                    getFragment(savedInstanceState, "SearchFragment");
        } else {
            fragment = SearchFragment.newInstance();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.view_pager, fragment)
                .commit();

        new SearchPresenter(fragment,
                QuestionsRepository.getInstance(QuestionsRemoteDataSource.getInstance(),
                        QuestionsLocalDataSource.getInstance()),
                UsersRepository.getInstance(UsersLocalDataSource.getInstance()));

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "SearchFragment", fragment);
    }
}
