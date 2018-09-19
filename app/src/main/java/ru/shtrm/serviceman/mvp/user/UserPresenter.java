package ru.shtrm.serviceman.mvp.user;

import android.support.annotation.NonNull;

import io.realm.RealmResults;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.data.source.UsersRepository;

public class UserPresenter implements UserContract.Presenter {

    @NonNull
    private final UsersRepository userRepository;

    public UserPresenter(@NonNull UsersRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {
    }

    @Override
    public RealmResults<User> loadUsers() {
        return userRepository.getUsers();
    }
}
