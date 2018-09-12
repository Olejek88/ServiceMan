package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import ru.shtrm.serviceman.data.EquipmentStatus;
import ru.shtrm.serviceman.data.source.EquipmentStatusDataSource;

public class EquipmentStatusLocalDataSource implements EquipmentStatusDataSource {

    @Nullable
    private static EquipmentStatusLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private EquipmentStatusLocalDataSource() {

    }

    public static EquipmentStatusLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EquipmentStatusLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public List<EquipmentStatus> getEquipmentStatuses() {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                realm.where(EquipmentStatus.class).findAllSorted("title"));
    }
}