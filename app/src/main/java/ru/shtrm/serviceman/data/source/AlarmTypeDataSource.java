package ru.shtrm.serviceman.data.source;

import java.util.List;

import ru.shtrm.serviceman.data.AlarmType;

public interface AlarmTypeDataSource {

    List<AlarmType> getAllAlarmTypes();
}
