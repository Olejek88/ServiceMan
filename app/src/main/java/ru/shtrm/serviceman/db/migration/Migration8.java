package ru.shtrm.serviceman.db.migration;

import android.util.Log;

import java.util.Date;

import io.realm.DynamicRealm;
import io.realm.RealmSchema;

public class Migration8 implements IAppMigration {

    @Override
    public void migration(DynamicRealm realm) {
        Log.d(this.getClass().getSimpleName(), "from seventh version");
        RealmSchema schema = realm.getSchema();
        schema.create("Message")
                .addField("_id", long.class)
                .addField("uuid", String.class)
                .addRealmObjectField("user", schema.get("User"))
                .addRealmObjectField("flat", schema.get("Flat"))
                .addField("message", String.class)
                .addField("date", Date.class)
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");

        schema.create("PhotoMessage")
                .addField("_id", long.class)
                .addField("uuid", String.class)
                .addRealmObjectField("message", schema.get("Message"))
                .addField("longitude", double.class)
                .addField("latitude", double.class)
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");
    }
}
