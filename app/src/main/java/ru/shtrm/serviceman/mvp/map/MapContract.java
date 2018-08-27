package ru.shtrm.serviceman.mvp.map;

import android.support.annotation.NonNull;

import java.util.List;

import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.mvp.BasePresenter;
import ru.shtrm.serviceman.mvp.BaseView;

public interface MapContract {

    interface View extends BaseView<Presenter> {

        void showDefaultView(boolean toShow);
        void showHouses(@NonNull List<House> list);

    }

    interface Presenter extends BasePresenter {

        void loadHouses();

    }

}
