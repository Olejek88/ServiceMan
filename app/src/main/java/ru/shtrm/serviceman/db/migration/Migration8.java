package ru.shtrm.serviceman.db.migration;

import android.util.Log;
import io.realm.DynamicRealm;
import io.realm.RealmSchema;

public class Migration8 implements IAppMigration {

    @Override
    public void migration(DynamicRealm realm) {
        Log.d(this.getClass().getSimpleName(), "from 7 version");
        RealmSchema schema = realm.getSchema();
        schema.get("");
    }
}