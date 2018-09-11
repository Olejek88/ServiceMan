package ru.shtrm.serviceman.mvp.profile;

import android.support.annotation.NonNull;
import android.view.View;

import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.mvp.BasePresenter;
import ru.shtrm.serviceman.mvp.BaseView;

public interface UserDetailContract {

    interface View extends BaseView<Presenter> {
        void showUser(@NonNull User user);
    }

    interface Presenter extends BasePresenter {
    }
}
