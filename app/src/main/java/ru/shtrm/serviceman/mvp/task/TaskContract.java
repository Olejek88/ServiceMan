package ru.shtrm.serviceman.mvp.task;

import android.support.annotation.NonNull;

import java.util.List;

import ru.shtrm.serviceman.data.Task;
import ru.shtrm.serviceman.mvp.BasePresenter;
import ru.shtrm.serviceman.mvp.BaseView;

public interface TaskContract {

    interface View extends BaseView<TaskContract.Presenter> {
        void showEmptyView(boolean toShow);
        void showTaskList(@NonNull List<Task> tasks);
    }

    interface Presenter extends BasePresenter {
        List<Task> loadTasks();
    }
}
