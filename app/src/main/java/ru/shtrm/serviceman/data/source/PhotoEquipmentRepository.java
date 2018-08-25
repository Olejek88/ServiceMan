package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.Measure;
import ru.shtrm.serviceman.data.PhotoEquipment;

public class PhotoEquipmentRepository implements PhotoEquipmentDataSource {

    @Nullable
    private static PhotoEquipmentRepository INSTANCE = null;

    @NonNull
    private final PhotoEquipmentDataSource localDataSource;

    // Prevent direct instantiation
    private PhotoEquipmentRepository(@NonNull PhotoEquipmentDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static PhotoEquipmentRepository getInstance(@NonNull PhotoEquipmentDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new PhotoEquipmentRepository(localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public List<PhotoEquipment> getPhotoByEquipment(Equipment equipment){
        return localDataSource.getPhotoByEquipment(equipment);
    }

    @Override
    public List<PhotoEquipment> getPhotoEquipment() {
        return localDataSource.getPhotoEquipment();
    }
}
