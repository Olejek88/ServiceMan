package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.realm.Realm;
import ru.shtrm.serviceman.data.Resident;
import ru.shtrm.serviceman.data.source.ResidentDataSource;

public class ResidentLocalDataSource implements ResidentDataSource {

    @Nullable
    private static ResidentLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private ResidentLocalDataSource() {

    }

    public static ResidentLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ResidentLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public Resident getResident(@NonNull String uuid) {
        Realm realm = Realm.getDefaultInstance();
        Resident resident = realm.where(Resident.class).equalTo("uuid", uuid).findFirst();
        if (resident!=null)
            return realm.copyFromRealm(resident);
        else
            return null;
    }

    @Override
    public Resident getResidentByFlat(@NonNull String uuid) {
        Realm realm = Realm.getDefaultInstance();
        Resident resident = realm.where(Resident.class).equalTo("flat.uuid", uuid).findFirst();
        if (resident!=null)
            return realm.copyFromRealm(resident);
        else
            return null;
    }
}