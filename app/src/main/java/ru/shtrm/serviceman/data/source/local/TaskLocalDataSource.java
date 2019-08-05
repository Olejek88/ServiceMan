package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.Nullable;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.Sort;
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.Operation;
import ru.shtrm.serviceman.data.Task;
import ru.shtrm.serviceman.data.TaskVerdict;
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
    public Task getTask(String uuid) {
        Realm realm = Realm.getDefaultInstance();
        Task task = realm.where(Task.class).equalTo("uuid", uuid).findFirst();
        if (task!=null) {
            task = realm.copyFromRealm(task);
            realm.close();
            return task;
        }
        else {
            return null;
        }
    }

    @Override
    public List<Task> getTaskByEquipment(Equipment equipment, String  status) {
        Realm realm = Realm.getDefaultInstance();
        if (status != null) {
            List<Task> tasks = realm.copyFromRealm(
                    realm.where(Task.class).
                            equalTo("equipment.uuid", equipment.getUuid()).
                            equalTo("workStatus.uuid", status).
                            findAllSorted("createdAt", Sort.ASCENDING));
            realm.close();
            return tasks;
        } else {
            List<Task> tasks = realm.copyFromRealm(
                    realm.where(Task.class).
                            equalTo("equipment.uuid", equipment.getUuid()).
                            findAllSorted("createdAt", Sort.ASCENDING));
            realm.close();
            return tasks;
        }
    }

    @Override
    public List<Task> getNewTasks() {
        Realm realm = Realm.getDefaultInstance();
        List<Task> tasks = realm.copyFromRealm(
                realm.where(Task.class)./*equalTo("equipment.uuid", equipment.getUuid()).*/
                        equalTo("workStatus.uuid", WorkStatus.Status.NEW).
                        or().
                        equalTo("workStatus.uuid", WorkStatus.Status.IN_WORK).
                        findAllSorted("createdAt", Sort.ASCENDING));
        realm.close();
        return tasks;
    }

    @Override
    public List<Task> getTasks() {
        Realm realm = Realm.getDefaultInstance();
        List<Task> tasks = realm.copyFromRealm(
                realm.where(Task.class)./*equalTo("equipment.uuid", equipment.getUuid()).*/
                        findAllSorted("createdAt", Sort.ASCENDING));
        realm.close();
        return tasks;
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
                task.setChangedAt(new Date());
                realm.copyToRealmOrUpdate(task);
            }
        });
        realm.close();
    }

    @Override
    public void setTaskVerdict(final Task task, final TaskVerdict taskVerdict) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                task.setTaskVerdict(taskVerdict);
                task.setChangedAt(new Date());
                realm.copyToRealmOrUpdate(task);
            }
        });
        realm.close();
    }

    @Override
    public void setEndDate(final Task task) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                task.setStartDate(new Date());
                task.setEndDate(new Date());
                task.setChangedAt(new Date());
                realm.copyToRealmOrUpdate(task);
            }
        });
        realm.close();
    }

    @Override
    public List<Operation> getOperationByTask(Task task) {
        return task.getOperations();
    }
}