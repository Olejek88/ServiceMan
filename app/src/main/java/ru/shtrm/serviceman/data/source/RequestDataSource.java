package ru.shtrm.serviceman.data.source;

import java.util.List;

import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.Operation;
import ru.shtrm.serviceman.data.Request;
import ru.shtrm.serviceman.data.Task;
import ru.shtrm.serviceman.data.WorkStatus;

public interface RequestDataSource {

    Request getRequest(String uuid);

    Request getRequestByTask(String taskUuid);

    List<Request> getRequests();
}
