package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import ru.shtrm.serviceman.data.AuthorizedUser;
import ru.shtrm.serviceman.data.Documentation;
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.source.DocumentationDataSource;


public class DocumentationLocalDataSource implements DocumentationDataSource {

    @Nullable
    private static DocumentationLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private DocumentationLocalDataSource() {

    }

    public static DocumentationLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DocumentationLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public List<Documentation> getDocumentations() {
        Realm realm = Realm.getDefaultInstance();
        List<Documentation> list = realm.where(Documentation.class)
                .equalTo("organization.uuid", AuthorizedUser.getInstance().getUser().getUuid())
                .findAllSorted("title");
        list = realm.copyFromRealm(list);
        realm.close();
        return list;
    }

    @Override
    public Documentation getDocumentation(String uuid) {
        Realm realm = Realm.getDefaultInstance();
        Documentation list = realm.where(Documentation.class)
                .equalTo("uuid", uuid).findFirst();
        if (list != null) {
            list = realm.copyFromRealm(list);
        }

        realm.close();
        return list;
    }

    @Override
    public Documentation getDocumentationByEquipment(String uuid) {
        Realm realm = Realm.getDefaultInstance();
        Documentation list = realm.where(Documentation.class)
                .equalTo("equipment.uuid", uuid).findFirst();
        if (list != null) {
            list = realm.copyFromRealm(list);
        } else {
            Equipment equipment = realm.where(Equipment.class).equalTo("uuid", uuid).findFirst();
            if (equipment != null) {
                list = realm.where(Documentation.class)
                        .equalTo("equipmentType.uuid", equipment.getEquipmentType().getUuid()).findFirst();
                if (list != null)
                    list = realm.copyFromRealm(list);
            }
        }

        realm.close();
        return list;
    }
}