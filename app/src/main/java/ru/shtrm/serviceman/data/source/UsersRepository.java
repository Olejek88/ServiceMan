package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.realm.RealmResults;
import ru.shtrm.serviceman.data.User;

public class UsersRepository implements UsersDataSource {

    @Nullable
    private static UsersRepository INSTANCE = null;

    @NonNull
    private final UsersDataSource localDataSource;

    // Prevent direct instantiation
    private UsersRepository(@NonNull UsersDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static UsersRepository getInstance(@NonNull UsersDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new UsersRepository(localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public RealmResults<User> getUsers() {
        return localDataSource.getUsers();
    }

    @Override
    public RealmResults<User> getUsers(int type) {
        return localDataSource.getUsers(type);
    }

    @Override
    public RealmResults<User> getUsers(Integer[] types) {
        return localDataSource.getUsers(types);
    }

    @Override
    public User getUser(@NonNull String userUuid) {
        return localDataSource.getUser(userUuid);
    }

    @Override
    public boolean checkUser(@NonNull String userUuid, @NonNull String pin) {
        return localDataSource.checkUser(userUuid, pin);
    }
}
