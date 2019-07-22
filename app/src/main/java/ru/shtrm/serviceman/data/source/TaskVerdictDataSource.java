package ru.shtrm.serviceman.data.source;

import java.util.List;

import ru.shtrm.serviceman.data.TaskVerdict;

public interface TaskVerdictDataSource {

    List<TaskVerdict> getTaskVerdicts();

    TaskVerdict getTaskVerdict(String uuid);

}
