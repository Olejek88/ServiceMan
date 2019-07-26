package ru.shtrm.serviceman.mvp.task;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import java.util.Calendar;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.Task;
import ru.shtrm.serviceman.data.source.TaskRepository;
import ru.shtrm.serviceman.data.source.local.ObjectLocalDataSource;
import ru.shtrm.serviceman.data.source.local.TaskLocalDataSource;

import static ru.shtrm.serviceman.mvp.task.TaskInfoActivity.TASK_UUID;

public class TaskInfoFragment extends Fragment {
    Calendar myCalendar;
    private Activity mainActivityConnector = null;
    private TaskRepository taskRepository;
    private Task task;
    private AppCompatTextView textViewTaskTitle;
    private AppCompatTextView textViewTaskAddress;
    private AppCompatTextView textTaskDate;
    private AppCompatTextView textDeadlineDate;
    private AppCompatTextView textAuthor;
    private AppCompatTextView textComment;

    private FloatingActionButton fab_complete;
    private FloatingActionButton fab_cancel;

    public TaskInfoFragment() {}

    public static TaskInfoFragment newInstance() {
        return new TaskInfoFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_info, container, false);
        Bundle b = getArguments();
        if (b != null) {
            String taskUuid = b.getString(TASK_UUID);
            if (taskUuid != null)
                task = TaskLocalDataSource.getInstance().getTask(taskUuid);
            if (task != null) {
                initViews(view);
                setHasOptionsMenu(true);
                return view;
            }
        }
        if (getFragmentManager() != null)
            getFragmentManager().popBackStack();

        if (task == null) {
            if (getFragmentManager() != null) {
                getFragmentManager().popBackStack();
            }
        }

        initViews(view);
        fab_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        fab_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        checkRepository();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void initViews(View view) {
        Toolbar mToolbar = view.findViewById(R.id.toolbar);
        if (mToolbar !=null) {
            mToolbar.setTitle("Информация о задаче");
        }

        textViewTaskTitle = view.findViewById(R.id.textViewTaskTitle);
        textViewTaskAddress = view.findViewById(R.id.textViewTaskAddress);
        textTaskDate = view.findViewById(R.id.textTaskDate);
        textDeadlineDate = view.findViewById(R.id.textDeadlineDate);
        textAuthor = view.findViewById(R.id.textAuthor);
        textComment = view.findViewById(R.id.textComment);

        fab_cancel = view.findViewById(R.id.task_complete);
        fab_complete = view.findViewById(R.id.task_verdict);

        if (this.task!=null) {
            textViewTaskTitle.setText(task.getTaskTemplate().getTitle());
            textViewTaskAddress.setText(task.getEquipment().getObject().getFullTitle());
            textTaskDate.setText(task.getTaskDate().toString());
            textDeadlineDate.setText(task.getDeadlineDate().toString());
            textAuthor.setText(task.getAuthor().getName());
            textComment.setText(task.getComment());
        }
    }

    /**
     * Hide the input method like soft keyboard, etc... when they are active.
     */
    private void hideImm() {
        InputMethodManager imm = (InputMethodManager)
                mainActivityConnector.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm!=null && imm.isActive()) {
            //imm.hideSoftInputFromWindow(fab.getWindowToken(), 0);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivityConnector = getActivity();
        // TODO решить что делать если контекст не приехал
        if (mainActivityConnector==null)
            onDestroyView();
        checkRepository();
    }

    void checkRepository() {
        if (taskRepository == null)
            taskRepository = TaskRepository.getInstance
                    (TaskLocalDataSource.getInstance());
    }
}
