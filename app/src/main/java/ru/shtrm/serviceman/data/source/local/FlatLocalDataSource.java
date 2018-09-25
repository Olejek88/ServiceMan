package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import ru.shtrm.serviceman.data.Flat;
import ru.shtrm.serviceman.data.FlatStatus;
import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.source.FlatDataSource;

public class FlatLocalDataSource implements FlatDataSource {

    @Nullable
    private static FlatLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private FlatLocalDataSource() {

    }

    public static FlatLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FlatLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public List<Flat> getFlatsByHouse(House house) {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                realm.where(Flat.class).equalTo("house.uuid", house.getUuid()).
                        findAllSorted("number"));
    }

    @Override
    public Flat getFlat(String uuid) {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                realm.where(Flat.class).equalTo("uuid", uuid).
                        findFirst());
    }

    @Override
    public void addFlat(final Flat flat) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(flat);
            }
        });
        realm.close();
    }

    @Override
    public void updateFlatStatus(final Flat flat, final FlatStatus flatStatus) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                flat.setSent(false);
                flat.setFlatStatus(flatStatus);
                realm.copyToRealmOrUpdate(flat);
            }
        });
        realm.close();
    }
}