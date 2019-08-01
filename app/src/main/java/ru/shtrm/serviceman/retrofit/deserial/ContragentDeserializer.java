package ru.shtrm.serviceman.retrofit.deserial;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;

import ru.shtrm.serviceman.data.Contragent;
import ru.shtrm.serviceman.data.ContragentType;
import ru.shtrm.serviceman.data.Organization;

public class ContragentDeserializer extends BaseDeserialzer implements JsonDeserializer<Contragent> {

    @Override
    public Contragent deserialize(JsonElement jsonElement, Type typeOF,
                                  JsonDeserializationContext context) {

        Contragent item = new Contragent();
        JsonObject object = jsonElement.getAsJsonObject();

        item.set_id(getLong(object, "_id"));
        item.setUuid(getString(object, "uuid"));
        item.setOrganization((Organization) getReference(object, Organization.class, "oid"));
        item.setGisId(getString(object, "gis_id", true));
        item.setTitle(getString(object, "title"));
        item.setAddress(getString(object, "address"));
        item.setPhone(getString(object, "phone"));
        item.setInn(getString(object, "inn"));
        item.setAccount(getString(object, "account"));
        item.setDirector(getString(object, "director"));
        item.setEmail(getString(object, "email"));
        item.setContragentType((ContragentType) getReference(object, ContragentType.class, "contragentTypeUuid"));
        item.setCreatedAt(getDate(object, "createdAt"));
        item.setChangedAt(getDate(object, "changedAt"));

        return item;
    }
}
