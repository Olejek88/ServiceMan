package ru.shtrm.serviceman.mvp.addquestion;

import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.source.QuestionsRepository;
import ru.shtrm.serviceman.data.source.UsersRepository;
import ru.shtrm.serviceman.data.source.local.ImagesLocalDataSource;
import ru.shtrm.serviceman.data.source.local.QuestionsLocalDataSource;
import ru.shtrm.serviceman.data.source.local.UsersLocalDataSource;
import ru.shtrm.serviceman.data.source.remote.QuestionsRemoteDataSource;

public class AddQuestionActivity extends AppCompatActivity {

    private AddQuestionFragment fragment;

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
            fragment = (AddQuestionFragment) getSupportFragmentManager().
                    getFragment(savedInstanceState, "AddQuestionFragment");
        } else {
            fragment = AddQuestionFragment.newInstance();
        }

        if (!fragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.view_pager, fragment, "AddQuestionFragment")
                    .commit();
        }

        // Create the presenter.
        new AddQuestionPresenter(QuestionsRepository.getInstance(
                QuestionsRemoteDataSource.getInstance(),
                QuestionsLocalDataSource.getInstance()),
                UsersRepository.getInstance(UsersLocalDataSource.getInstance()),
                ImagesLocalDataSource.getInstance(),
                fragment);

    }

    // Save the fragment state to bundle.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "AddQuestionFragment", fragment);
    }

}
