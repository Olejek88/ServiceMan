package ru.shtrm.serviceman.mvp.object;

import android.support.annotation.NonNull;

import java.util.List;

import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.ZhObject;
import ru.shtrm.serviceman.mvp.BasePresenter;
import ru.shtrm.serviceman.mvp.BaseView;

public interface ObjectContract {

    interface View extends BaseView<Presenter> {
        void showEquipments(@NonNull List<Equipment> list);
    }

    interface Presenter extends BasePresenter {
        List<Equipment> loadEquipmentsByObject(ZhObject object);
    }
}
