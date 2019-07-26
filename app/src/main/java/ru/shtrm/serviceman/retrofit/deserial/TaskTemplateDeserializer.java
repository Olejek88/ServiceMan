package ru.shtrm.serviceman.retrofit.deserial;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;

import ru.shtrm.serviceman.data.Organization;
import ru.shtrm.serviceman.data.TaskTemplate;
import ru.shtrm.serviceman.data.TaskType;

public class TaskTemplateDeserializer extends BaseDeserialzer implements JsonDeserializer<TaskTemplate> {

    @Override
    public TaskTemplate deserialize(JsonElement jsonElement, Type typeOF,
                                    JsonDeserializationContext context) {

        TaskTemplate item = new TaskTemplate();
        JsonObject object = jsonElement.getAsJsonObject();

        item.set_id(getLong(object, "_id"));
        item.setUuid(getString(object, "uuid"));
        item.setOrganization((Organization) getReference(object, Organization.class, "oid"));
        item.setTitle(getString(object, "title"));
        item.setDescription(getString(object, "description"));
        item.setNormative(getInt(object, "normative"));
        item.setTaskType((TaskType) getReference(object, TaskType.class, "taskTypeUuid"));
        item.setCreatedAt(getDate(object, "createdAt"));
        item.setChangedAt(getDate(object, "changedAt"));

        return item;
    }
}
