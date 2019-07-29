package ru.shtrm.serviceman.mvp.user;

import android.support.annotation.NonNull;

import java.util.List;

import io.realm.RealmResults;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.mvp.BasePresenter;
import ru.shtrm.serviceman.mvp.BaseView;

public interface UserContract {

    interface View extends BaseView<Presenter> {
        void showUsers(@NonNull List<User> list);
    }

    interface Presenter extends BasePresenter {
        RealmResults<User> loadUsers();

        RealmResults<User> loadUsers(int type);
    }
}
