package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import ru.shtrm.serviceman.data.EquipmentType;

public class EquipmentTypeRepository implements EquipmentTypeDataSource {

    @Nullable
    private static EquipmentTypeRepository INSTANCE = null;

    @NonNull
    private final EquipmentTypeDataSource localDataSource;

    // Prevent direct instantiation
    private EquipmentTypeRepository(@NonNull EquipmentTypeDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static EquipmentTypeRepository getInstance(@NonNull EquipmentTypeDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new EquipmentTypeRepository(localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public List<EquipmentType> getEquipmentTypes() {
        return localDataSource.getEquipmentTypes();
    }

    @Override
    public EquipmentType getEquipmentType(String uuid) {
        return localDataSource.getEquipmentType(uuid);
    }
}
