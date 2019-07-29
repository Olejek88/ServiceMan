package ru.shtrm.serviceman.retrofit.serial;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Locale;

import ru.shtrm.serviceman.data.Equipment;

public class EquipmentSerializer implements JsonSerializer<Equipment> {
    @Override
    public JsonElement serialize(Equipment src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

        object.addProperty("uuid", src.getUuid());
        object.addProperty("equipmentTypeUuid", src.getEquipmentType().getUuid());
        object.addProperty("equipmentStatusUuid", src.getEquipmentStatus().getUuid());
        object.addProperty("serial", src.getSerial());
        object.addProperty("testDate", sdf.format(src.getTestDate()));
        object.addProperty("createdAt", sdf.format(src.getCreatedAt()));
        object.addProperty("changedAt", sdf.format(src.getChangedAt()));

        return object;
    }
}
