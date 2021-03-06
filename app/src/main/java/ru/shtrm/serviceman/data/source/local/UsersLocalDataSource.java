package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.data.source.UsersDataSource;
import ru.shtrm.serviceman.util.MainUtil;

public class UsersLocalDataSource implements UsersDataSource {

    @Nullable
    private static UsersLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private UsersLocalDataSource() {

    }

    public static UsersLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UsersLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public RealmResults<User> getUsers() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<User> list = realm.where(User.class)
                .notEqualTo("uuid", User.SERVICE_USER_UUID)
                .findAllSorted("name", Sort.ASCENDING);
        realm.close();
        return list;
    }

    @Override
    public RealmResults<User> getUsers(int type) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<User> list = realm
                .where(User.class)
                .notEqualTo("uuid", User.SERVICE_USER_UUID)
                .equalTo("type", type)
                .findAllSorted("name", Sort.ASCENDING);
        realm.close();
        return list;
    }

    @Override
    public RealmResults<User> getUsers(Integer[] types) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<User> list = realm
                .where(User.class)
                .notEqualTo("uuid", User.SERVICE_USER_UUID)
                .in("type", types)
                .findAllSorted("name", Sort.ASCENDING);
        realm.close();
        return list;
    }

    public User getLastUser() {
        Realm realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).findFirst();
        if (user != null) {
            user = realm.copyFromRealm(user);
        }

        realm.close();
        return user;
    }

    @Override
    public User getUser(@NonNull String uuid) {
        Realm realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).equalTo("uuid", uuid).findFirst();
        if (user != null) {
            user = realm.copyFromRealm(user);
        }

        realm.close();
        return user;
    }

    @Override
    public boolean checkUser(@NonNull String userUuid, @NonNull String pin) {
        Realm realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).equalTo("uuid", userUuid).findFirst();
        if (user != null) {
            user = realm.copyFromRealm(user);
        }

        realm.close();
        return user != null && user.getPin().equals(MainUtil.MD5(pin));
    }
}