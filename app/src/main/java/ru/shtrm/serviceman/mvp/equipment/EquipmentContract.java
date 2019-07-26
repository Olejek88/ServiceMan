package ru.shtrm.serviceman.mvp.equipment;

import java.util.List;

import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.EquipmentStatus;
import ru.shtrm.serviceman.mvp.BasePresenter;
import ru.shtrm.serviceman.mvp.BaseView;

public interface EquipmentContract {

    interface View extends BaseView<Presenter> {
    }

    interface Presenter extends BasePresenter {
        void updateEquipmentStatus(Equipment equipment, EquipmentStatus equipmentStatus);
        List<EquipmentStatus> loadEquipmentStatuses();
    }
}
