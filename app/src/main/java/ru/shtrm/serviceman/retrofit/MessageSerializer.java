package ru.shtrm.serviceman.retrofit;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Locale;

import ru.shtrm.serviceman.data.Message;

public class MessageSerializer implements JsonSerializer<Message> {
    @Override
    public JsonElement serialize(Message src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

        object.addProperty("uuid", src.getUuid());
        object.addProperty("flatUuid", src.getFlat().getUuid());
        object.addProperty("userUuid", src.getUser().getUuid());
        object.addProperty("date", sdf.format(src.getDate()));
        object.addProperty("message", src.getMessage());
        object.addProperty("createdAt", sdf.format(src.getCreatedAt()));
        object.addProperty("changedAt", sdf.format(src.getChangedAt()));

        return object;
    }
}
