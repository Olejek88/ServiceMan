package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;

import java.util.List;

import ru.shtrm.serviceman.data.Alarm;
import ru.shtrm.serviceman.data.AlarmStatus;
import ru.shtrm.serviceman.data.AlarmType;
import ru.shtrm.serviceman.data.User;

public interface AlarmDataSource {

    List<Alarm> getAlarms();

    List<Alarm> getAlarmsByType(AlarmType alarmType);

    List<Alarm> getAlarmsByStatus(AlarmStatus alarmStatus);

    Alarm getAlarm(@NonNull String uuid);
}
