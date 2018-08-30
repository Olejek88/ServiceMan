package ru.shtrm.serviceman.mvp.alarm;

import android.support.annotation.NonNull;

import java.util.List;

import ru.shtrm.serviceman.data.Alarm;
import ru.shtrm.serviceman.data.AlarmStatus;
import ru.shtrm.serviceman.mvp.BasePresenter;
import ru.shtrm.serviceman.mvp.BaseView;

public interface AlarmContract {

    interface View extends BaseView<Presenter> {
        void showEmptyView(boolean toShow);
        void showAlarms(@NonNull List<Alarm> list);
    }

    interface Presenter extends BasePresenter {

        List<Alarm> loadAlarms();

        List<Alarm> loadAlarmsByStatus(AlarmStatus alarmStatus);

/*
        List<Alarm> loadAlarmsByEquipment(Equipment equipment);

        List<Alarm> loadAlarmsByFlat(Flat flat);

        List<Alarm> loadAlarmsByHouse(House house);
*/

    }

}
