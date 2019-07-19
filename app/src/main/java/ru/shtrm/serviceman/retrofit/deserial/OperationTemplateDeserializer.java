package ru.shtrm.serviceman.retrofit.deserial;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;

import ru.shtrm.serviceman.data.OperationTemplate;
import ru.shtrm.serviceman.data.Organization;

public class OperationTemplateDeserializer extends BaseDeserialzer implements JsonDeserializer<OperationTemplate> {

    @Override
    public OperationTemplate deserialize(JsonElement jsonElement, Type typeOF,
                                         JsonDeserializationContext context) {

        OperationTemplate item = new OperationTemplate();
        JsonObject object = jsonElement.getAsJsonObject();

        item.set_id(getLong(object, "_id"));
        item.setUuid(getString(object, "uuid"));
        item.setOrganization((Organization) getReference(object, Organization.class, "oid"));
        item.setTitle(getString(object, "title"));
        item.setDescription(getString(object, "description"));
        item.setCreatedAt(getDate(object, "createdAt"));
        item.setChangedAt(getDate(object, "changedAt"));

        super.close();

        return item;
    }

}
