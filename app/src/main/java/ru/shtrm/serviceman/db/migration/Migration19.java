package ru.shtrm.serviceman.db.migration;

import android.util.Log;

import java.util.Date;

import io.realm.DynamicRealm;
import io.realm.RealmSchema;

public class Migration19 implements IAppMigration {

    @Override
    public void migration(DynamicRealm realm) {
        Log.d(this.getClass().getSimpleName(), "from 18 version");
        RealmSchema schema = realm.getSchema();

        schema.create("Organization").addField("_id", long.class)
                .addField("uuid", String.class)
                .addField("title", String.class)
                .addPrimaryKey("_id");
        schema.get("User").addRealmObjectField("organization", schema.get("Organization"));

        schema.remove("Image");
        schema.create("ControlPointType");

        schema.create("ZhObjectStatus").addField("_id", long.class)
                .addField("uuid", String.class)
                .addField("title", String.class)
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");
        schema.create("ZhObjectType").addField("_id", long.class)
                .addField("uuid", String.class)
                .addField("title", String.class)
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");
        schema.create("ZhObject").addField("_id", long.class)
                .addField("uuid", String.class)
                .addRealmObjectField("organization", schema.get("Organization"))
                .addField("title", String.class)
                .addField("gisId", String.class)
                .addField("square", double.class)
                .addRealmObjectField("objectStatus", schema.get("ZhObjectStatus"))
                .addRealmObjectField("house", schema.get("House"))
                .addRealmObjectField("objectType", schema.get("ZhObjectType"))
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");
        schema.get("Alarm").addRealmObjectField("organization", schema.get("Organization"))
                .addRealmObjectField("object", schema.get("ZhObject"));
        ;
        schema.get("City").addRealmObjectField("organization", schema.get("Organization"))
                .addField("gisId", String.class);
        schema.create("ContragentType").addField("_id", long.class)
                .addField("uuid", String.class)
                .addField("title", String.class)
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");
        schema.create("Contragent").addField("_id", long.class)
                .addField("uuid", String.class)
                .addRealmObjectField("organization", schema.get("Organization"))
                .addField("gisId", String.class)
                .addField("title", String.class)
                .addField("address", String.class)
                .addField("phone", String.class)
                .addField("inn", String.class)
                .addField("account", String.class)
                .addField("director", String.class)
                .addField("email", String.class)
                .addRealmObjectField("contragentType", schema.get("ContragentType"))
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");
        schema.create("ContragentRegister").addField("_id", long.class)
                .addField("uuid", String.class)
                .addRealmObjectField("organization", schema.get("Organization"))
                .addRealmObjectField("contragent", schema.get("Contragent"))
                .addField("date", Date.class)
                .addField("description", String.class)
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");
        schema.create("MeasureType").addField("_id", long.class)
                .addField("uuid", String.class)
                .addField("title", String.class)
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");
        schema.get("Measure").addRealmObjectField("organization", schema.get("Organization"))
                .addRealmObjectField("measureType", schema.get("MeasureType"));
        schema.create("DefectType").addField("_id", long.class)
                .addField("uuid", String.class)
                .addField("title", String.class)
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");
        schema.create("Defect").addField("_id", long.class)
                .addField("uuid", String.class)
                .addRealmObjectField("organization", schema.get("Organization"))
                .addRealmObjectField("user", schema.get("User"))
                .addField("title", String.class)
                .addField("date", Date.class)
                .addRealmObjectField("task", schema.get("Task"))
                .addRealmObjectField("equipment", schema.get("Equipment"))
                .addRealmObjectField("defectType", schema.get("DefectType"))
                .addField("status", int.class)
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addField("sent", boolean.class)
                .addPrimaryKey("_id");
        schema.create("DocumentationType").addField("_id", long.class)
                .addField("uuid", String.class)
                .addField("title", String.class)
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");
        schema.get("Street").addRealmObjectField("organization", schema.get("Organization"))
                .addField("gisId", String.class);
        schema.get("HouseType").addRealmObjectField("organization", schema.get("Organization"))
                .addField("gisId", String.class);
        schema.get("House").addRealmObjectField("organization", schema.get("Organization"))
                .addField("gisId", String.class)
                .addField("latitude", double.class)
                .addField("longitude", double.class);
        schema.create("EquipmentSystem").addField("_id", long.class)
                .addField("uuid", String.class)
                .addField("title", String.class)
                .addField("titleUser", String.class)
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");
        schema.get("EquipmentType").addRealmObjectField("equipmentSystem", schema.get("EquipmentSystem"));
        schema.get("Journal").addField("type", String.class)
                .addField("title", String.class);
        schema.get("UserHouse").addRealmObjectField("organization", schema.get("Organization"));
        schema.get("Message").addRealmObjectField("organization", schema.get("Organization"))
                .renameField("user", "fromUser")
                .addRealmObjectField("toUser", schema.get("User"))
                .renameField("message", "text")
                .addField("status", int.class);
    }
}
