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
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.Locale;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.Request;
import ru.shtrm.serviceman.data.Task;
import ru.shtrm.serviceman.data.WorkStatus;
import ru.shtrm.serviceman.data.source.TaskRepository;
import ru.shtrm.serviceman.data.source.local.RequestLocalDataSource;
import ru.shtrm.serviceman.data.source.local.TaskLocalDataSource;
import ru.shtrm.serviceman.data.source.local.WorkStatusLocalDataSource;

import static ru.shtrm.serviceman.mvp.task.TaskInfoActivity.TASK_UUID;

public class TaskInfoFragment extends Fragment {
    private Activity mainActivityConnector = null;
    private TaskRepository taskRepository;
    private Task task;

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
        AppCompatTextView textContragent;
        AppCompatTextView textType;

        LinearLayout endLayout;
        LinearLayout typeLayout;
        LinearLayout contragentLayout;

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
        textContragent = view.findViewById(R.id.textContragent);
        textType = view.findViewById(R.id.textType);

        endLayout = view.findViewById(R.id.endLayout);
        typeLayout = view.findViewById(R.id.type);
        contragentLayout = view.findViewById(R.id.contragent);

        FloatingActionButton fab_cancel = view.findViewById(R.id.task_verdict);
        FloatingActionButton fab_complete = view.findViewById(R.id.task_complete);

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

            Request request = RequestLocalDataSource.getInstance().getRequestByTask(task.getUuid());
            if (request != null) {
                typeLayout.setVisibility(View.VISIBLE);
                contragentLayout.setVisibility(View.VISIBLE);
                textContragent.setText(request.getContragent().getTitle());
                textType.setText(request.getRequestType().getTitle());
            } else {
                typeLayout.setVisibility(View.GONE);
                contragentLayout.setVisibility(View.GONE);
            }

            textAuthor.setText(task.getAuthor().getName());
            textComment.setText(task.getComment());
            if (task.getWorkStatus().getUuid().equals(WorkStatus.Status.COMPLETE)) {
                fab_complete.setVisibility(View.GONE);
                fab_cancel.setVisibility(View.GONE);
            } else {
                fab_complete.setVisibility(View.VISIBLE);
                fab_cancel.setVisibility(View.VISIBLE);
            }
        }

        fab_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkStatus ws = WorkStatusLocalDataSource.getInstance().getWorkStatusByUuid(WorkStatus.Status.COMPLETE);
                TaskLocalDataSource.getInstance().setTaskStatus(task, ws);
                TaskLocalDataSource.getInstance().setEndDate(task);
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
