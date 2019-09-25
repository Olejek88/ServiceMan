package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;

import io.realm.RealmResults;
import ru.shtrm.serviceman.data.User;

public interface UsersDataSource {

    RealmResults<User> getUsers();

    RealmResults<User> getUsers(int type);

    RealmResults<User> getUsers(Integer[] types);

    User getUser(@NonNull String userUuid);

    boolean checkUser(@NonNull String userUuid, String pin);

}
