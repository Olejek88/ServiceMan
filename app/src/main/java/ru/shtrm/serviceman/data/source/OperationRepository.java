package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import ru.shtrm.serviceman.data.Operation;
import ru.shtrm.serviceman.data.Task;
import ru.shtrm.serviceman.data.WorkStatus;

public class OperationRepository implements OperationDataSource {

    @Nullable
    private static OperationRepository INSTANCE = null;

    @NonNull
    private final OperationDataSource localDataSource;

    // Prevent direct instantiation
    private OperationRepository(@NonNull OperationDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static OperationRepository getInstance(@NonNull OperationDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new OperationRepository(localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void setOperationStatus(Operation operation, WorkStatus status) {
        localDataSource.setOperationStatus(operation, status);
    }
}
