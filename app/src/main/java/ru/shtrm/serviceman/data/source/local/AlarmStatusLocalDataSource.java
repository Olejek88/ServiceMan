package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import io.realm.Sort;
import ru.shtrm.serviceman.data.AlarmStatus;
import ru.shtrm.serviceman.data.source.AlarmStatusDataSource;

public class AlarmStatusLocalDataSource implements AlarmStatusDataSource {

    @Nullable
    private static AlarmStatusLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private AlarmStatusLocalDataSource() {

    }

    public static AlarmStatusLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AlarmStatusLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public List<AlarmStatus> getAllAlarmStatus() {
        Realm realm = Realm.getDefaultInstance();
        List<AlarmStatus> list = realm.where(AlarmStatus.class)
                .findAllSorted("title", Sort.ASCENDING);
        list = realm.copyFromRealm(list);
        realm.close();
        return list;
    }
}