package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import ru.shtrm.serviceman.data.Alarm;
import ru.shtrm.serviceman.data.AlarmStatus;
import ru.shtrm.serviceman.data.AlarmType;

public class AlarmRepository implements AlarmDataSource {

    @Nullable
    private static AlarmRepository INSTANCE = null;

    @NonNull
    private final AlarmDataSource localDataSource;

    // Prevent direct instantiation
    private AlarmRepository(@NonNull AlarmDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static AlarmRepository getInstance(@NonNull AlarmDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new AlarmRepository(localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public List<Alarm> getAlarms() {
        return localDataSource.getAlarms();
    }

    @Override
    public Alarm getAlarm(@NonNull String uuid) {
        return localDataSource.getAlarm(uuid);
    }

    @Override
    public List<Alarm> getAlarmsByType(AlarmType alarmType){
        return localDataSource.getAlarmsByType(alarmType);
    }

    @Override
    public List<Alarm> getAlarmsByStatus(AlarmStatus alarmStatus){
        return localDataSource.getAlarmsByStatus(alarmStatus);
    }

    @Override
    public long getLastId() {
        return localDataSource.getLastId();
    }
}
