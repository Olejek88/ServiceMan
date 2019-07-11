package ru.shtrm.serviceman.db.migration;

import android.util.Log;

import io.realm.DynamicRealm;
import io.realm.RealmSchema;

public class Migration18 implements IAppMigration {

    @Override
    public void migration(DynamicRealm realm) {
        Log.d(this.getClass().getSimpleName(), "from 17 version");
        RealmSchema schema = realm.getSchema();

        schema.get("Equipment").removeField("flat");
        schema.get("Message").removeField("flat");
        schema.get("Task").removeField("flat");
        schema.get("PhotoAlarm").removeField("flat");

        schema.remove("Resident");
        schema.remove("Subject");
        schema.remove("PhotoFlat");

        schema.remove("Flat");
        schema.remove("FlatStatus");
        schema.remove("FlatType");
    }
}
