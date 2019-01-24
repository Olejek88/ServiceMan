package ru.shtrm.serviceman.db.migration;

import android.util.Log;

import java.util.Date;

import io.realm.DynamicRealm;
import io.realm.RealmSchema;

public class Migration17 implements IAppMigration {

    @Override
    public void migration(DynamicRealm realm) {
        Log.d(this.getClass().getSimpleName(), "from 16 version");
        RealmSchema schema = realm.getSchema();
        String[] classes = new String[]{
                "WorkStatus",
                "OperationTemplate",
                "Task",
                "Operation"
        };
        for (String className : classes
                ) {
            schema.get(className).removePrimaryKey().addPrimaryKey("uuid").addIndex("_id");
        }
    }
}
