package ru.shtrm.serviceman.mvp.flat;

import android.support.annotation.NonNull;

import java.util.List;

import ru.shtrm.serviceman.data.Alarm;
import ru.shtrm.serviceman.data.AlarmStatus;
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.Flat;
import ru.shtrm.serviceman.data.FlatStatus;
import ru.shtrm.serviceman.mvp.BasePresenter;
import ru.shtrm.serviceman.mvp.BaseView;

public interface FlatContract {

    interface View extends BaseView<Presenter> {
        void showEquipments(@NonNull List<Equipment> list);
    }

    interface Presenter extends BasePresenter {
        void addFlat(Flat flat);
        void updateFlatStatus(Flat flat, FlatStatus flatStatus);
        List<FlatStatus> loadFlatStatuses();
        List<Equipment> loadEquipmentsByFlat(Flat flat);
    }
}
