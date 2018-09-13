package ru.shtrm.serviceman.db.migration;

import android.util.Log;

import java.util.Date;

import io.realm.DynamicRealm;
import io.realm.RealmSchema;

public class Migration7 implements IAppMigration {

    @Override
    public void migration(DynamicRealm realm) {
        Log.d(this.getClass().getSimpleName(), "from sixth version");
        RealmSchema schema = realm.getSchema();
        schema.get("Subject").addRealmObjectField("flat", schema.get("Flat"));
    }
}
