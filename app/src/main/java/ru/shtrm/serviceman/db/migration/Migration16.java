package ru.shtrm.serviceman.db.migration;

import android.util.Log;

import java.util.Date;

import io.realm.DynamicRealm;
import io.realm.RealmSchema;

public class Migration16 implements IAppMigration {

    @Override
    public void migration(DynamicRealm realm) {
        Log.d(this.getClass().getSimpleName(), "from 15 version");
        RealmSchema schema = realm.getSchema();

        schema.get("Equipment").addField("tag", String.class);

        schema.create("WorkStatus")
                .addField("_id", long.class)
                .addField("uuid", String.class)
                .addField("title", String.class)
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");

        schema.create("OperationTemplate")
                .addField("_id", long.class)
                .addField("uuid", String.class)
                .addField("title", String.class)
                .addField("description", String.class)
                .addField("normative", int.class)
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");

        schema.create("Task")
                .addField("_id", long.class)
                .addField("uuid", String.class)
                .addField("comment", String.class)
                .addRealmObjectField("flat", schema.get("Flat"))
                .addRealmObjectField("workStatus", schema.get("WorkStatus"))
                .addRealmObjectField("equipment", schema.get("Equipment"))
                .addField("startDate", Date.class)
                .addField("endDate", Date.class)
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");

        schema.create("Operation")
                .addField("_id", long.class)
                .addField("uuid", String.class)
                .addRealmObjectField("task", schema.get("Task"))
                .addRealmObjectField("workStatus", schema.get("WorkStatus"))
                .addRealmObjectField("operationTemplate", schema.get("OperationTemplate"))
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");
    }
}
