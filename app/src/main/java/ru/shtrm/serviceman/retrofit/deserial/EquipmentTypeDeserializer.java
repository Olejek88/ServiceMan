package ru.shtrm.serviceman.retrofit.deserial;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;

import ru.shtrm.serviceman.data.EquipmentSystem;
import ru.shtrm.serviceman.data.EquipmentType;

public class EquipmentTypeDeserializer extends BaseDeserialzer
        implements JsonDeserializer<EquipmentType> {

    @Override
    public EquipmentType deserialize(JsonElement jsonElement, Type typeOF,
                                     JsonDeserializationContext context) {

        EquipmentType item = new EquipmentType();
        JsonObject object = jsonElement.getAsJsonObject();

        item.set_id(getLong(object, "_id"));
        item.setUuid(getString(object, "uuid"));
        item.setTitle(getString(object, "title"));
        item.setEquipmentSystem((EquipmentSystem) getReference(object, EquipmentSystem.class,
                "equipmentSystemUuid"));
        item.setCreatedAt(getDate(object, "createdAt"));
        item.setChangedAt(getDate(object, "changedAt"));

        return item;
    }
}
