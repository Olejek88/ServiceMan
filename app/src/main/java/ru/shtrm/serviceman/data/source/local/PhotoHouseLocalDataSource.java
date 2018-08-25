package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.PhotoEquipment;
import ru.shtrm.serviceman.data.PhotoHouse;
import ru.shtrm.serviceman.data.source.PhotoEquipmentDataSource;
import ru.shtrm.serviceman.data.source.PhotoHouseDataSource;

public class PhotoHouseLocalDataSource implements PhotoHouseDataSource {

    @Nullable
    private static PhotoHouseLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private PhotoHouseLocalDataSource() {

    }

    public static PhotoHouseLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PhotoHouseLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public List<PhotoHouse> getPhotoByHouse(House house) {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                realm.where(PhotoHouse.class).equalTo("house", house.getUuid()).
                        findAllSorted("date"));
    }

    @Override
    public List<PhotoHouse> getPhotosHouses() {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                realm.where(PhotoHouse.class).findAllSorted("date"));
    }
}