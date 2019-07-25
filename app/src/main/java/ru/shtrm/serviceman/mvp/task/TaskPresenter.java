package ru.shtrm.serviceman.mvp.task;

import android.support.annotation.NonNull;

import java.util.List;

import ru.shtrm.serviceman.data.Task;
import ru.shtrm.serviceman.data.source.TaskRepository;

public class TaskPresenter implements TaskContract.Presenter {

    @NonNull
    private final TaskContract.View view;

    @NonNull
    private final TaskRepository TaskRepository;

    public TaskPresenter(@NonNull TaskContract.View view,
                         @NonNull TaskRepository TaskRepository) {
        this.view = view;
        this.TaskRepository = TaskRepository;
        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {
        loadTasks();
    }

    @Override
    public void unsubscribe() {
    }

    @Override
    public List<Task> loadTasks() {
        List<Task> tasks = TaskRepository.getNewTasks();
        view.showTaskList(tasks);
        return tasks;
    }
}
