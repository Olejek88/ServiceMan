package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import io.realm.Sort;
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.Operation;
import ru.shtrm.serviceman.data.Task;
import ru.shtrm.serviceman.data.WorkStatus;
import ru.shtrm.serviceman.data.source.TaskDataSource;

public class TaskLocalDataSource implements TaskDataSource {

    @Nullable
    private static TaskLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private TaskLocalDataSource() {

    }

    public static TaskLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TaskLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public List<Task> getTaskByEquipment(Equipment equipment, String status) {
        Realm realm = Realm.getDefaultInstance();
        List<Task> list = realm.where(Task.class)./*equalTo("equipment.uuid", equipment.getUuid()).*/
                equalTo("workStatus.uuid", status).
                findAllSorted("createdAt", Sort.ASCENDING);
        list = realm.copyFromRealm(list);
        realm.close();
        return list;
    }

    @Override
    public boolean checkAllOperationsComplete(Task task) {
        Realm realm = Realm.getDefaultInstance();
        long unCompleteOperations = realm.where(Operation.class).
                equalTo("task.uuid", task.getUuid()).
                equalTo("workStatus.uuid", WorkStatus.Status.UN_COMPLETE).
                count();
        realm.close();
        return unCompleteOperations <= 0;
    }

    @Override
    public void setTaskStatus(final Task task, final WorkStatus status) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                task.setWorkStatus(status);
                realm.copyToRealmOrUpdate(task);
            }
        });
        realm.close();
    }
}