package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.ZhObject;
import ru.shtrm.serviceman.data.source.ObjectDataSource;

public class ObjectLocalDataSource implements ObjectDataSource {

    @Nullable
    private static ObjectLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private ObjectLocalDataSource() {

    }

    public static ObjectLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ObjectLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public List<ZhObject> getObjects() {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                realm.where(ZhObject.class).findAllSorted("title"));
    }

    @Override
    public List<ZhObject> getObjectsByHouse(House house) {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                realm.where(ZhObject.class).equalTo("house.uuid", house.getUuid()).
                        findAllSorted("title"));
    }

    @Override
    public ZhObject getObject(String uuid) {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                realm.where(ZhObject.class).equalTo("uuid", uuid).
                        findFirst());
    }
}