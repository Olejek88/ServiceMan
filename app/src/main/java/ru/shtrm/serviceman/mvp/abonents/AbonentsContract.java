package ru.shtrm.serviceman.mvp.abonents;

import android.support.annotation.NonNull;

import java.util.List;

import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.Street;
import ru.shtrm.serviceman.data.ZhObject;
import ru.shtrm.serviceman.mvp.BasePresenter;
import ru.shtrm.serviceman.mvp.BaseView;

public interface AbonentsContract {

    interface View extends BaseView<Presenter> {

        void showEmptyView(boolean toShow);
        void showStreets(@NonNull List<Street> list);
        void showHouses(@NonNull List<House> list);
        void showObjects(@NonNull List<ZhObject> list);

        int getCurrentLevel();

        House getCurrentHouse();

        Street getCurrentStreet();
    }

    interface Presenter extends BasePresenter {

        List<Street> loadStreets();

        List<House> loadHouses(Street street);

        List<ZhObject> loadObjects(House house);

    }

}
