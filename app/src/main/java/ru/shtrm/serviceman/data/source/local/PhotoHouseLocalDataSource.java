package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import ru.shtrm.serviceman.data.GpsTrack;
import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.Measure;
import ru.shtrm.serviceman.data.PhotoHouse;
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
        List<PhotoHouse> photoHouses = realm.where(PhotoHouse.class).findAllSorted("createdAt");
        return realm.copyFromRealm(
                realm.where(PhotoHouse.class).equalTo("house.uuid", house.getUuid()).
                        findAllSorted("createdAt"));
    }

    @Override
    public List<PhotoHouse> getPhotosHouses() {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                realm.where(PhotoHouse.class).findAllSorted("createdAt"));
    }

    /**
     * Save a photo of house to database.
     * @param photoHouse The photo to save. See {@link PhotoHouse}
     */
    @Override
    public void savePhotoHouse(@NonNull final PhotoHouse photoHouse) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(photoHouse);
            }
        });
        realm.close();

    }

    @Override
    public long getLastId() {
        Realm realm = Realm.getDefaultInstance();
        Number lastId = realm.where(PhotoHouse.class).max("_id");
        if (lastId == null) {
            lastId = 0;
        }
        realm.close();
        return lastId.longValue();
    }

}