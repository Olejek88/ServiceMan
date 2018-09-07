package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import ru.shtrm.serviceman.data.EquipmentStatus;
import ru.shtrm.serviceman.data.EquipmentType;
import ru.shtrm.serviceman.data.source.EquipmentStatusDataSource;
import ru.shtrm.serviceman.data.source.EquipmentTypeDataSource;

public class EquipmentTypeLocalDataSource implements EquipmentTypeDataSource {

    @Nullable
    private static EquipmentTypeLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private EquipmentTypeLocalDataSource() {

    }

    public static EquipmentTypeLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EquipmentTypeLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public List<EquipmentType> getEquipmentTypes() {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                realm.where(EquipmentType.class).findAllSorted("title"));
    }
}