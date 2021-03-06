package ru.shtrm.serviceman.retrofit.serial;

import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Locale;

import ru.shtrm.serviceman.data.Photo;

public class PhotoSerializer implements JsonSerializer<Photo> {
    @Override
    public JsonObject serialize(Photo src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

        object.addProperty("uuid", src.getUuid());
        object.addProperty("objectUuid", src.getObjectUuid());
        object.addProperty("userUuid", src.getUser().getUuid());
        object.addProperty("latitude", src.getLatitude());
        object.addProperty("longitude", src.getLongitude());
        object.addProperty("createdAt", sdf.format(src.getCreatedAt()));
        object.addProperty("changedAt", sdf.format(src.getChangedAt()));

        return object;
    }
}
