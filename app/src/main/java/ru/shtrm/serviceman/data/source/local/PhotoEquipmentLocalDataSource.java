package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.Flat;
import ru.shtrm.serviceman.data.PhotoEquipment;
import ru.shtrm.serviceman.data.PhotoFlat;
import ru.shtrm.serviceman.data.PhotoHouse;
import ru.shtrm.serviceman.data.source.PhotoEquipmentDataSource;

public class PhotoEquipmentLocalDataSource implements PhotoEquipmentDataSource {

    @Nullable
    private static PhotoEquipmentLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private PhotoEquipmentLocalDataSource() {

    }

    public static PhotoEquipmentLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PhotoEquipmentLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public List<PhotoEquipment> getPhotoByEquipment(Equipment equipment) {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                realm.where(PhotoEquipment.class).equalTo("equipment", equipment.getUuid()).
                        findAllSorted("createdAt"));
    }

    @Override
    public List<PhotoEquipment> getPhotoEquipment() {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                realm.where(PhotoEquipment.class).findAllSorted("createdAt"));
    }
    @Override
    public PhotoEquipment getLastPhotoByEquipment(Equipment equipment) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<PhotoEquipment> photoEquipments = realm.where(PhotoEquipment.class).
                equalTo("equipment.uuid", equipment.getUuid()).
                findAllSorted("createdAt", Sort.DESCENDING);
        if (!photoEquipments.isEmpty())
            return realm.copyFromRealm(photoEquipments.first());
        else
            return null;
    }


    /**
     * Save a photo of equipment to database.
     * @param photoEquipment The photo to save. See {@link PhotoEquipment}
     */
    @Override
    public void savePhotoEquipment(@NonNull final PhotoEquipment photoEquipment) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(photoEquipment);
            }
        });
        realm.close();

    }

    @Override
    public long getLastId() {
        Realm realm = Realm.getDefaultInstance();
        Number lastId = realm.where(PhotoEquipment.class).max("_id");
        if (lastId == null) {
            lastId = 0;
        }
        realm.close();
        return lastId.longValue();
    }
}