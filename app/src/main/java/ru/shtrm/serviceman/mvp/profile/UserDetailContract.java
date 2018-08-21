package ru.shtrm.serviceman.mvp.profile;

import ru.shtrm.serviceman.mvp.BasePresenter;
import ru.shtrm.serviceman.mvp.BaseView;

public interface UserDetailContract {

    interface View extends BaseView<Presenter> {

        void setUserName(String name);

        void setUserAddress(String address);

        void setUserWebsite(String website);

    }

    interface Presenter extends BasePresenter {
    }
}
