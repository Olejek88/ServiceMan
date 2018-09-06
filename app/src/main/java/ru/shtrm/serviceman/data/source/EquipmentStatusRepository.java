package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.EquipmentStatus;
import ru.shtrm.serviceman.data.FlatStatus;

public class EquipmentStatusRepository implements EquipmentStatusDataSource {

    @Nullable
    private static EquipmentStatusRepository INSTANCE = null;

    @NonNull
    private final EquipmentStatusDataSource localDataSource;

    // Prevent direct instantiation
    private EquipmentStatusRepository(@NonNull EquipmentStatusDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static EquipmentStatusRepository getInstance(@NonNull EquipmentStatusDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new EquipmentStatusRepository(localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public List<EquipmentStatus> getEquipmentStatuses() {
        return localDataSource.getEquipmentStatuses();
    }
}
