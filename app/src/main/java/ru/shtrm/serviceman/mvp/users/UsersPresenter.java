package ru.shtrm.serviceman.mvp.users;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.data.source.UsersRepository;

public class UsersPresenter implements UsersContract.Presenter {

    @NonNull
    private UsersContract.View view;

    @NonNull
    private UsersRepository usersRepository;

    @NonNull
    private CompositeDisposable compositeDisposable;

    public UsersPresenter(@NonNull UsersContract.View view,
                          @NonNull UsersRepository usersRepository) {
        this.view = view;
        this.usersRepository = usersRepository;
        compositeDisposable = new CompositeDisposable();
        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {
        getUsers();
    }

    @Override
    public void unsubscribe() {
        compositeDisposable.clear();
    }

    private void getUsers() {
        Disposable disposable = usersRepository
                .getUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<User>>() {
                    @Override
                    public void onNext(List<User> value) {
                        view.showUsers(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        compositeDisposable.add(disposable);
    }

}
