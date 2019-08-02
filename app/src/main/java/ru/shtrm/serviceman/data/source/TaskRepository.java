package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.Operation;
import ru.shtrm.serviceman.data.Task;
import ru.shtrm.serviceman.data.TaskVerdict;
import ru.shtrm.serviceman.data.WorkStatus;

public class TaskRepository implements TaskDataSource {

    @Nullable
    private static TaskRepository INSTANCE = null;

    @NonNull
    private final TaskDataSource localDataSource;

    // Prevent direct instantiation
    private TaskRepository(@NonNull TaskDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static TaskRepository getInstance(@NonNull TaskDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new TaskRepository(localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public List<Task> getTaskByEquipment(Equipment equipment, String status) {
        return localDataSource.getTaskByEquipment(equipment,status);
    }

    @Override
    public Task getTask(String uuid) {
        return localDataSource.getTask(uuid);
    }

    @Override
    public List<Task> getNewTasks() {
        return localDataSource.getNewTasks();
    }

    @Override
    public List<Task> getTasks() {
        return localDataSource.getTasks();
    }

    @Override
    public boolean checkAllOperationsComplete(Task task) {
        return localDataSource.checkAllOperationsComplete(task);
    }

    @Override
    public void setTaskStatus(Task task, WorkStatus status) {
        localDataSource.setTaskStatus(task, status);
    }

    @Override
    public void setTaskVerdict(Task task, TaskVerdict taskVerdict) {
        localDataSource.setTaskVerdict(task, taskVerdict);
    }

    @Override
    public void setEndDate(Task task) {
        localDataSource.setEndDate(task);
    }

    @Override
    public List<Operation> getOperationByTask(Task task) {
        return localDataSource.getOperationByTask(task);
    }
}
