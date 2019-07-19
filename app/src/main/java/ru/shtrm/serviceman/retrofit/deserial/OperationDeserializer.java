package ru.shtrm.serviceman.retrofit.deserial;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;

import ru.shtrm.serviceman.data.Operation;
import ru.shtrm.serviceman.data.OperationTemplate;
import ru.shtrm.serviceman.data.Organization;
import ru.shtrm.serviceman.data.Task;
import ru.shtrm.serviceman.data.WorkStatus;

public class OperationDeserializer extends BaseDeserialzer implements JsonDeserializer<Operation> {

    @Override
    public Operation deserialize(JsonElement jsonElement, Type typeOF,
                                 JsonDeserializationContext context) {

        Operation item = new Operation();
        JsonObject object = jsonElement.getAsJsonObject();

        item.set_id(getLong(object, "_id"));
        item.setUuid(getString(object, "uuid"));
        item.setOrganization((Organization) getReference(object, Organization.class, "oid"));
        item.setTask((Task) getReference(object, Task.class, "taskUuid"));
        item.setWorkStatus((WorkStatus) getReference(object, WorkStatus.class, "workStatusUuid"));
        item.setOperationTemplate((OperationTemplate) getReference(object, OperationTemplate.class, "operationTemplateUuid"));
        item.setCreatedAt(getDate(object, "createdAt"));
        item.setChangedAt(getDate(object, "changedAt"));

        super.close();

        return item;
    }
}
