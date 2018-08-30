package ru.shtrm.serviceman.mvp.alarm;

import android.support.annotation.NonNull;

import java.util.List;

import ru.shtrm.serviceman.data.Alarm;
import ru.shtrm.serviceman.data.AlarmStatus;
import ru.shtrm.serviceman.data.source.AlarmRepository;

public class AlarmPresenter implements AlarmContract.Presenter {

    @NonNull
    private final AlarmContract.View view;

    @NonNull
    private final AlarmRepository alarmRepository;

    public AlarmPresenter(@NonNull AlarmContract.View view,
                          @NonNull AlarmRepository alarmRepository) {
        this.view = view;
        this.alarmRepository = alarmRepository;
        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {
        loadAlarms();
    }

    @Override
    public void unsubscribe() {
    }

    @Override
    public List<Alarm> loadAlarms() {
        List<Alarm> alarms = alarmRepository.getAlarms();
        view.showAlarms(alarms);
        return alarms;
    }

    @Override
    public List<Alarm> loadAlarmsByStatus(AlarmStatus alarmStatus){
        List<Alarm> alarms = alarmRepository.getAlarmsByStatus(alarmStatus);
        view.showAlarms(alarms);
        return alarms;
    }
}
