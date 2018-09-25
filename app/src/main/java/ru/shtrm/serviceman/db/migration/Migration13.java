package ru.shtrm.serviceman.db.migration;

import android.util.Log;

import io.realm.DynamicRealm;
import io.realm.RealmSchema;

public class Migration13 implements IAppMigration {

    @Override
    public void migration(DynamicRealm realm) {
        Log.d(this.getClass().getSimpleName(), "from 11 version");
        RealmSchema schema = realm.getSchema();
        schema.get("Flat").renameField("title", "number");
        schema.get("House").renameField("title", "number");
    }
}
