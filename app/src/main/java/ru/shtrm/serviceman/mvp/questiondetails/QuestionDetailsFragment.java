package ru.shtrm.serviceman.mvp.questiondetails;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.List;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.Answer;
import ru.shtrm.serviceman.data.Question;
import ru.shtrm.serviceman.data.source.AnswersRepository;
import ru.shtrm.serviceman.data.source.QuestionsRepository;
import ru.shtrm.serviceman.mvp.addanswer.AddAnswerActivity;
import ru.shtrm.serviceman.mvp.questionedit.QuestionEditActivity;
import ru.shtrm.serviceman.util.MainUtil;

public class QuestionDetailsFragment extends Fragment
        implements QuestionDetailsContract.View {
    private Activity mainActivityConnector = null;

    private RecyclerView recyclerView;
    private RecyclerView recyclerViewAnswer;
    private View view;
    private AppCompatTextView userName;
    private AppCompatTextView userStatus;
    private AppCompatTextView userStats;
    private ImageView imageView;

    private boolean FAB_Status = false;
    private Question currentQuestion;

    private FloatingActionButton fab;
    private FloatingActionButton fab_edit_text;
    private FloatingActionButton fab_edit;
    private FloatingActionButton fab_delete;
    private FloatingActionButton fab_answer;

    private QuestionDetailsAdapter adapter;
    private AnswersAdapter answerAdapter;

    private QuestionDetailsContract.Presenter presenter;

    public QuestionDetailsFragment() {}

    public static QuestionDetailsFragment newInstance() {
        return new QuestionDetailsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_question_details, container, false);

        initViews(view);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!FAB_Status) {
                    expandFAB();
                    FAB_Status = true;
                } else {
                    hideFAB();
                    FAB_Status = false;
                }
            }
        });

        fab_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditTextDialog();
            }
        });

        fab_edit_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), QuestionEditActivity.class);
                intent.putExtra(QuestionEditActivity.QUESTION_ID, currentQuestion.getId());
                startActivity(intent);
            }
        });

        fab_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.deleteQuestion();
                mainActivityConnector.finish();
            }
        });

        fab_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddAnswerActivity.class);
                intent.putExtra(QuestionEditActivity.QUESTION_ID, currentQuestion.getId());
                startActivity(intent);
            }
        });

        setHasOptionsMenu(true);

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.question_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            exit();

        } else if (id == R.id.action_delete) {
            showDeleteAlertDialog();
        }
        return true;
    }

    /**
     * Init views.
     * @param view The root view of fragment.
     */
    @Override
    public void initViews(View view) {
        QuestionDetailsActivity activity = (QuestionDetailsActivity) mainActivityConnector;
        activity.setSupportActionBar((Toolbar) view.findViewById(R.id.toolbar));
        if (activity.getSupportActionBar()!=null)
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerViewAnswer = view.findViewById(R.id.recyclerViewAnswers);


        fab = view.findViewById(R.id.fab);
        fab_edit_text = view.findViewById(R.id.fab_edit_text);
        fab_edit = view.findViewById(R.id.fab_edit);
        fab_delete = view.findViewById(R.id.fab_delete);
        fab_answer = view.findViewById(R.id.fab_answer);

        /*
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(mainActivityConnector, R.color.colorPrimary));
*/

        RelativeLayout user_info = view.findViewById(R.id.user_detail);
        userName = user_info.findViewById(R.id.profile_name);
        userStatus = user_info.findViewById(R.id.profile_status);
        userStats = user_info.findViewById(R.id.profile_stats);
        imageView = user_info.findViewById(R.id.profile_image);

    }

    /**
     * Bind the presenter to view.
     * @param presenter The presenter. See at {@link QuestionDetailsPresenter}
     */
    @Override
    public void setPresenter(@NonNull QuestionDetailsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    /**
     * When the network is slow or is not connected, show this message.
     */
    @Override
    public void showNetworkError() {
        Snackbar.make(fab, R.string.network_error, Snackbar.LENGTH_SHORT)
                .setAction(R.string.go_to_settings, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent().setAction(Settings.ACTION_SETTINGS));
                    }
                })
                .show();
    }

    /**
     * Show the question details.
     * @param question The question. See at {@link Question}
     */
    @Override
    public void showQuestionDetails(@NonNull Question question) {
        currentQuestion = question;
        if (adapter == null) {
            adapter = new QuestionDetailsAdapter(mainActivityConnector, question, presenter);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateData(question.getAnswers());
        }
        if (question.getUser()!=null) {
            userStatus.setText(R.string.user_master);
            userStats.setText(question.getUser().getStats());
            userName.setText(question.getUser().getName());

            String path = MainUtil.getPicturesDirectory(mainActivityConnector.getApplicationContext());
            if (path != null) {
                String avatar = question.getUser().getAvatar();
                if (avatar != null)
                    imageView.setImageBitmap(MainUtil.getBitmapByPath(path, avatar));
            }
        }
        showAnswers(currentQuestion.getAnswers());
    }

    /**
     * Finish the activity.
     */
    @Override
    public void exit() {
        mainActivityConnector.onBackPressed();
    }

    /**
     * Show a dialog when user select the DELETE option menu item.
     */
    private void showDeleteAlertDialog() {
        AlertDialog dialog = new AlertDialog.Builder(mainActivityConnector).create();
        dialog.setTitle(R.string.delete);
        dialog.setMessage(getString(R.string.delete_question_message));
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setButton(DialogInterface.BUTTON_POSITIVE,
                getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                presenter.deleteQuestion();
            }
        });
        dialog.show();
    }

    /**
     * Show the dialog which contains an EditText.
     */
    private void showEditTextDialog() {
        AlertDialog dialog = new AlertDialog.Builder(mainActivityConnector).create();
        dialog.setTitle(getString(R.string.edit_name));

        View view = mainActivityConnector.getLayoutInflater().
                inflate(R.layout.dialog_edit_question_title, null);
        final AppCompatEditText editText = view.findViewById(R.id.editTextName);
        editText.setText(presenter.getQuestionTitle());
        dialog.setView(view);

        dialog.setButton(DialogInterface.BUTTON_POSITIVE,
                getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String input = editText.getText().toString();
                if (input.isEmpty()) {
                    showInputIsEmpty();
                } else {
                    presenter.updateQuestionTitle(input);
                }
                dialog.dismiss();
            }
        });

        dialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /**
     * Show the message that the user's input is empty.
     */
    private void showInputIsEmpty() {
        Snackbar.make(fab, R.string.input_empty, Snackbar.LENGTH_SHORT).show();
    }

    private void expandFAB() {
        showFloatingActionButton(R.id.fab_edit_text, R.anim.fab1_show, 0, 3.0);
        showFloatingActionButton(R.id.fab_edit, R.anim.fab2_show, 0, 2.0);
        showFloatingActionButton(R.id.fab_delete, R.anim.fab3_show, 0, 1.0);
        showFloatingActionButton(R.id.fab_answer, R.anim.fab4_show, 0.0, 0.0);
    }

    private void showFloatingActionButton(int buttonId, int animationId, double kw, double kh) {
        FloatingActionButton fab = view.findViewById(buttonId);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) fab.getLayoutParams();
        lp.rightMargin += (int) (fab.getWidth() * kw);
        lp.bottomMargin += (int) (fab.getHeight() * kh);
        fab.setLayoutParams(lp);
        Animation animation = AnimationUtils.loadAnimation(mainActivityConnector, animationId);
        fab.startAnimation(animation);
        fab.setClickable(true);
    }

    private void hideFAB() {
        hideFloatingActionButton(R.id.fab_edit_text, R.anim.fab1_hide,  3.0);
        hideFloatingActionButton(R.id.fab_edit, R.anim.fab2_hide,  2.0);
        hideFloatingActionButton(R.id.fab_delete, R.anim.fab3_hide,  1.0);
        hideFloatingActionButton(R.id.fab_answer, R.anim.fab4_hide, 0.0);
    }

    private void hideFloatingActionButton(int buttonId, int animationId,  double kh) {
        FloatingActionButton fab = view.findViewById(buttonId);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) fab.getLayoutParams();
        lp.rightMargin -= 0;
        lp.bottomMargin -= (int) (fab.getHeight() * kh);
        fab.setLayoutParams(lp);
        Animation animation = AnimationUtils.loadAnimation(mainActivityConnector, animationId);
        fab.startAnimation(animation);
        fab.setClickable(false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivityConnector = getActivity();
        // TODO решить что делать если контекст не приехал
        if (mainActivityConnector==null)
            onDestroyView();
        presenter.refreshQuestion();
    }

    public void showAnswers(final List<Answer> list) {
        if (answerAdapter == null) {
            answerAdapter = new AnswersAdapter(mainActivityConnector, list, presenter);
            recyclerViewAnswer.setAdapter(adapter);
        }
    }
 }
