package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import ru.shtrm.serviceman.data.TaskType;


public class TaskTypeRepository implements TaskTypeDataSource {

    @Nullable
    private static TaskTypeRepository INSTANCE = null;

    @NonNull
    private final TaskTypeDataSource localDataSource;

    // Prevent direct instantiation
    private TaskTypeRepository(@NonNull TaskTypeDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static TaskTypeRepository getInstance(@NonNull TaskTypeDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new TaskTypeRepository(localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public List<TaskType> getTaskTypes() {
        return localDataSource.getTaskTypes();
    }

    @Override
    public TaskType getTaskType(String uuid) {
        return localDataSource.getTaskType(uuid);
    }

}
