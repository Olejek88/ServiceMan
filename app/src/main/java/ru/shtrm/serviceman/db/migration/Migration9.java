package ru.shtrm.serviceman.db.migration;

import android.util.Log;
import io.realm.DynamicRealm;
import io.realm.RealmSchema;

public class Migration9 implements IAppMigration {

    @Override
    public void migration(DynamicRealm realm) {
        Log.d(this.getClass().getSimpleName(), "from 8 version");
        RealmSchema schema = realm.getSchema();
        schema.get("Alarm").addField("sent", boolean.class);
        schema.get("Equipment").addField("sent", boolean.class);
        schema.get("Measure").addField("sent", boolean.class);
        schema.get("PhotoAlarm").addField("sent", boolean.class);
        schema.get("PhotoEquipment").addField("sent", boolean.class);
        schema.get("PhotoFlat").addField("sent", boolean.class);
        schema.get("PhotoHouse").addField("sent", boolean.class);
    }
}