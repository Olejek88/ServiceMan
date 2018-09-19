package ru.shtrm.serviceman.db.migration;

import android.util.Log;

import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.util.MainUtil;

public class Migration8 implements IAppMigration {

    @Override
    public void migration(DynamicRealm realm) {
        Log.d(this.getClass().getSimpleName(), "from 7 version");
        RealmSchema schema = realm.getSchema();
        schema.rename("PhotoAlert", "PhotoAlarm");
        String[] classes = new String[]{
                "Alarm",
                "Equipment",
                "PhotoAlarm",
                "PhotoEquipment",
                "PhotoFlat",
                "PhotoHouse",
                "Measure",
        };
        for (String className : classes
                ) {
            schema.get(className).removePrimaryKey().addPrimaryKey("uuid").addIndex("_id");
        }
    }
}