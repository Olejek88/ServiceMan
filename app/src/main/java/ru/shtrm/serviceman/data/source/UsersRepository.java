package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.reactivex.Observable;
import ru.shtrm.serviceman.data.Answer;
import ru.shtrm.serviceman.data.Question;
import ru.shtrm.serviceman.data.Trick;
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
    public Observable<List<User>> getUsers() {
        return localDataSource.getUsers();
    }

    @Override
    public Observable<User> getUser(@NonNull String id) {
        return localDataSource.getUser(id);
    }

    @Override
    public Observable<List<User>> searchUsers(@NonNull String keyWords) {
        return localDataSource.searchUsers(keyWords);
    }

    @Override
    public User getUserById(@NonNull String id) {
        return localDataSource.getUserById(id);
    }

    @Override
    public boolean isUserExist(@NonNull String id) {
        return localDataSource.isUserExist(id);
    }

    @Override
    public void deleteUser(@NonNull String id) {
        localDataSource.deleteUser(id);
    }

    @Override
    public void saveUser(@NonNull User user) {
        localDataSource.saveUser(user);
    }

    @Override
    public void addQuestion(@NonNull Question question, User user) {
        localDataSource.addQuestion(question, user);
    }

    @Override
    public void addTrick(@NonNull Trick trick, User user) {
        localDataSource.addTrick(trick, user);
    }

    @Override
    public void addAnswer(@NonNull Answer answer, User user) {
        localDataSource.addAnswer(answer, user);
    }

}
