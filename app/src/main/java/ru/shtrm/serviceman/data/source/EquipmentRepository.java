package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.EquipmentType;
import ru.shtrm.serviceman.data.ZhObject;

public class EquipmentRepository implements EquipmentDataSource {

    @Nullable
    private static EquipmentRepository INSTANCE = null;

    @NonNull
    private final EquipmentDataSource localDataSource;

    // Prevent direct instantiation
    private EquipmentRepository(@NonNull EquipmentDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static EquipmentRepository getInstance(@NonNull EquipmentDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new EquipmentRepository(localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public Equipment getEquipmentByUuid(String uuid) {
        return localDataSource.getEquipmentByUuid(uuid);
    }

    @Override
    public List<Equipment> getAllEquipment()
    {
        return localDataSource.getAllEquipment();
    }

    @Override
    public List<Equipment> getEquipmentByObject(ZhObject object)
    {
        return localDataSource.getEquipmentByObject(object);
    }

    @Override
    public List<Equipment> getEquipmentByType(EquipmentType equipmentType) {
        return localDataSource.getEquipmentByType(equipmentType);
    }

    @Override
    public long getLastId() {
        return localDataSource.getLastId();
    }
}
