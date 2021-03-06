package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import io.realm.Sort;
import ru.shtrm.serviceman.data.UserHouse;
import ru.shtrm.serviceman.data.source.UserHouseDataSource;

public class UserHouseLocalDataSource implements UserHouseDataSource {

    @Nullable
    private static UserHouseLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private UserHouseLocalDataSource() {

    }

    public static UserHouseLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserHouseLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public List<UserHouse> getAllUserHouses() {
        Realm realm = Realm.getDefaultInstance();
        List<UserHouse> list = realm.copyFromRealm(realm.where(UserHouse.class)
                .findAllSorted("user.name", Sort.ASCENDING));
        realm.close();
        return list;
    }

    @Override
    public List<UserHouse> getHousesByUser(@NonNull String userUuid) {
        Realm realm = Realm.getDefaultInstance();
        List<UserHouse> list = realm.where(UserHouse.class).
                findAllSorted("name", Sort.ASCENDING);
        list = realm.copyFromRealm(list);
        realm.close();
        return list;
    }
}