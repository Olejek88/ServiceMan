package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.Flat;
import ru.shtrm.serviceman.data.Measure;
import ru.shtrm.serviceman.data.PhotoEquipment;
import ru.shtrm.serviceman.data.PhotoFlat;
import ru.shtrm.serviceman.data.PhotoHouse;

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

    @Override
    public PhotoEquipment getLastPhotoByEquipment(Equipment equipment) {
        return localDataSource.getLastPhotoByEquipment(equipment);
    }

    @Override
    public void savePhotoEquipment(PhotoEquipment photoEquipment) {
        localDataSource.savePhotoEquipment(photoEquipment);
    }

    @Override
    public long getLastId() {
        return localDataSource.getLastId();
    }
}
