package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import ru.shtrm.serviceman.data.Equipment;
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
        return realm.copyFromRealm(
                realm.where(PhotoEquipment.class).equalTo("equipment.uuid", equipment.getUuid()).
                        findAllSorted("createdAt DESC").first());
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

}