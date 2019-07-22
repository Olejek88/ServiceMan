package ru.shtrm.serviceman.data.source;

import java.util.List;

import ru.shtrm.serviceman.data.TaskType;

public interface TaskTypeDataSource {

    List<TaskType> getTaskTypes();

    TaskType getTaskType(String uuid);

}
