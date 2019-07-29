package ru.shtrm.serviceman.retrofit.deserial;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;
import ru.shtrm.serviceman.data.City;
import ru.shtrm.serviceman.data.Organization;
import ru.shtrm.serviceman.data.Street;

public class StreetDeserializer extends BaseDeserialzer implements JsonDeserializer<Street> {

    @Override
    public Street deserialize(JsonElement jsonElement, Type typeOF,
                              JsonDeserializationContext context) {

        Street item = new Street();
        JsonObject object = jsonElement.getAsJsonObject();

        item.set_id(getLong(object, "_id"));
        item.setUuid(getString(object, "uuid"));
        item.setOrganization((Organization) getReference(object, Organization.class, "oid"));
        item.setTitle(getString(object, "title"));
        item.setCity((City) getReference(object, City.class, "cityUuid"));
        item.setCreatedAt(getDate(object, "createdAt"));
        item.setChangedAt(getDate(object, "changedAt"));

        return item;
    }
}
