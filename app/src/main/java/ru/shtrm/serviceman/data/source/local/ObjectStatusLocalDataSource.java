package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import ru.shtrm.serviceman.data.ZhObjectStatus;
import ru.shtrm.serviceman.data.source.ObjectStatusDataSource;

public class ObjectStatusLocalDataSource implements ObjectStatusDataSource {

    @Nullable
    private static ObjectStatusLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private ObjectStatusLocalDataSource() {

    }

    public static ObjectStatusLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ObjectStatusLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public List<ZhObjectStatus> getObjectStatuses() {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                realm.where(ZhObjectStatus.class).findAllSorted("title"));
    }

    @Override
    public ZhObjectStatus getObjectStatus(String uuid) {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                realm.where(ZhObjectStatus.class).equalTo("uuid", uuid).
                        findFirst());
    }
}