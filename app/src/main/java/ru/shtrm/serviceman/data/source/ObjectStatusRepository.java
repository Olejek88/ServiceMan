package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import ru.shtrm.serviceman.data.ZhObjectStatus;

public class ObjectStatusRepository implements ObjectStatusDataSource {

    @Nullable
    private static ObjectStatusRepository INSTANCE = null;

    @NonNull
    private final ObjectStatusDataSource localDataSource;

    // Prevent direct instantiation
    private ObjectStatusRepository(@NonNull ObjectStatusDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static ObjectStatusRepository getInstance(@NonNull ObjectStatusDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new ObjectStatusRepository(localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public List<ZhObjectStatus> getObjectStatuses() {
        return localDataSource.getObjectStatuses();
    }

    @Override
    public ZhObjectStatus getObjectStatus(String uuid) {
        return localDataSource.getObjectStatus(uuid);
    }

}
