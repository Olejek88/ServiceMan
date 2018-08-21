package ru.shtrm.serviceman.mvp.questionedit;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.Question;
import ru.shtrm.serviceman.data.source.QuestionsRepository;
import ru.shtrm.serviceman.data.source.local.QuestionsLocalDataSource;
import ru.shtrm.serviceman.mvp.addanswer.AddAnswerActivity;
import ru.shtrm.serviceman.mvp.images.ImageGridAdapter;

public class QuestionEditFragment extends Fragment
        implements QuestionEditContract.View {
    private Activity mainActivityConnector = null;

    private RecyclerView recyclerView;
    private Question currentQuestion;

    private FloatingActionButton fab;

    private QuestionEditContract.Presenter presenter;
    private QuestionsLocalDataSource questionsLocalDataSource;

    private EditText textViewText;
    private EditText textViewTitle;
    private GridView photoGridView;

    public QuestionEditFragment() {}

    public static QuestionEditFragment newInstance() {
        return new QuestionEditFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        questionsLocalDataSource = QuestionsLocalDataSource.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_edit, container, false);
        Bundle b = getArguments();
        if (b!=null) {
            String questionId = b.getString(QuestionEditActivity.QUESTION_ID);
            if (questionId!=null)
                currentQuestion = questionsLocalDataSource.getQuestionById(questionId);
        }

        if (currentQuestion==null) {
            mainActivityConnector.finish();
            return view;
        }

        initViews(view);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textViewTitle.getText().toString().length()<5 ||
                        textViewText.getText().toString().length()<5) {
                    showInputIsEmpty();
                }
                else {
                    presenter.saveQuestion(currentQuestion.getId(),
                            textViewTitle.getText().toString(),
                            textViewText.getText().toString(),
                            currentQuestion.getDate(),
                            currentQuestion.isClosed(),
                            currentQuestion.getImages(),
                            currentQuestion.getAnswers(),
                            currentQuestion.getUser()
                    );
                }
                mainActivityConnector.finish();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unsubscribe();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        QuestionsRepository.destroyInstance();
    }

    /**
     * Init views.
     * @param view The root view of fragment.
     */
    @Override
    public void initViews(View view) {
        QuestionEditActivity activity = (QuestionEditActivity)mainActivityConnector;
        activity.setSupportActionBar((Toolbar) view.findViewById(R.id.toolbar));
        if (activity.getSupportActionBar()!=null)
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fab = view.findViewById(R.id.fab);

        textViewText = view.findViewById(R.id.questionText);
        textViewTitle = view.findViewById(R.id.questionTitle);
        photoGridView = view.findViewById(R.id.gridview);

        textViewTitle.setText(currentQuestion.getTitle());
        textViewText.setText(currentQuestion.getText());
        photoGridView.setAdapter(new ImageGridAdapter(mainActivityConnector, currentQuestion.getImages()));
        photoGridView.invalidateViews();

    }

    /**
     * Bind the presenter to view.
     * @param presenter The presenter. See at {@link QuestionEditPresenter}
     */
    @Override
    public void setPresenter(@NonNull QuestionEditContract.Presenter presenter) {
        this.presenter = presenter;
    }

    /**
     * Show the question details.
     * @param question The question. See at {@link Question}
     */
    @Override
    public void showQuestionEdit(@NonNull Question question) {
        currentQuestion = question;

        textViewTitle.setText(currentQuestion.getTitle());
        textViewText.setText(currentQuestion.getText());
        photoGridView.setAdapter(new ImageGridAdapter(mainActivityConnector, currentQuestion.getImages()));
        photoGridView.invalidateViews();
    }

    /**
     * Finish the activity.
     */
    @Override
    public void exit() {
        mainActivityConnector.onBackPressed();
    }

    /**
     * Show the message that the user's input is empty.
     */
    private void showInputIsEmpty() {
        Snackbar.make(fab, R.string.input_empty, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorMsg() {
        Snackbar.make(fab, R.string.something_wrong, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * Finish current activity.
     */
    @Override
    public void showQuestion() {
        mainActivityConnector.setResult(Activity.RESULT_OK);
        mainActivityConnector.finish();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivityConnector = getActivity();
        // TODO решить что делать если контекст не приехал
        if (mainActivityConnector==null)
            onDestroyView();
    }
}
