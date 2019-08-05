package ru.shtrm.serviceman.mvp.task;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.Defect;
import ru.shtrm.serviceman.data.Documentation;
import ru.shtrm.serviceman.data.Request;
import ru.shtrm.serviceman.data.Task;
import ru.shtrm.serviceman.data.TaskVerdict;
import ru.shtrm.serviceman.data.UpdateQuery;
import ru.shtrm.serviceman.data.WorkStatus;
import ru.shtrm.serviceman.data.source.TaskRepository;
import ru.shtrm.serviceman.data.source.TaskVerdictRepository;
import ru.shtrm.serviceman.data.source.local.DocumentationLocalDataSource;
import ru.shtrm.serviceman.data.source.local.RequestLocalDataSource;
import ru.shtrm.serviceman.data.source.local.TaskLocalDataSource;
import ru.shtrm.serviceman.data.source.local.TaskVerdictLocalDataSource;
import ru.shtrm.serviceman.data.source.local.WorkStatusLocalDataSource;
import ru.shtrm.serviceman.mvp.equipment.EquipmentFragment;

import static ru.shtrm.serviceman.mvp.task.TaskInfoActivity.TASK_UUID;

public class TaskInfoFragment extends Fragment {
    private Activity mainActivityConnector = null;
    private TaskRepository taskRepository;
    private Task task;

    public TaskInfoFragment() {}

    private Documentation documentation;

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
        if (getFragmentManager() != null)
            getFragmentManager().popBackStack();

        if (task == null) {
            if (getFragmentManager() != null) {
                getFragmentManager().popBackStack();
            }
        }

        Bundle b = getArguments();
        if (b != null) {
            String taskUuid = b.getString(TASK_UUID);
            if (taskUuid != null)
                task = TaskLocalDataSource.getInstance().getTask(taskUuid);
            if (task != null) {
                initViews(view, container);
                setHasOptionsMenu(true);
                return view;
            }
        }
        initViews(view, container);

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

