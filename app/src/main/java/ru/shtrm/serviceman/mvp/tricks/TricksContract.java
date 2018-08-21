package ru.shtrm.serviceman.mvp.tricks;

import android.support.annotation.NonNull;

import java.util.List;

import ru.shtrm.serviceman.data.Trick;
import ru.shtrm.serviceman.mvp.BasePresenter;
import ru.shtrm.serviceman.mvp.BaseView;

public interface TricksContract {

    interface View extends BaseView<Presenter> {

        void showEmptyView(boolean toShow);

        void showTricks(@NonNull List<Trick> list);
    }

    interface Presenter extends BasePresenter {

        void loadTricks();
    }

}
