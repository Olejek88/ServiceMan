package ru.shtrm.serviceman.db.migration;

import android.util.Log;

import io.realm.DynamicRealm;
import io.realm.RealmSchema;

public class Migration25 implements IAppMigration {

    @Override
    public void migration(DynamicRealm realm) {
        Log.d(this.getClass().getSimpleName(), "from 24 version");
        RealmSchema schema = realm.getSchema();

        schema.get("Request").removeField("user")
                .addRealmObjectField("contragent", realm.getSchema().get("Contragent"));
    }
}
