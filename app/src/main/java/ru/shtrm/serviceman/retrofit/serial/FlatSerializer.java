package ru.shtrm.serviceman.retrofit.serial;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Locale;

import ru.shtrm.serviceman.data.Flat;

public class FlatSerializer implements JsonSerializer<Flat> {
    @Override
    public JsonElement serialize(Flat src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

        object.addProperty("uuid", src.getUuid());
        object.addProperty("number", src.getNumber());
        object.addProperty("houseUuid", src.getHouse().getUuid());
        object.addProperty("flatTypeUuid", src.getFlatType().getUuid());
        object.addProperty("flatStatusUuid", src.getFlatStatus().getUuid());
        object.addProperty("inhabitants", src.getInhabitants());
        object.addProperty("createdAt", sdf.format(src.getCreatedAt()));
        object.addProperty("changedAt", sdf.format(src.getChangedAt()));

        return object;
    }
}
