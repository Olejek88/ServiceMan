package ru.shtrm.serviceman.mvp.user;

import android.support.annotation.NonNull;

import java.util.List;

import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.Flat;
import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.Street;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.data.source.FlatRepository;
import ru.shtrm.serviceman.data.source.HouseRepository;
import ru.shtrm.serviceman.data.source.StreetRepository;
import ru.shtrm.serviceman.data.source.UsersRepository;
import ru.shtrm.serviceman.mvp.abonents.AbonentsContract;

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
