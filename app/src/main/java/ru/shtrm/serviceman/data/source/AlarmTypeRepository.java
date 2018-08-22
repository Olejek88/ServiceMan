package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import ru.shtrm.serviceman.data.AlarmStatus;
import ru.shtrm.serviceman.data.AlarmType;

public class AlarmTypeRepository implements AlarmTypeDataSource {

    @Nullable
    private static AlarmTypeRepository INSTANCE = null;

    @NonNull
    private final AlarmTypeDataSource localDataSource;

    // Prevent direct instantiation
    private AlarmTypeRepository(@NonNull AlarmTypeDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static AlarmTypeRepository getInstance(@NonNull AlarmTypeDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new AlarmTypeRepository(localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public List<AlarmType> getAllAlarmTypes() {
        return localDataSource.getAllAlarmTypes();
    }
}
