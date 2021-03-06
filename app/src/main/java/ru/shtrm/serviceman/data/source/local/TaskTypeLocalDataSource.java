package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import ru.shtrm.serviceman.data.Task;
import ru.shtrm.serviceman.data.TaskType;
import ru.shtrm.serviceman.data.source.TaskTypeDataSource;


public class TaskTypeLocalDataSource implements TaskTypeDataSource {

    @Nullable
    private static TaskTypeLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private TaskTypeLocalDataSource() {

    }

    public static TaskTypeLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TaskTypeLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public List<TaskType> getTaskTypes() {
        Realm realm = Realm.getDefaultInstance();
        List<TaskType> list = realm.copyFromRealm(
                realm.where(TaskType.class).findAllSorted("title"));
        realm.close();
        return list;
    }

    @Override
    public TaskType getTaskType(String uuid) {
        Realm realm = Realm.getDefaultInstance();
        TaskType list = realm.where(TaskType.class).equalTo("uuid", uuid).findFirst();
        if (list != null) {
            list = realm.copyFromRealm(list);
        }

        realm.close();
        return list;
    }
}