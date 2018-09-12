package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import ru.shtrm.serviceman.data.FlatStatus;

public class FlatStatusRepository implements FlatStatusDataSource {

    @Nullable
    private static FlatStatusRepository INSTANCE = null;

    @NonNull
    private final FlatStatusDataSource localDataSource;

    // Prevent direct instantiation
    private FlatStatusRepository(@NonNull FlatStatusDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static FlatStatusRepository getInstance(@NonNull FlatStatusDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new FlatStatusRepository(localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public List<FlatStatus> getFlatStatuses() {
        return localDataSource.getFlatStatuses();
    }
}
