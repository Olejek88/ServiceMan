package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import ru.shtrm.serviceman.data.HouseStatus;
import ru.shtrm.serviceman.data.source.HouseStatusDataSource;

public class HouseStatusLocalDataSource implements HouseStatusDataSource {

    @Nullable
    private static HouseStatusLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private HouseStatusLocalDataSource() {

    }

    public static HouseStatusLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HouseStatusLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public List<HouseStatus> getHouseStatuses() {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                realm.where(HouseStatus.class).findAllSorted("title"));
    }

    @Override
    public HouseStatus getHouseStatus(String uuid) {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                realm.where(HouseStatus.class).equalTo("uuid", uuid).findFirst());
    }
}