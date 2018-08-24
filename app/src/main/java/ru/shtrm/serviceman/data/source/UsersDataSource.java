package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;

import java.util.List;

import ru.shtrm.serviceman.data.User;

public interface UsersDataSource {

    List<User> getUsers();

    User getUser(@NonNull String userUuid);

    boolean checkUser(@NonNull String userUuid, String pin);

}
