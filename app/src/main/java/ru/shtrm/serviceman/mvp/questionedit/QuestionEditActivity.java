package ru.shtrm.serviceman.mvp.questionedit;

import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.source.QuestionsRepository;
import ru.shtrm.serviceman.data.source.local.QuestionsLocalDataSource;
import ru.shtrm.serviceman.data.source.remote.QuestionsRemoteDataSource;
import ru.shtrm.serviceman.mvp.addanswer.AddAnswerFragment;

public class QuestionEditActivity extends AppCompatActivity{

    private QuestionEditFragment fragment;

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
            fragment = (QuestionEditFragment) getSupportFragmentManager().
                    getFragment(savedInstanceState, "QuestionEditFragment");
        } else {
            fragment = QuestionEditFragment.newInstance();
        }

        if (!fragment.isAdded()) {
            Bundle b = new Bundle();
            b.putString(QUESTION_ID,getIntent().getStringExtra(QUESTION_ID));
            fragment.setArguments(b);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.view_pager, fragment, "QuestionEditFragment")
                    .commit();
        }

        // Create the presenter.
        new QuestionEditPresenter(
                getIntent().getStringExtra(QUESTION_ID),
                QuestionsRepository.getInstance(
                        QuestionsRemoteDataSource.getInstance(),
                        QuestionsLocalDataSource.getInstance()),
                fragment);

    }

    // Save the fragment state to bundle.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "QuestionEditFragment", fragment);
    }

}
