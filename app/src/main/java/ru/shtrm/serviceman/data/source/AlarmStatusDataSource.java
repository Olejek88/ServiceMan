package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;

import java.util.List;

import ru.shtrm.serviceman.data.Alarm;
import ru.shtrm.serviceman.data.AlarmStatus;
import ru.shtrm.serviceman.data.AlarmType;

public interface AlarmStatusDataSource {

    List<AlarmStatus> getAllAlarmStatus();
}
