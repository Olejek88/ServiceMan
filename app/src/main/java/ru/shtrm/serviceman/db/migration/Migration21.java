package ru.shtrm.serviceman.db.migration;

import android.util.Log;

import io.realm.DynamicRealm;
import io.realm.RealmSchema;

public class Migration21 implements IAppMigration {

    @Override
    public void migration(DynamicRealm realm) {
        Log.d(this.getClass().getSimpleName(), "from 20 version");
        RealmSchema schema = realm.getSchema();

        schema.get("Photo")
                .removeField("object")
                .addField("objectUuid", String.class);

        schema.remove("PhotoAlarm");
        schema.remove("PhotoEquipment");
        schema.remove("PhotoHouse");
        schema.remove("PhotoMessage");
    }
}
