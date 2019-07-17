package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import ru.shtrm.serviceman.data.EquipmentSystem;
import ru.shtrm.serviceman.data.source.EquipmentSystemDataSource;

public class EquipmentSystemLocalDataSource implements EquipmentSystemDataSource {

    @Nullable
    private static EquipmentSystemLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private EquipmentSystemLocalDataSource() {

    }

    public static EquipmentSystemLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EquipmentSystemLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public List<EquipmentSystem> getAllEquipmentSystems() {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                realm.where(EquipmentSystem.class).findAllSorted("title"));
    }
}