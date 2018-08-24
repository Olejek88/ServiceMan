package ru.shtrm.serviceman.db.migration;

import android.util.Log;

import java.util.Date;

import io.realm.DynamicRealm;
import io.realm.RealmSchema;

public class Migration1 implements IAppMigration {

    @Override
    public void migration(DynamicRealm realm) {
        Log.d(this.getClass().getSimpleName(), "from zero version");
        RealmSchema schema = realm.getSchema();
        schema.create("City")
                .addField("_id", long.class)
                .addField("uuid", String.class)
                .addField("title", String.class)
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");

        schema.create("Street")
                .addField("_id", long.class)
                .addField("uuid", String.class)
                .addField("title", String.class)
                .addRealmObjectField("city", schema.get("City"))
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");

        schema.create("HouseStatus")
                .addField("_id", long.class)
                .addField("uuid", String.class)
                .addField("title", String.class)
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");

        schema.create("FlatStatus")
                .addField("_id", long.class)
                .addField("uuid", String.class)
                .addField("title", String.class)
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");

        schema.create("House")
                .addField("_id", long.class)
                .addField("uuid", String.class)
                .addField("number", String.class)
                .addRealmObjectField("houseStatus", schema.get("HouseStatus"))
                .addRealmObjectField("street", schema.get("Street"))
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");

        schema.create("Flat")
                .addField("_id", long.class)
                .addField("uuid", String.class)
                .addField("number", String.class)
                .addRealmObjectField("flatStatus", schema.get("FlatStatus"))
                .addRealmObjectField("house", schema.get("House"))
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");

        schema.create("Resident")
                .addField("_id", long.class)
                .addField("uuid", String.class)
                .addField("owner", String.class)
                .addRealmObjectField("flat", schema.get("Flat"))
                .addField("inn", String.class)
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");

        schema.create("Subject")
                .addField("_id", long.class)
                .addField("uuid", String.class)
                .addRealmObjectField("house", schema.get("House"))
                .addField("contractNumber", String.class)
                .addField("contractDate", Date.class)
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");

        schema.create("AlarmType")
                .addField("_id", long.class)
                .addField("uuid", String.class)
                .addField("title", String.class)
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");

        schema.create("AlarmStatus")
                .addField("_id", long.class)
                .addField("uuid", String.class)
                .addField("title", String.class)
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");

        schema.create("User")
                .addField("_id", long.class)
                .addField("uuid", String.class)
                .addField("name", String.class)
                .addField("pin", String.class)
                .addField("image", String.class)
                .addField("contact", String.class)
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");

        schema.create("Alarm")
                .addField("_id", long.class)
                .addField("uuid", String.class)
                .addRealmObjectField("type", schema.get("AlarmType"))
                .addRealmObjectField("status", schema.get("AlarmStatus"))
                .addRealmObjectField("user", schema.get("User"))
                .addField("longitude", double.class)
                .addField("latitude", double.class)
                .addField("comment", String.class)
                .addField("date", Date.class)
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");

        schema.create("EquipmentType")
                .addField("_id", long.class)
                .addField("uuid", String.class)
                .addField("title", String.class)
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");

        schema.create("EquipmentStatus")
                .addField("_id", long.class)
                .addField("uuid", String.class)
                .addField("title", String.class)
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");

        schema.create("Equipment")
                .addField("_id", long.class)
                .addField("uuid", String.class)
                .addRealmObjectField("house", schema.get("House"))
                .addRealmObjectField("flat", schema.get("Flat"))
                .addRealmObjectField("equipmentType", schema.get("EquipmentType"))
                .addRealmObjectField("equipmentStatus", schema.get("EquipmentStatus"))
                .addField("serial", String.class)
                .addField("testDate", Date.class)
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");

        schema.create("ControlPointType")
                .addField("_id", long.class)
                .addField("uuid", String.class)
                .addField("title", String.class)
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");

        schema.create("PhotoHouse")
                .addField("_id", long.class)
                .addField("uuid", String.class)
                .addRealmObjectField("house", schema.get("House"))
                .addRealmObjectField("user", schema.get("User"))
                .addField("longitude", double.class)
                .addField("latitude", double.class)
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");

        schema.create("PhotoFlat")
                .addField("_id", long.class)
                .addField("uuid", String.class)
                .addRealmObjectField("flat", schema.get("Flat"))
                .addRealmObjectField("user", schema.get("User"))
                .addField("longitude", double.class)
                .addField("latitude", double.class)
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");

        schema.create("PhotoEquipment")
                .addField("_id", long.class)
                .addField("uuid", String.class)
                .addRealmObjectField("equipment", schema.get("Equipment"))
                .addRealmObjectField("user", schema.get("User"))
                .addField("longitude", double.class)
                .addField("latitude", double.class)
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");

        schema.create("PhotoAlert")
                .addField("_id", long.class)
                .addField("uuid", String.class)
                .addRealmObjectField("alert", schema.get("Alert"))
                .addRealmObjectField("user", schema.get("User"))
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");

        schema.create("Measure")
                .addField("_id", long.class)
                .addField("uuid", String.class)
                .addRealmObjectField("equipment", schema.get("Equipment"))
                .addRealmObjectField("user", schema.get("User"))
                .addField("date", Date.class)
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");
    }
}
