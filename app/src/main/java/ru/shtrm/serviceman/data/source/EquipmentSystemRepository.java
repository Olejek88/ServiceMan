package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import ru.shtrm.serviceman.data.EquipmentSystem;

public class EquipmentSystemRepository implements EquipmentSystemDataSource {

    @Nullable
    private static EquipmentSystemRepository INSTANCE = null;

    @NonNull
    private final EquipmentSystemDataSource localDataSource;

    // Prevent direct instantiation
    private EquipmentSystemRepository(@NonNull EquipmentSystemDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static EquipmentSystemRepository getInstance(@NonNull EquipmentSystemDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new EquipmentSystemRepository(localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public List<EquipmentSystem> getAllEquipmentSystems() {
        return localDataSource.getAllEquipmentSystems();
    }
}
