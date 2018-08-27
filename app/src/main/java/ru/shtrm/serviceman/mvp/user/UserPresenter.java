package ru.shtrm.serviceman.mvp.user;

import android.support.annotation.NonNull;

import java.util.List;

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
    public List<User> loadUsers() {
        List<User> users = userRepository.getUsers();
        //view.showUsers(users);
        return users;
    }
}
