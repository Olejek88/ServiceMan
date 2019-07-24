package ru.shtrm.serviceman.db.migration;

import android.util.Log;

import java.util.Date;

import io.realm.DynamicRealm;
import io.realm.RealmSchema;

public class Migration24 implements IAppMigration {

    @Override
    public void migration(DynamicRealm realm) {
        Log.d(this.getClass().getSimpleName(), "from 23 version");
        RealmSchema schema = realm.getSchema();

        schema.create("UpdateQuery").addField("_id", long.class)
                .addField("modelClass", String.class)
                .addField("modelUuid", String.class)
                .addField("attribute", String.class)
                .addField("value", String.class)
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");
    }
}
