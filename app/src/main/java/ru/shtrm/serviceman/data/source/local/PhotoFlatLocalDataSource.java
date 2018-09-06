package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import ru.shtrm.serviceman.data.Flat;
import ru.shtrm.serviceman.data.PhotoFlat;
import ru.shtrm.serviceman.data.source.PhotoFlatDataSource;

public class PhotoFlatLocalDataSource implements PhotoFlatDataSource {

    @Nullable
    private static PhotoFlatLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private PhotoFlatLocalDataSource() {

    }

    public static PhotoFlatLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PhotoFlatLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public List<PhotoFlat> getPhotoByFlat(Flat flat) {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                realm.where(PhotoFlat.class).equalTo("flat.uuid", flat.getUuid()).
                        findAllSorted("createdAt"));
    }

    @Override
    public List<PhotoFlat> getPhotosFlats() {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                realm.where(PhotoFlat.class).findAllSorted("createdAt"));
    }

    @Override
    public PhotoFlat getLastPhotoByFlat(Flat flat) {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                realm.where(PhotoFlat.class).equalTo("flat.uuid", flat.getUuid()).
                        findAllSorted("createdAt DESC").first());
        }
}