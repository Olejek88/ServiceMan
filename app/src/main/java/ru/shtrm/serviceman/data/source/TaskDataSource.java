package ru.shtrm.serviceman.data.source;

import java.util.List;

import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.Task;
import ru.shtrm.serviceman.data.WorkStatus;

public interface TaskDataSource {

    List<Task> getTaskByEquipment(Equipment equipment, String status);

    boolean checkAllOperationsComplete(Task task);

    void setTaskStatus(Task task, WorkStatus status);
}
