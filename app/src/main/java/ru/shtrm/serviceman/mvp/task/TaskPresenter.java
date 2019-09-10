package ru.shtrm.serviceman.mvp.task;

import android.support.annotation.NonNull;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
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

        // просто тупо реагируем на все изменения в базе realm один раз
        // при удалении MapPresenter необходимо удалять RealmChangeListener!
        Realm realm = Realm.getDefaultInstance();
        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm realm) {
                TaskPresenter.this.loadTasks();
            }
        });
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
        List<Task> tasks = TaskRepository.getTasks();
        view.showTaskList(tasks);
        return tasks;
    }
}
