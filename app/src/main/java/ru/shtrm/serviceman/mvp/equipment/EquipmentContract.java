package ru.shtrm.serviceman.mvp.equipment;

import android.support.annotation.NonNull;

import java.util.List;

import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.EquipmentStatus;
import ru.shtrm.serviceman.data.Task;
import ru.shtrm.serviceman.mvp.BasePresenter;
import ru.shtrm.serviceman.mvp.BaseView;

public interface EquipmentContract {

    interface View extends BaseView<Presenter> {
        void showEmptyView(boolean toShow);
        void showTaskList(@NonNull List<Task> tasks);
    }

    interface Presenter extends BasePresenter {
        void updateEquipmentStatus(Equipment equipment, EquipmentStatus equipmentStatus);
        List<Task> loadTasks();
        List<EquipmentStatus> loadEquipmentStatuses();
    }
}
