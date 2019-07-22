package ru.shtrm.serviceman.db.migration;

import android.util.Log;

import io.realm.DynamicRealm;
import io.realm.RealmSchema;

public class Migration23 implements IAppMigration {

    @Override
    public void migration(DynamicRealm realm) {
        Log.d(this.getClass().getSimpleName(), "from 22 version");
        RealmSchema schema = realm.getSchema();

        schema.get("Task").addRealmListField("operations", schema.get("Operation"));
        schema.get("Operation").removeField("task").addField("taskUuid", String.class);
    }
}
