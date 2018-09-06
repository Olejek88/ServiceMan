package ru.shtrm.serviceman.mvp.equipment;

import android.support.annotation.NonNull;

import java.util.List;

import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.EquipmentStatus;
import ru.shtrm.serviceman.data.Flat;
import ru.shtrm.serviceman.data.FlatStatus;
import ru.shtrm.serviceman.mvp.BasePresenter;
import ru.shtrm.serviceman.mvp.BaseView;

public interface EquipmentContract {

    interface View extends BaseView<Presenter> {
    }

    interface Presenter extends BasePresenter {
        void addEquipment(Equipment equipment);
        void updateEquipmentStatus(Equipment equipment, EquipmentStatus equipmentStatus);
        List<EquipmentStatus> loadEquipmentStatuses();
        List<Equipment> loadEquipmentsByFlat(Flat flat);
    }
}
