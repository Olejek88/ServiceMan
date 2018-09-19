package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
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
    public List<User> getUsers() {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                realm
                        .where(User.class)
                        .notEqualTo("uuid", User.SERVICE_USER_UUID)
                        .findAllSorted("name", Sort.ASCENDING)
        );
    }

    public User getLastUser() {
        Realm realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).findFirst();
        if (user != null)
            return realm.copyFromRealm(user);
        else
            return null;
    }

    @Override
    public User getUser(@NonNull String uuid) {
        Realm realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).equalTo("uuid", uuid).findFirst();
        if (user != null)
            return realm.copyFromRealm(user);
        else
            return null;
    }

    @Override
    public boolean checkUser(@NonNull String userUuid, @NonNull String pin) {
        Realm realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).equalTo("uuid", userUuid).findFirst();
        return user != null && user.getPin().equals(MainUtil.MD5(pin));
    }
}