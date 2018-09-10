package ru.shtrm.serviceman.db.migration;

import android.util.Log;

import java.util.Date;

import io.realm.DynamicRealm;
import io.realm.RealmSchema;

public class Migration5 implements IAppMigration {

    @Override
    public void migration(DynamicRealm realm) {
        Log.d(this.getClass().getSimpleName(), "from fourth version");
        RealmSchema schema = realm.getSchema();
        schema.get("Flat").addRealmObjectField("flatType", schema.get("FlatType"));
        schema.get("House").addRealmObjectField("houseType", schema.get("HouseType"));
    }
}
