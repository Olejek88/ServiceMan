package ru.shtrm.serviceman.data.source;

import java.util.List;

import ru.shtrm.serviceman.data.AlarmStatus;

public interface AlarmStatusDataSource {

    List<AlarmStatus> getAllAlarmStatus();
}
