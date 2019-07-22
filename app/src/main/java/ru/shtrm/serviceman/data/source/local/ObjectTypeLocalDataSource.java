package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import ru.shtrm.serviceman.data.ZhObjectType;
import ru.shtrm.serviceman.data.source.ObjectTypeDataSource;

public class ObjectTypeLocalDataSource implements ObjectTypeDataSource {

    @Nullable
    private static ObjectTypeLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private ObjectTypeLocalDataSource() {

    }

    public static ObjectTypeLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ObjectTypeLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public List<ZhObjectType> getObjectTypes() {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                realm.where(ZhObjectType.class).findAllSorted("title"));
    }

    @Override
    public ZhObjectType getObjectType(String uuid) {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                realm.where(ZhObjectType.class).equalTo("uuid", uuid).
                        findFirst());
    }
}