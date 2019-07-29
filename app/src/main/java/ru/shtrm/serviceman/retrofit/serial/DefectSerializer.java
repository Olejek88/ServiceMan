package ru.shtrm.serviceman.retrofit.serial;

import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Locale;

import ru.shtrm.serviceman.data.Defect;
import ru.shtrm.serviceman.data.Task;

public class DefectSerializer implements JsonSerializer<Defect> {
    @Override
    public JsonObject serialize(Defect src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

        Task task = src.getTask();
        String taskUuid = task == null ? null : task.getUuid();
        object.addProperty("uuid", src.getUuid());
        object.addProperty("oid", src.getOrganization().getUuid());
        object.addProperty("userUuid", src.getUser().getUuid());
        object.addProperty("title", src.getTitle());
        object.addProperty("date", sdf.format(src.getDate()));
        object.addProperty("taskUuid", taskUuid);
        object.addProperty("equipmentUuid", src.getEquipment().getUuid());
        object.addProperty("defectTypeUuid", src.getDefectType().getUuid());
        object.addProperty("defectStatus", src.getDefectStatus());
        object.addProperty("createdAt", sdf.format(src.getCreatedAt()));
        object.addProperty("changedAt", sdf.format(src.getChangedAt()));

        return object;
    }
}
