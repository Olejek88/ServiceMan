package ru.shtrm.serviceman.db.migration;

import android.util.Log;

import java.util.Date;

import io.realm.DynamicRealm;
import io.realm.RealmSchema;

public class Migration20 implements IAppMigration {

    @Override
    public void migration(DynamicRealm realm) {
        Log.d(this.getClass().getSimpleName(), "from 19 version");
        RealmSchema schema = realm.getSchema();

        schema.get("Photo").addField("latitude", double.class)
                .addField("longitude", double.class)
                .addField("sent", boolean.class)
                .addIndex("_id");

        schema.get("ContragentRegister").addIndex("_id");

        schema.get("Defect").addIndex("_id");

        schema.get("EquipmentRegister").addIndex("_id");

        schema.get("Equipment").renameField("removeDate", "replaceDate");

        schema.get("Task")
                .removePrimaryKey()
                .addPrimaryKey("_id");
    }
}
