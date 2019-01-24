package ru.shtrm.serviceman.data.source;

import java.util.List;

import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.EquipmentStatus;
import ru.shtrm.serviceman.data.Flat;
import ru.shtrm.serviceman.data.Operation;
import ru.shtrm.serviceman.data.PhotoEquipment;
import ru.shtrm.serviceman.data.Task;
import ru.shtrm.serviceman.data.WorkStatus;

public interface TaskDataSource {

    List<Task> getTaskByEquipment(Equipment equipment, String status);

    List<Task> getTaskByFlat(Flat flat, String status);

    boolean checkAllOperationsComplete(Task task);

    void setTaskStatus(Task task, WorkStatus status);
}
