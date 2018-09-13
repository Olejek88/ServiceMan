package ru.shtrm.serviceman.db.migration;

import android.util.Log;

import java.util.Date;

import io.realm.DynamicRealm;
import io.realm.RealmSchema;
import ru.shtrm.serviceman.data.User;

public class Migration6 implements IAppMigration {

    @Override
    public void migration(DynamicRealm realm) {
        Log.d(this.getClass().getSimpleName(), "from fifth version");
        RealmSchema schema = realm.getSchema();
        schema.create("UserHouse")
                .addField("_id", long.class)
                .addField("uuid", String.class)
                .addRealmObjectField("user", schema.get("User"))
                .addRealmObjectField("house", schema.get("House"))
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");

        schema.get("Flat").addField("inhabitants", int.class);
    }
}
