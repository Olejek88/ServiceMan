package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;

import java.util.List;

import ru.shtrm.serviceman.data.Alarm;
import ru.shtrm.serviceman.data.AlarmStatus;
import ru.shtrm.serviceman.data.AlarmType;
import ru.shtrm.serviceman.data.Resident;

public interface ResidentDataSource {

    Resident getResident(@NonNull String uuid);

    Resident getResidentByFlat(@NonNull String uuid);

}
