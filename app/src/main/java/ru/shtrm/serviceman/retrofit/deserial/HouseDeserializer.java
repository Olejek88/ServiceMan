package ru.shtrm.serviceman.retrofit.deserial;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import io.realm.Realm;
import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.HouseStatus;
import ru.shtrm.serviceman.data.HouseType;
import ru.shtrm.serviceman.data.Organization;
import ru.shtrm.serviceman.data.Street;

public class HouseDeserializer extends BaseDeserialzer implements JsonDeserializer<House> {

    @Override
    public House deserialize(JsonElement jsonElement, Type typeOF,
                             JsonDeserializationContext context) throws JsonParseException {

        House item = new House();
        JsonObject object = jsonElement.getAsJsonObject();

        item.set_id(getLong(object, "_id"));
        item.setUuid(getString(object, "uuid"));
        item.setOrganization((Organization) getReference(object, Organization.class, "oid"));
        item.setGisId(getString(object, "gis_id", true));
        item.setNumber(getString(object, "number"));
        item.setStreet((Street) getReference(object, Street.class, "streetUuid"));
        item.setHouseStatus((HouseStatus) getReference(object, HouseStatus.class, "houseStatusUuid"));
        item.setHouseType((HouseType) getReference(object, HouseType.class, "houseTypeUuid"));
        item.setCreatedAt(getDate(object, "createdAt"));
        item.setChangedAt(getDate(object, "changedAt"));

        super.close();

        return item;
    }
}
