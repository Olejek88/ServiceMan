package ru.shtrm.serviceman.retrofit.deserial;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;

import ru.shtrm.serviceman.data.RequestType;
import ru.shtrm.serviceman.data.TaskTemplate;

public class RequestTypeDeserializer extends BaseDeserialzer implements JsonDeserializer<RequestType> {

    @Override
    public RequestType deserialize(JsonElement jsonElement, Type typeOF,
                                   JsonDeserializationContext context) {

        RequestType item = new RequestType();
        JsonObject object = jsonElement.getAsJsonObject();

        item.set_id(getLong(object, "_id"));
        item.setUuid(getString(object, "uuid"));
        item.setTitle(getString(object, "title"));
        item.setTaskTemplate((TaskTemplate) getReference(object, TaskTemplate.class, "taskTemplateUuid"));
        item.setCreatedAt(getDate(object, "createdAt"));
        item.setChangedAt(getDate(object, "changedAt"));

        return item;
    }

}
