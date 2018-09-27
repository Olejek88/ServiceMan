package ru.shtrm.serviceman.mvp.profile;

import android.support.annotation.NonNull;

import ru.shtrm.serviceman.data.AuthorizedUser;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.data.source.UsersRepository;

public class UserDetailPresenter implements UserDetailContract.Presenter {

    @NonNull
    private UserDetailContract.View view;

    public UserDetailPresenter(@NonNull UserDetailContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {
        fetchUserData();
    }

    @Override
    public void unsubscribe() {
    }

    private void fetchUserData() {
        User user = AuthorizedUser.getInstance().getUser();
        view.showUser(user);
    }
}
