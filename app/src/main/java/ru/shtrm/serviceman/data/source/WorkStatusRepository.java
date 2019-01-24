package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import ru.shtrm.serviceman.data.WorkStatus;

public class WorkStatusRepository implements WorkStatusDataSource {

    @Nullable
    private static WorkStatusRepository INSTANCE = null;

    @NonNull
    private final WorkStatusDataSource localDataSource;

    // Prevent direct instantiation
    private WorkStatusRepository(@NonNull WorkStatusDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static WorkStatusRepository getInstance(@NonNull WorkStatusDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new WorkStatusRepository(localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public List<WorkStatus> getWorkStatuses() {
        return localDataSource.getWorkStatuses();
    }

    @Override
    public WorkStatus getWorkStatusByUuid(String uuid) {
        return localDataSource.getWorkStatusByUuid(uuid);
    }
}
