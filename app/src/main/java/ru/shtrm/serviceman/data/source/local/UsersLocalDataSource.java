package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import io.realm.Sort;
import ru.shtrm.serviceman.data.AuthorizedUser;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.data.source.UsersDataSource;

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
                        realm.where(User.class).findAllSorted("name", Sort.ASCENDING));
    }

    public User getLastUser() {
        Realm realm = Realm.getDefaultInstance();
        User user = realm.copyFromRealm(realm.where(User.class).findFirst());
        realm.close();
        return user;
    }

    @Override
    public User getUser(@NonNull String id) {
        Realm realm = Realm.getDefaultInstance();
        User user = realm.where(User.class).equalTo("id", id).findFirst();
        if (user!=null)
            return realm.copyFromRealm(user);
        else
            return null;
    }

    public User getAuthorisedUser() {
        Realm realm = Realm.getDefaultInstance();
        AuthorizedUser aUser = AuthorizedUser.getInstance();
        if (aUser!=null) {
            User user = realm.where(User.class).equalTo("id",
                    aUser.getId()).findFirst();
            if (user!=null) {
                return realm.copyFromRealm(user);
            }
        }
        return null;
    }

}