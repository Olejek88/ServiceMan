package ru.shtrm.serviceman.mvp.profile;

import android.support.annotation.NonNull;

import io.reactivex.disposables.CompositeDisposable;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.data.source.UsersRepository;

public class UserDetailPresenter implements UserDetailContract.Presenter {

    @NonNull
    private UserDetailContract.View view;

    @NonNull
    private UsersRepository usersRepository;

    @NonNull
    private String userId;

    @NonNull
    private CompositeDisposable compositeDisposable;

    public UserDetailPresenter(@NonNull UserDetailContract.View view,
                               @NonNull UsersRepository usersRepository,
                               @NonNull String userId) {
        this.view = view;
        this.usersRepository = usersRepository;
        this.userId = userId;
        if (this.view!=null)
            this.view.setPresenter(this);
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe() {
        fetchUserData();
    }

    @Override
    public void unsubscribe() {
        compositeDisposable.clear();
    }

    private void fetchUserData() {
        User user = usersRepository.getUser(userId);
        if (user == null) return;
/*
        view.setUserName(value.getName());
        view.setUserAddress(value.getAddress());
        view.setUserWebsite(value.getWebsite());
*/
    }
}
