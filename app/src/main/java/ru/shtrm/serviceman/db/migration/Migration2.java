package ru.shtrm.serviceman.db.migration;

import android.util.Log;

import java.util.Date;

import io.realm.DynamicRealm;
import io.realm.RealmSchema;

public class Migration2 implements IAppMigration {

    @Override
    public void migration(DynamicRealm realm) {
        Log.d(this.getClass().getSimpleName(), "from first version");
        RealmSchema schema = realm.getSchema();
        schema.create("GpsTrack")
                .addField("_id", long.class)
                .addField("userUuid", String.class)
                .addField("date", Date.class)
                .addField("longitude", double.class)
                .addField("latitude", double.class)
                .addField("sent", boolean.class)
                .addPrimaryKey("_id");
    }
}
