package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.Operation;
import ru.shtrm.serviceman.data.Request;
import ru.shtrm.serviceman.data.Task;
import ru.shtrm.serviceman.data.WorkStatus;

public class RequestRepository implements RequestDataSource {

    @Nullable
    private static RequestRepository INSTANCE = null;

    @NonNull
    private final RequestDataSource localDataSource;

    // Prevent direct instantiation
    private RequestRepository(@NonNull RequestDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static RequestRepository getInstance(@NonNull RequestDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new RequestRepository(localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public Request getRequest(String uuid) {
        return localDataSource.getRequest(uuid);
    }

    @Override
    public Request getRequestByTask(String taskUuid) {
        return localDataSource.getRequestByTask(taskUuid);
    }

    @Override
    public List<Request> getRequests() {
        return localDataSource.getRequests();
    }

}
