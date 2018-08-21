package ru.shtrm.serviceman.mvp.profileedit;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.data.source.UsersRepository;

public class UserEditPresenter implements UserEditContract.Presenter {

    @NonNull
    private UserEditContract.View view;

    @NonNull
    private UsersRepository usersRepository;

    @NonNull
    private String userId;

    @NonNull
    private CompositeDisposable compositeDisposable;

    public UserEditPresenter(@NonNull UserEditContract.View view,
                             @NonNull UsersRepository usersRepository,
                             @NonNull String userId) {
        this.view = view;
        this.usersRepository = usersRepository;
        this.userId = userId;
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
        Observable<User> user = usersRepository.getUser(userId);
        if (user==null) return;

        Disposable disposable = user.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<User>() {
                    @Override
                    public void onNext(User value) {
                        if (value != null) {
                            view.setUserName(value.getName());
                            view.setUserAddress(value.getAddress());
                            view.setUserWebsite(value.getWebsite());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showErrorMsg();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void saveUser(String id, String name, String address, String website, String phone,
                         String imageName, Bitmap avatar, User user) {
        compositeDisposable.clear();
        User newUser = new User();
        newUser.setId(id);
        newUser.setName(name);
        newUser.setAddress(address);
        newUser.setWebsite(website);
        newUser.setPhone(phone);
        //MainUtil.storeNewImage(avatar, context, 512, fileName);
        if (imageName != null)
            newUser.setAvatar(imageName);
        usersRepository.saveUser(newUser);
    }
}
