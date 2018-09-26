package ru.shtrm.serviceman.db.migration;

import android.util.Log;

import io.realm.DynamicRealm;
import io.realm.RealmSchema;

public class Migration15 implements IAppMigration {

    @Override
    public void migration(DynamicRealm realm) {
        Log.d(this.getClass().getSimpleName(), "from 14 version");
        RealmSchema schema = realm.getSchema();
        String[] classes = new String[]{
                "Message",
                "PhotoMessage",
        };
        for (String className : classes
                ) {
            schema.get(className).removePrimaryKey().addPrimaryKey("uuid").addIndex("_id");
        }
    }
}
