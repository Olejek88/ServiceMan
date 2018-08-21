package ru.shtrm.serviceman.mvp.users;

import java.util.List;

import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.mvp.BasePresenter;
import ru.shtrm.serviceman.mvp.BaseView;

public interface UsersContract {

    interface View extends BaseView<Presenter> {

        void showUsers(List<User> list);

    }

    interface Presenter extends BasePresenter {

    }

}
