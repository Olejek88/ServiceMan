package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import ru.shtrm.serviceman.data.AlarmStatus;

public class AlarmStatusRepository implements AlarmStatusDataSource {

    @Nullable
    private static AlarmStatusRepository INSTANCE = null;

    @NonNull
    private final AlarmStatusDataSource localDataSource;

    // Prevent direct instantiation
    private AlarmStatusRepository(@NonNull AlarmStatusDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static AlarmStatusRepository getInstance(@NonNull AlarmStatusDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new AlarmStatusRepository(localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public List<AlarmStatus> getAllAlarmStatus() {
        return localDataSource.getAllAlarmStatus();
    }
}
