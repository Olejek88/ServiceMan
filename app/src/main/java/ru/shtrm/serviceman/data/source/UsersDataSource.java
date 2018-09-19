package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;

import io.realm.RealmResults;
import ru.shtrm.serviceman.data.User;

public interface UsersDataSource {

    RealmResults<User> getUsers();

    User getUser(@NonNull String userUuid);

    boolean checkUser(@NonNull String userUuid, String pin);

}
