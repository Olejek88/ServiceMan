package ru.shtrm.serviceman.data.source;

import java.util.List;

import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.Flat;
import ru.shtrm.serviceman.data.Operation;
import ru.shtrm.serviceman.data.Task;
import ru.shtrm.serviceman.data.WorkStatus;

public interface OperationDataSource {

    List<Operation> getOperationByTask(Task task);

    void setOperationStatus(Operation operation, WorkStatus status);
}
