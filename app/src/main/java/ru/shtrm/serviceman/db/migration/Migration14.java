package ru.shtrm.serviceman.db.migration;

import android.util.Log;

import io.realm.DynamicRealm;
import io.realm.RealmSchema;

public class Migration14 implements IAppMigration {

    @Override
    public void migration(DynamicRealm realm) {
        Log.d(this.getClass().getSimpleName(), "from 13 version");
        RealmSchema schema = realm.getSchema();
        schema.get("Message").addField("sent", boolean.class);
        schema.get("PhotoMessage").addField("sent", boolean.class);
    }
}
