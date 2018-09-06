package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;
import ru.shtrm.serviceman.data.Flat;
import ru.shtrm.serviceman.data.PhotoFlat;
import ru.shtrm.serviceman.data.PhotoHouse;
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
        RealmResults<PhotoFlat> photoFlats = realm.where(PhotoFlat.class).equalTo("flat.uuid", flat.getUuid()).
                findAllSorted("createdAt", Sort.DESCENDING);
        if (!photoFlats.isEmpty())
            return realm.copyFromRealm(photoFlats.first());
        else
            return null;
    }

    @Override
    public long getLastId() {
        Realm realm = Realm.getDefaultInstance();
        Number lastId = realm.where(PhotoFlat.class).max("_id");
        if (lastId == null) {
            lastId = 0;
        }
        realm.close();
        return lastId.longValue();
    }

}