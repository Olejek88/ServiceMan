package ru.shtrm.serviceman.retrofit.deserial;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import io.realm.Realm;
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.EquipmentStatus;
import ru.shtrm.serviceman.data.EquipmentType;
import ru.shtrm.serviceman.data.Organization;
import ru.shtrm.serviceman.data.ZhObject;

public class EquipmentDeserializer extends BaseDeserialzer implements JsonDeserializer<Equipment> {

    @Override
    public Equipment deserialize(JsonElement jsonElement, Type typeOF,
                                 JsonDeserializationContext context) throws JsonParseException {

        Equipment item = new Equipment();
        JsonObject object = jsonElement.getAsJsonObject();

        item.set_id(getLong(object, "_id"));
        item.setUuid(getString(object, "uuid"));
        item.setOrganization((Organization) getReference(object, Organization.class, "oid"));
        item.setTitle(getString(object, "title"));
        item.setEquipmentType((EquipmentType) getReference(object, EquipmentType.class, "equipmentTypeUuid"));
        item.setSerial(getString(object, "serial"));
        item.setTag(getString(object, "tag"));
        item.setEquipmentStatus((EquipmentStatus) getReference(object, EquipmentStatus.class, "equipmentStatusUuid"));
        item.setInputDate(getDate(object, "inputDate"));
        item.setTestDate(getDate(object, "testDate"));
        int period = getInt(object, "period", true);
        item.setPeriod(period == -1 ? 0 : period);
        item.setReplaceDate(getDate(object, "replaceDate"));
        item.setObject((ZhObject) getReference(object, ZhObject.class, "objectUuid"));
        item.setCreatedAt(getDate(object, "createdAt"));
        item.setChangedAt(getDate(object, "changedAt"));

        return item;
    }
}
