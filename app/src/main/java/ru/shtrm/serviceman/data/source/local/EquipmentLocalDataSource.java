package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.EquipmentStatus;
import ru.shtrm.serviceman.data.EquipmentType;
import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.ZhObject;
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
        if (equipment != null) {
            equipment = realm.copyFromRealm(equipment);
        }

        realm.close();
        return equipment;
    }

    @Override
    public List<Equipment> getAllEquipment() {
        Realm realm = Realm.getDefaultInstance();
        List<Equipment> list = realm.where(Equipment.class).findAllSorted("equipmentType", Sort.ASCENDING);
        list = realm.copyFromRealm(list);
        realm.close();
        return list;
    }

    @Override
    public List<Equipment> getEquipmentByObject(ZhObject object) {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                realm.where(Equipment.class).
                        equalTo("object.uuid", object.getUuid()).findAll());
    }

    @Override
    public List<Equipment> getEquipmentByType(EquipmentType equipmentType) {
        Realm realm = Realm.getDefaultInstance();
        List<Equipment> list = realm.where(Equipment.class)
                .equalTo("equipmentType", equipmentType.getUuid()).findAll();
        list = realm.copyFromRealm(list);
        realm.close();
        return list;
    }

    @Override
    public long getLastId() {
        Realm realm = Realm.getDefaultInstance();
        Number lastId = realm.where(Equipment.class).max("_id");
        if (lastId == null) {
            lastId = 0;
        }
        realm.close();
        return lastId.longValue();
    }
}
