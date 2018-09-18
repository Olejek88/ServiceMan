package ru.shtrm.serviceman.db.migration;

import android.util.Log;

import java.util.Date;

import io.realm.DynamicRealm;
import io.realm.RealmSchema;

public class Migration9 implements IAppMigration {

    @Override
    public void migration(DynamicRealm realm) {
        Log.d(this.getClass().getSimpleName(), "from 8 version");
        RealmSchema schema = realm.getSchema();

        schema.create("ReferenceUpdate")
                .addField("referenceName", String.class)
                .addField("updateDate", Date.class)
                .addPrimaryKey("referenceName");
        schema.create("Journal")
                .addField("_id", long.class)
                .addField("userUuid", String.class)
                .addField("description", String.class)
                .addField("date", Date.class)
                .addField("sent", boolean.class)
                .addPrimaryKey("_id");
    }
}