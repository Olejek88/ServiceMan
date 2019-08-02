package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import ru.shtrm.serviceman.data.TaskType;
import ru.shtrm.serviceman.data.TaskVerdict;
import ru.shtrm.serviceman.data.source.TaskTypeDataSource;
import ru.shtrm.serviceman.data.source.TaskVerdictDataSource;


public class TaskVerdictLocalDataSource implements TaskVerdictDataSource {

    @Nullable
    private static TaskVerdictLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private TaskVerdictLocalDataSource() {

    }

    public static TaskVerdictLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TaskVerdictLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public List<TaskVerdict> getTaskVerdicts() {
        Realm realm = Realm.getDefaultInstance();
        List<TaskVerdict> list = realm.copyFromRealm(
                realm.where(TaskVerdict.class).findAllSorted("title"));
        realm.close();
        return list;
    }

    @Override
    public TaskVerdict getTaskVerdict(String uuid) {
        Realm realm = Realm.getDefaultInstance();
        TaskVerdict list = realm.where(TaskVerdict.class).equalTo("uuid", uuid).findFirst();
        if (list != null) {
            list = realm.copyFromRealm(list);
        }
        realm.close();
        return list;
    }
}