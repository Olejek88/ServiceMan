package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import ru.shtrm.serviceman.data.TaskVerdict;


public class TaskVerdictRepository implements TaskVerdictDataSource {

    @Nullable
    private static TaskVerdictRepository INSTANCE = null;

    @NonNull
    private final TaskVerdictDataSource localDataSource;

    // Prevent direct instantiation
    private TaskVerdictRepository(@NonNull TaskVerdictDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static TaskVerdictRepository getInstance(@NonNull TaskVerdictDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new TaskVerdictRepository(localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public List<TaskVerdict> getTaskVerdicts() {
        return localDataSource.getTaskVerdicts();
    }

    @Override
    public TaskVerdict getTaskVerdict(String uuid) {
        return localDataSource.getTaskVerdict(uuid);
    }

}
