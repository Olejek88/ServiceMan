package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import io.realm.Sort;
import ru.shtrm.serviceman.data.AlarmType;
import ru.shtrm.serviceman.data.source.AlarmTypeDataSource;

public class AlarmTypeLocalDataSource implements AlarmTypeDataSource {

    @Nullable
    private static AlarmTypeLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private AlarmTypeLocalDataSource() {

    }

    public static AlarmTypeLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AlarmTypeLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public List<AlarmType> getAllAlarmTypes() {
        Realm realm = Realm.getDefaultInstance();
        List<AlarmType> list = realm.where(AlarmType.class)
                .findAllSorted("title", Sort.ASCENDING);
        list = realm.copyFromRealm(list);
        realm.close();
        return list;
    }
}