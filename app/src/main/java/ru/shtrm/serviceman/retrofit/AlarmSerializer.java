package ru.shtrm.serviceman.retrofit;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Locale;

import ru.shtrm.serviceman.data.Alarm;

public class AlarmSerializer implements JsonSerializer<Alarm> {
    @Override
    public JsonElement serialize(Alarm src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

        object.addProperty("uuid", src.getUuid());
        object.addProperty("alarmType", src.getAlarmType().getUuid());
        object.addProperty("alarmStatus", src.getAlarmStatus().getUuid());
        object.addProperty("user", src.getUser().getUuid());
        object.addProperty("latitude", src.getLatitude());
        object.addProperty("longitude", src.getLongitude());
        object.addProperty("date", sdf.format(src.getDate()));
        object.addProperty("comment", src.getComment());
        object.addProperty("createdAt", sdf.format(src.getCreatedAt()));
        object.addProperty("changedAt", sdf.format(src.getChangedAt()));

        return object;
    }
}