    public void initViews(View view, final ViewGroup container) {
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
        AppCompatTextView textStatus;
        final AppCompatTextView documentation_text = view.findViewById(R.id.documentation);

        LinearLayout endLayout;
        LinearLayout typeLayout;
        LinearLayout contragentLayout;
        LinearLayout docs = view.findViewById(R.id.docs);

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
        textStatus = view.findViewById(R.id.textStatus);

        endLayout = view.findViewById(R.id.endLayout);
        typeLayout = view.findViewById(R.id.type);
        contragentLayout = view.findViewById(R.id.contragent);

        FloatingActionButton fab_cancel = view.findViewById(R.id.task_verdict);
        FloatingActionButton fab_complete = view.findViewById(R.id.task_complete);
        FloatingActionButton defectButton = view.findViewById(R.id.add_new_defect);

        if (this.task!=null) {
            TaskLocalDataSource.getInstance().setTaskStatus(task, task.getWorkStatus());

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
                textContragent.setText(request.getContragent().getTitle().concat(" [").concat(request.getContragent().getPhone()).concat("]"));
                textType.setText(request.getRequestType().getTitle());
            } else {
                typeLayout.setVisibility(View.GONE);
                contragentLayout.setVisibility(View.GONE);
            }

            textAuthor.setText(task.getAuthor().getName());
            textComment.setText(task.getComment());
            if (task.getWorkStatus().getUuid().equals(WorkStatus.Status.COMPLETE) ||
                    task.getWorkStatus().getUuid().equals(WorkStatus.Status.UN_COMPLETE)) {
                fab_complete.setVisibility(View.GONE);
                fab_cancel.setVisibility(View.GONE);
            } else {
                fab_complete.setVisibility(View.VISIBLE);
                fab_cancel.setVisibility(View.VISIBLE);
            }

            textStatus.setText(task.getWorkStatus().getTitle());

            documentation = DocumentationLocalDataSource.getInstance().getDocumentationByEquipment(task.getEquipment().getUuid());

            if (documentation != null) {
                docs.setVisibility(View.VISIBLE);
                documentation_text.setText(documentation.getTitle());
                documentation_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final File file = new File(mainActivityConnector.getExternalFilesDir(documentation.getPath()),
                                documentation.getPath());
                        if (file.exists()) {
                            Intent intent = EquipmentFragment.showDocument(file, mainActivityConnector.getApplicationContext());
                            if (intent != null) {
                                startActivity(intent);
                            }
                        }
                    }
                });
            } else {
                docs.setVisibility(View.GONE);
            }

            defectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Defect.showDialogNewDefect(getContext(), getLayoutInflater(), container, task.getEquipment());
                }
            });
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

                query.set_id(UpdateQuery.getLastId() + 1);
                query.setAttribute("startDate");
                query.setValue(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).format(task.getStartDate()));
                query.setChangedAt(task.getChangedAt());
                UpdateQuery.addToQuery(query);

                mainActivityConnector.onBackPressed();
            }
        });

        fab_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogNewVerdict(getContext(), getLayoutInflater(), container, task);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivityConnector = getActivity();
        // TODO решить что делать если контекст не приехал
        if (mainActivityConnector == null)
            onDestroyView();
        checkRepository();
    }

    void checkRepository() {
        if (taskRepository == null)
            taskRepository = TaskRepository.getInstance
                    (TaskLocalDataSource.getInstance());
    }

    public void showDialogNewVerdict(final Context context, LayoutInflater inflater, ViewGroup parent, final Task task) {
        View addVerdictLayout;
        final Spinner verdictSpinner;
        TaskVerdictLocalDataSource taskVerdictLocalDataSource = TaskVerdictLocalDataSource.getInstance();
        TaskVerdictRepository taskVerdictRepository = TaskVerdictRepository.getInstance(taskVerdictLocalDataSource);
        final TaskVerdictListAdapter taskVerdictAdapter = new TaskVerdictListAdapter(mainActivityConnector,
                R.layout.simple_spinner_item, taskVerdictRepository.getTaskVerdicts(), R.color.colorPrimaryDark);

        addVerdictLayout = inflater.inflate(R.layout.add_task_verdict_dialog, parent, false);
        verdictSpinner = addVerdictLayout.findViewById(R.id.spinner_task_verdict);
        verdictSpinner.setAdapter(taskVerdictAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Укажите вердикт");
        builder.setView(addVerdictLayout);
        builder.setIcon(R.drawable.baseline_check_box_black_48dp);
//        builder.setCancelable(false);
        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TaskVerdict currentTaskVerdict = null;
                int position = verdictSpinner.getSelectedItemPosition();
                if (position != AdapterView.INVALID_POSITION) {
                    currentTaskVerdict = (TaskVerdict) verdictSpinner.getAdapter().getItem(position);
                }
                if (currentTaskVerdict != null) {
                    task.setTaskVerdict(currentTaskVerdict);
                    // отправляем в очередь на отправку
                    UpdateQuery query = new UpdateQuery();
                    query.setModelClass(Task.class.getSimpleName());
                    query.setModelUuid(task.getUuid());
                    query.set_id(UpdateQuery.getLastId() + 1);
                    query.setAttribute("taskVerdictUuid");
                    query.setValue(currentTaskVerdict.getUuid());
                    query.setChangedAt(task.getChangedAt());
                    UpdateQuery.addToQuery(query);

                    WorkStatus ws = WorkStatusLocalDataSource.getInstance().getWorkStatusByUuid(WorkStatus.Status.UN_COMPLETE);
                    TaskLocalDataSource.getInstance().setTaskStatus(task, ws);
                    TaskLocalDataSource.getInstance().setEndDate(task);

                    query.set_id(UpdateQuery.getLastId() + 1);
                    query.setAttribute("workStatusUuid");
                    query.setValue(task.getWorkStatus().getUuid());
                    query.setChangedAt(task.getChangedAt());
                    UpdateQuery.addToQuery(query);

                    query.set_id(UpdateQuery.getLastId() + 1);
                    query.setAttribute("endDate");
                    query.setValue(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).format(task.getEndDate()));
                    query.setChangedAt(task.getChangedAt());
                    UpdateQuery.addToQuery(query);
                    dialog.dismiss();
                    mainActivityConnector.onBackPressed();
                } else
                    dialog.dismiss();
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
        dialog.show();
    }
}
