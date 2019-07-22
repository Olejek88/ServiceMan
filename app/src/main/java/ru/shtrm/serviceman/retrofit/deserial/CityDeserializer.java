package ru.shtrm.serviceman.retrofit.deserial;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;

import ru.shtrm.serviceman.data.City;
import ru.shtrm.serviceman.data.Organization;

public class CityDeserializer extends BaseDeserialzer
        implements JsonDeserializer<City> {

    @Override
    public City deserialize(JsonElement jsonElement, Type typeOF, JsonDeserializationContext context) {

        City item = new City();
        JsonObject object = jsonElement.getAsJsonObject();

        item.set_id(getLong(object, "_id"));
        item.setUuid(getString(object, "uuid"));
        item.setOrganization((Organization) getReference(object, Organization.class, "oid"));
        item.setGisId(getString(object, "gis_id", true));
        item.setTitle(getString(object, "title"));
        item.setCreatedAt(getDate(object, "createdAt"));
        item.setChangedAt(getDate(object, "changedAt"));

        super.close();

        return item;
    }
}
