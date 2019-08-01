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
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.Locale;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.Task;
import ru.shtrm.serviceman.data.UpdateQuery;
import ru.shtrm.serviceman.data.WorkStatus;
import ru.shtrm.serviceman.data.source.TaskRepository;
import ru.shtrm.serviceman.data.source.local.TaskLocalDataSource;
import ru.shtrm.serviceman.data.source.local.WorkStatusLocalDataSource;

import static ru.shtrm.serviceman.mvp.task.TaskInfoActivity.TASK_UUID;

public class TaskInfoFragment extends Fragment {
    private Activity mainActivityConnector = null;
    private TaskRepository taskRepository;
    private Task task;

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
        AppCompatTextView textAuthor = view.findViewById(R.id.textAuthor);
        AppCompatTextView textViewTaskTitle;
        AppCompatTextView textViewTaskAddress;
        AppCompatTextView textViewTaskEquipment;
        AppCompatTextView textTaskDate;
        AppCompatTextView textEndDate;
        AppCompatTextView textDeadlineDate;
        AppCompatTextView textComment;
        LinearLayout endLayout;

        Toolbar mToolbar = view.findViewById(R.id.toolbar);
        if (mToolbar !=null) {
            mToolbar.setTitle("Информация о задаче");
        }

        textViewTaskTitle = view.findViewById(R.id.textViewTaskTitle);
        textViewTaskEquipment = view.findViewById(R.id.textViewTaskEquipment);
        textViewTaskAddress = view.findViewById(R.id.textViewTaskAddress);
        textTaskDate = view.findViewById(R.id.textTaskDate);
        textEndDate = view.findViewById(R.id.textEndDate);
        textDeadlineDate = view.findViewById(R.id.textDeadlineDate);
        textComment = view.findViewById(R.id.textComment);
        endLayout = view.findViewById(R.id.endLayout);

        fab_cancel = view.findViewById(R.id.task_verdict);
        fab_complete = view.findViewById(R.id.task_complete);

        if (this.task!=null) {
            textViewTaskTitle.setText(task.getTaskTemplate().getTitle());
            textViewTaskAddress.setText(task.getEquipment().getObject().getFullTitle());
            textDeadlineDate.setText(task.getDeadlineDate().toString());
            textViewTaskEquipment.setText(task.getEquipment().getTitle());
            if (task.getTaskDate()!=null) {
                String sDate = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.US).format(task.getTaskDate());
                textTaskDate.setText(sDate);
            }
            else textTaskDate.setText("не задано");
            if (task.getDeadlineDate()!=null) {
                String sDate = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.US).format(task.getDeadlineDate());
                textDeadlineDate.setText(sDate);
            }
            else textDeadlineDate.setText("не задано");
            if (task.getEndDate()!=null) {
                String sDate = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.US).format(task.getEndDate());
                textEndDate.setText(sDate);
                endLayout.setVisibility(View.VISIBLE);
            }
            else endLayout.setVisibility(View.GONE);

            textAuthor.setText(task.getAuthor().getName());
            textComment.setText(task.getComment());
        }

        fab_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkStatus ws = WorkStatusLocalDataSource.getInstance().getWorkStatusByUuid(WorkStatus.Status.COMPLETE);
                TaskLocalDataSource.getInstance().setTaskStatus(task, ws);
                TaskLocalDataSource.getInstance().setEndDate(task);
                UpdateQuery query = new UpdateQuery();
                query.set_id(UpdateQuery.getLastId() + 1);
                query.setModelClass(Task.class.getSimpleName());
                query.setModelUuid(task.getUuid());
                query.setAttribute("workStatusUuid");
                query.setValue(task.getWorkStatus().getUuid());
                query.setChangedAt(task.getChangedAt());
                UpdateQuery.addToQuery(query);

                query.set_id(UpdateQuery.getLastId() + 1);
                query.setAttribute("endDate");
                query.setValue(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).format(task.getEndDate()));
                query.setChangedAt(task.getChangedAt());
                UpdateQuery.addToQuery(query);
                mainActivityConnector.onBackPressed();
            }
        });
        fab_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO set Verdict & status
                mainActivityConnector.onBackPressed();
            }
        });
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