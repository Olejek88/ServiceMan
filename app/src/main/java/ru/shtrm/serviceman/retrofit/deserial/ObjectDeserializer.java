package ru.shtrm.serviceman.retrofit.deserial;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.Organization;
import ru.shtrm.serviceman.data.ZhObject;
import ru.shtrm.serviceman.data.ZhObjectStatus;
import ru.shtrm.serviceman.data.ZhObjectType;

public class ObjectDeserializer extends BaseDeserialzer implements JsonDeserializer<ZhObject> {

    @Override
    public ZhObject deserialize(JsonElement jsonElement, Type typeOF,
                                JsonDeserializationContext context) throws JsonParseException {

        ZhObject item = new ZhObject();
        JsonObject object = jsonElement.getAsJsonObject();

        item.set_id(getLong(object, "_id"));
        item.setUuid(getString(object, "uuid"));
        item.setOrganization((Organization) getReference(object, Organization.class, "oid"));
        item.setTitle(getString(object, "title"));
        item.setGisId(getString(object, "gis_id", true));
        item.setSquare(getDouble(object, "square"));
        item.setObjectStatus((ZhObjectStatus) getReference(object, ZhObjectStatus.class, "objectStatusUuid"));
        item.setHouse((House) getReference(object, House.class, "houseUuid"));
        item.setObjectType((ZhObjectType) getReference(object, ZhObjectType.class, "objectTypeUuid"));
        item.setCreatedAt(getDate(object, "createdAt"));
        item.setChangedAt(getDate(object, "changedAt"));

        return item;
    }
}
