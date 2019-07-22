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
        schema.remove("ControlPointType");

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
                .addField("sent", boolean.class)
                .addPrimaryKey("uuid");
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
                .addPrimaryKey("uuid");
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
        schema.create("EquipmentRegisterType").addField("_id", long.class)
                .addField("uuid", String.class)
                .addField("title", String.class)
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");
        schema.create("EquipmentRegister").addField("_id", long.class)
                .addField("uuid", String.class)
                .addRealmObjectField("organization", schema.get("Organization"))
                .addRealmObjectField("equipment", schema.get("Equipment"))
                .addRealmObjectField("registerType", schema.get("EquipmentRegisterType"))
                .addRealmObjectField("user", schema.get("User"))
                .addField("date", Date.class)
                .addField("description", String.class)
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addField("sent", boolean.class)
                .addPrimaryKey("uuid");
        schema.create("ObjectContragent").addField("_id", long.class)
                .addField("uuid", String.class)
                .addRealmObjectField("organization", schema.get("Organization"))
                .addRealmObjectField("object", schema.get("ZhObject"))
                .addRealmObjectField("contragent", schema.get("Contragent"))
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");
        schema.create("TaskType").addField("_id", long.class)
                .addField("uuid", String.class)
                .addField("title", String.class)
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");
        schema.create("TaskVerdict").addField("_id", long.class)
                .addField("uuid", String.class)
                .addField("title", String.class)
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");
        schema.create("TaskUser").addField("_id", long.class)
                .addField("uuid", String.class)
                .addRealmObjectField("organization", schema.get("Organization"))
                .addRealmObjectField("user", schema.get("User"))
                .addRealmObjectField("task", schema.get("Task"))
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");
        schema.create("TaskTemplate").addField("_id", long.class)
                .addField("uuid", String.class)
                .addRealmObjectField("organization", schema.get("Organization"))
                .addField("title", String.class)
                .addField("description", String.class)
                .addField("normative", int.class)
                .addRealmObjectField("taskType", schema.get("TaskType"))
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");
        schema.create("RequestType").addField("_id", long.class)
                .addField("uuid", String.class)
                .addField("title", String.class)
                .addRealmObjectField("taskTemplate", schema.get("TaskTemplate"))
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");
        schema.create("RequestStatus").addField("_id", long.class)
                .addField("uuid", String.class)
                .addField("title", String.class)
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");
        schema.create("Request").addField("_id", long.class)
                .addField("uuid", String.class)
                .addRealmObjectField("organization", schema.get("Organization"))
                .addField("type", int.class)
                .addRealmObjectField("user", schema.get("User"))
                .addRealmObjectField("author", schema.get("User"))
                .addRealmObjectField("requestStatus", schema.get("RequestStatus"))
                .addRealmObjectField("requestType", schema.get("RequestType"))
                .addField("comment", String.class)
                .addField("verdict", String.class)
                .addField("result", String.class)
                .addRealmObjectField("equipment", schema.get("Equipment"))
                .addRealmObjectField("object", schema.get("ZhObject"))
                .addRealmObjectField("task", schema.get("Task"))
                .addField("closeDate", Date.class)
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");
        schema.create("TaskTemplateEquipmentType").addField("_id", long.class)
                .addField("uuid", String.class)
                .addRealmObjectField("taskTemplate", schema.get("TaskTemplate"))
                .addRealmObjectField("equipmentType", schema.get("EquipmentType"))
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");
        schema.create("TaskTemplateEquipment").addField("_id", long.class)
                .addField("uuid", String.class)
                .addRealmObjectField("taskTemplate", schema.get("TaskTemplate"))
                .addRealmObjectField("equipment", schema.get("Equipment"))
                .addField("period", String.class)
                .addField("lastDate", Date.class)
                .addField("nextDate", Date.class)
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");
        schema.create("UserSystem").addField("_id", long.class)
                .addField("uuid", String.class)
                .addRealmObjectField("organization", schema.get("Organization"))
                .addRealmObjectField("user", schema.get("User"))
                .addRealmObjectField("equipmentSystem", schema.get("EquipmentSystem"))
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");
        schema.get("Equipment")
                .addRealmObjectField("organization", schema.get("Organization"))
                .addField("title", String.class)
                .removeField("house")
                .addField("inputDate", Date.class)
                .addField("removeDate", Date.class)
                .addField("period", int.class)
                .addRealmObjectField("object", schema.get("ZhObject"))
                .removeField("sent")
                .removePrimaryKey()
                .addPrimaryKey("_id");
        schema.get("OperationTemplate")
                .addRealmObjectField("organization", schema.get("Organization"))
                .removeField("normative")
                .removePrimaryKey()
                .addPrimaryKey("_id");
        schema.get("Task")
                .addRealmObjectField("organization", schema.get("Organization"))
                .addRealmObjectField("author", schema.get("User"))
                .addRealmObjectField("taskVerdict", schema.get("TaskVerdict"))
                .addRealmObjectField("taskTemplate", schema.get("TaskTemplate"))
                .addField("taskDate", Date.class)
                .addField("deadlineDate", Date.class);
        schema.get("Operation")
                .addRealmObjectField("organization", schema.get("Organization"))
                .removePrimaryKey()
                .addPrimaryKey("_id");
        schema.create("TaskOperation").addField("_id", long.class)
                .addField("uuid", String.class)
                .addRealmObjectField("taskTemplate", schema.get("TaskTemplate"))
                .addRealmObjectField("operationTemplate", schema.get("OperationTemplate"))
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");
        schema.create("Photo")
                .addField("_id", long.class)
                .addField("uuid", String.class)
                .addRealmObjectField("object", schema.get("ZhObject"))
                .addRealmObjectField("user", schema.get("User"))
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("uuid");
        schema.create("Documentation")
                .addField("_id", long.class)
                .addField("uuid", String.class)
                .addRealmObjectField("equipment", schema.get("Equipment"))
                .addRealmObjectField("equipmentType", schema.get("EquipmentType"))
                .addRealmObjectField("documentationType", schema.get("DocumentationType"))
                .addField("title", String.class)
                .addField("path", String.class)
                .addField("createdAt", Date.class)
                .addField("changedAt", Date.class)
                .addPrimaryKey("_id");
    }
}
