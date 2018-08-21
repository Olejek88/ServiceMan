package ru.shtrm.serviceman.data.source.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.reactivex.Observable;
import ru.shtrm.serviceman.data.Answer;
import ru.shtrm.serviceman.data.Question;
import ru.shtrm.serviceman.data.Trick;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.data.source.UsersDataSource;

public class UsersRemoteDataSource implements UsersDataSource {

    @Nullable
    private static UsersRemoteDataSource INSTANCE;

    // Prevent direct instantiation
    private UsersRemoteDataSource() {

    }

    // Access this instance for outside classes.
    public static UsersRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UsersRemoteDataSource();
        }
        return INSTANCE;
    }

    // Destroy the instance.
    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public Observable<List<User>> getUsers() {
        // Not required because the {@link UsersRepository} handles the logic
        // of refreshing the users from all available data source
        return null;
    }

    @Override
    public Observable<User> getUser(@NonNull String id) {
        // Not required because the {@link UsersRepository} handles the logic
        // of refreshing the users from all available data source
        return null;
    }

    @Override
    public User getUserById(@NonNull String id) {
        // Not required because the {@link UsersRepository} handles the logic
        // of refreshing the users from all available data source
        return null;
    }

    @Override
    public void saveUser(@NonNull User user) {
        // Not required because the {@link UsersRepository} handles the logic
        // of refreshing the users from all available data source
    }

    @Override
    public void deleteUser(@NonNull String id) {
        // Not required because the {@link UsersRepository} handles the logic
        // of refreshing the users from all available data source
    }

    @Override
    public boolean isUserExist(@NonNull String id) {
        // Not required
        return false;
    }

    @Override
    public Observable<List<User>> searchUsers(@NonNull String keyWords) {
        // Not required
        return null;
    }

    @Override
    public void addQuestion(@NonNull Question question, User user) {
        // Not required because the {@link UsersRepository} handles the logic
        // of refreshing the users from all available data source
    }

    @Override
    public void addTrick(@NonNull Trick trick, User user) {
        // Not required because the {@link UsersRepository} handles the logic
        // of refreshing the users from all available data source
    }

    @Override
    public void addAnswer(@NonNull Answer answer, User user) {
        // Not required because the {@link UsersRepository} handles the logic
        // of refreshing the users from all available data source
    }

}