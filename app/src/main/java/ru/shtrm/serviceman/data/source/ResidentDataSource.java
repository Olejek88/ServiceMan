package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;

import ru.shtrm.serviceman.data.Resident;

public interface ResidentDataSource {

    Resident getResident(@NonNull String uuid);

    Resident getResidentByFlat(@NonNull String uuid);

}
