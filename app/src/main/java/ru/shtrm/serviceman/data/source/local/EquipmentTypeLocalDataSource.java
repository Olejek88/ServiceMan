package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import ru.shtrm.serviceman.data.AuthorizedUser;
import ru.shtrm.serviceman.data.EquipmentType;
import ru.shtrm.serviceman.data.source.EquipmentTypeDataSource;

public class EquipmentTypeLocalDataSource implements EquipmentTypeDataSource {

    @Nullable
    private static EquipmentTypeLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private EquipmentTypeLocalDataSource() {

    }

    public static EquipmentTypeLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EquipmentTypeLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public List<EquipmentType> getEquipmentTypes() {
        Realm realm = Realm.getDefaultInstance();
        List<EquipmentType> list = realm.where(EquipmentType.class)
                .equalTo("organization.uuid", AuthorizedUser.getInstance().getUser().getUuid())
                .findAllSorted("title");
        list = realm.copyFromRealm(list);
        realm.close();
        return list;
    }

    @Override
    public EquipmentType getEquipmentType(String uuid) {
        Realm realm = Realm.getDefaultInstance();
        EquipmentType list = realm.where(EquipmentType.class)
                .equalTo("uuid", uuid)
                .equalTo("organization.uuid", AuthorizedUser.getInstance().getUser().getUuid())
                .findFirst();
        list = realm.copyFromRealm(list);
        if (list != null) {
            list = realm.copyFromRealm(list);
        }

        realm.close();
        return list;
    }
}