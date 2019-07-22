package ru.shtrm.serviceman.mvp.task;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.Task;

public class TaskFragment extends Fragment implements TaskContract.View {
    private Activity mainActivityConnector = null;

    private TaskContract.Presenter presenter;
    private View view;
    private List<Task> tasks;

    public TaskFragment() {
    }

    public static TaskFragment newInstance() {
        return new TaskFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tasks, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.subscribe();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (presenter != null) {
            presenter.unsubscribe();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mainActivityConnector.onBackPressed();
        }

        return true;
    }

    @Override
    public void initViews(View view) {
        ListView listView = view.findViewById(R.id.list_view);
        if (this.tasks != null) {
            listView.setAdapter(null);
        }
    }

    @Override
    public void setPresenter(@NonNull TaskContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivityConnector = getActivity();
        // TODO решить что делать если контекст не приехал
        if (mainActivityConnector == null) {
            onDestroyView();
        }
    }

    @Override
    public void showTaskList(@NonNull List<Task> tasks) {
        this.tasks = tasks;
        initViews(view);
    }
}
