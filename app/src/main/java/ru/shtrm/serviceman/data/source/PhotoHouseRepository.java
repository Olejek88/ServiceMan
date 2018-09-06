package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.Measure;
import ru.shtrm.serviceman.data.PhotoHouse;

public class PhotoHouseRepository implements PhotoHouseDataSource {

    @Nullable
    private static PhotoHouseRepository INSTANCE = null;

    @NonNull
    private final PhotoHouseDataSource localDataSource;

    // Prevent direct instantiation
    private PhotoHouseRepository(@NonNull PhotoHouseDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static PhotoHouseRepository getInstance(@NonNull PhotoHouseDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new PhotoHouseRepository(localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public List<PhotoHouse> getPhotoByHouse(House house){
        return localDataSource.getPhotoByHouse(house);
    }

    @Override
    public List<PhotoHouse> getPhotosHouses() {
        return localDataSource.getPhotosHouses();
    }

    @Override
    public void savePhotoHouse(PhotoHouse photoHouse) {
        localDataSource.savePhotoHouse(photoHouse);
    }

    @Override
    public long getLastId() {
        return localDataSource.getLastId();
    }
}
