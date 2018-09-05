package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import ru.shtrm.serviceman.data.Alarm;
import ru.shtrm.serviceman.data.AlarmStatus;
import ru.shtrm.serviceman.data.AlarmType;
import ru.shtrm.serviceman.data.Resident;

public class ResidentRepository implements ResidentDataSource {

    @Nullable
    private static ResidentRepository INSTANCE = null;

    @NonNull
    private final ResidentDataSource localDataSource;

    // Prevent direct instantiation
    private ResidentRepository(@NonNull ResidentDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static ResidentRepository getInstance(@NonNull ResidentDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new ResidentRepository(localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public Resident getResident(@NonNull String uuid) {
        return localDataSource.getResident(uuid);
    }

    @Override
    public Resident getResidentByFlat(@NonNull String uuid) {
        return localDataSource.getResidentByFlat(uuid);
    }

}
