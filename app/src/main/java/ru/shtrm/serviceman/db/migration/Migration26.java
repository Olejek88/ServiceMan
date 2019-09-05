package ru.shtrm.serviceman.db.migration;

import android.util.Log;

import io.realm.DynamicRealm;
import io.realm.RealmSchema;

public class Migration26 implements IAppMigration {

    @Override
    public void migration(DynamicRealm realm) {
        Log.d(this.getClass().getSimpleName(), "from 25 version");
        RealmSchema schema = realm.getSchema();

        schema.get("HouseType").removeField("organization");
        schema.get("RequestType")
                .addRealmObjectField("organization", realm.getSchema().get("Organization"));
    }
}
