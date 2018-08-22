package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmSchema;
import io.realm.Sort;
import ru.shtrm.serviceman.data.Alarm;
import ru.shtrm.serviceman.data.AlarmStatus;
import ru.shtrm.serviceman.data.AlarmType;
import ru.shtrm.serviceman.data.source.AlarmDataSource;

public class AlarmLocalDataSource implements AlarmDataSource {

    @Nullable
    private static AlarmLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private AlarmLocalDataSource() {

    }

    public static AlarmLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AlarmLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public List<Alarm> getAlarms() {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                        realm.where(Alarm.class).findAllSorted("date", Sort.ASCENDING));
    }

    @Override
    public Alarm getAlarm(@NonNull String uuid) {
        Realm realm = Realm.getDefaultInstance();
        Alarm alarm = realm.where(Alarm.class).equalTo("uuid", uuid).findFirst();
        if (alarm!=null)
            return realm.copyFromRealm(alarm);
        else
            return null;
    }

    @Override
    public List<Alarm> getAlarmsByType(AlarmType alarmType) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Alarm.class).equalTo("alarmType", alarmType.getUuid()).findAll();
    }

    @Override
    public List<Alarm> getAlarmsByStatus(AlarmStatus alarmStatus) {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Alarm.class).equalTo("alarmStatus", alarmStatus.getUuid()).findAll();
    }
}