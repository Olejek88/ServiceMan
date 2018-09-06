package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import io.realm.Sort;
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.EquipmentStatus;
import ru.shtrm.serviceman.data.EquipmentType;
import ru.shtrm.serviceman.data.Flat;
import ru.shtrm.serviceman.data.FlatStatus;
import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.source.EquipmentDataSource;

public class EquipmentLocalDataSource implements EquipmentDataSource {

    @Nullable
    private static EquipmentLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private EquipmentLocalDataSource() {

    }

    public static EquipmentLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EquipmentLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public Equipment getEquipmentByUuid(String uuid) {
        Realm realm = Realm.getDefaultInstance();
        Equipment equipment = realm.where(Equipment.class).equalTo("uuid", uuid).findFirst();
        if (equipment!=null)
            return realm.copyFromRealm(equipment);
        else
            return null;
    }

    @Override
    public List<Equipment> getAllEquipment() {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                realm.where(Equipment.class).findAllSorted("equipmentType", Sort.ASCENDING));
    }

    @Override
    public List<Equipment> getEquipmentByHouse(House house) {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                realm.where(Equipment.class).equalTo("house", house.getUuid()).findAll());
    }

    @Override
    public List<Equipment> getEquipmentByFlat(Flat flat) {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                realm.where(Equipment.class).equalTo("flat", flat.getUuid()).findAll());
    }

    @Override
    public List<Equipment> getEquipmentByType(EquipmentType equipmentType) {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                realm.where(Equipment.class).
                        equalTo("equipmentType", equipmentType.getUuid()).findAll());
    }

    @Override
    public void addEquipment(final Equipment equipment) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(equipment);
            }
        });
        realm.close();
    }

    @Override
    public void updateEquipmentStatus(final Equipment equipment, final EquipmentStatus equipmentStatus) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                equipment.setEquipmentStatus(equipmentStatus);
                realm.copyToRealmOrUpdate(equipment);
            }
        });
        realm.close();
    }
}