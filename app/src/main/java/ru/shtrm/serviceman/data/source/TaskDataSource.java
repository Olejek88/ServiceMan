package ru.shtrm.serviceman.data.source;

import java.util.List;

import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.Task;
import ru.shtrm.serviceman.data.WorkStatus;

public interface TaskDataSource {

    Task getTask(String uuid);

    List<Task> getTaskByEquipment(Equipment equipment, String status);

    List<Task> getNewTasks();

    boolean checkAllOperationsComplete(Task task);

    void setTaskStatus(Task task, WorkStatus status);
}
