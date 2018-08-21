package ru.shtrm.serviceman.mvp.questiondetails;

import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.source.AnswersRepository;
import ru.shtrm.serviceman.data.source.QuestionsRepository;
import ru.shtrm.serviceman.data.source.local.AnswersLocalDataSource;
import ru.shtrm.serviceman.data.source.local.QuestionsLocalDataSource;
import ru.shtrm.serviceman.data.source.remote.AnswersRemoteDataSource;
import ru.shtrm.serviceman.data.source.remote.QuestionsRemoteDataSource;

public class QuestionDetailsActivity extends AppCompatActivity{

    private QuestionDetailsFragment fragment;

    public static final String QUESTION_ID = "QUESTION_ID";

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
            fragment = (QuestionDetailsFragment) getSupportFragmentManager().
                    getFragment(savedInstanceState, "QuestionDetailsFragment");
        } else {
            fragment = QuestionDetailsFragment.newInstance();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.view_pager, fragment)
                .commit();

        // Create the presenter.
        new QuestionDetailsPresenter(
                getIntent().getStringExtra(QUESTION_ID),
                QuestionsRepository.getInstance(
                        QuestionsRemoteDataSource.getInstance(),
                        QuestionsLocalDataSource.getInstance()),
                AnswersRepository.getInstance(AnswersRemoteDataSource.getInstance(),
                        AnswersLocalDataSource.getInstance()),
                fragment);

    }

    // Save the fragment state to bundle.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "QuestionDetailsFragment", fragment);
    }

}
