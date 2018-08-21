package ru.shtrm.serviceman.mvp.trickdetails;

import android.support.annotation.NonNull;

import ru.shtrm.serviceman.data.Trick;
import ru.shtrm.serviceman.mvp.BasePresenter;
import ru.shtrm.serviceman.mvp.BaseView;

public interface TrickDetailsContract {

    interface View extends BaseView<Presenter> {

        void showTrickDetails(@NonNull Trick Trick);

        void exit();

    }

    interface Presenter extends BasePresenter {

        void deleteTrick();
    }

}
