package ru.shtrm.serviceman.mvp.profile;

import android.support.annotation.NonNull;

import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.data.source.UsersRepository;
import ru.shtrm.serviceman.data.source.local.UsersLocalDataSource;

public class UserDetailPresenter implements UserDetailContract.Presenter {

    @NonNull
    private UserDetailContract.View view;

    public UserDetailPresenter(@NonNull UserDetailContract.View view,
                               @NonNull UsersRepository usersRepository,
                               @NonNull String userId) {
        this.view = view;
        if (this.view!=null)
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
        User user = UsersLocalDataSource.getInstance().getAuthorisedUser();
        view.showUser(user);
    }
}
