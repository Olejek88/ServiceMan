package ru.shtrm.serviceman.retrofit.deserial;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Date;

import io.realm.Realm;
import ru.shtrm.serviceman.data.City;
import ru.shtrm.serviceman.data.Organization;

public class CityDeserializer implements JsonDeserializer<City> {

    @Override
    public City deserialize(JsonElement jsonElement, Type typeOF,
                            JsonDeserializationContext context) throws JsonParseException {

        City item = new City();
        JsonElement element;
        JsonObject itemObject = jsonElement.getAsJsonObject();
        Realm realm = Realm.getDefaultInstance();
        String field;
        DateTypeDeserializer dtd = new DateTypeDeserializer();

        field = "_id";
        element = itemObject.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            item.set_id(element.getAsLong());
        }

        field = "uuid";
        element = itemObject.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            item.setUuid(element.getAsString());
        }

        field = "oid";
        element = itemObject.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            String refUuid = element.getAsString();
            Organization refItem = realm.where(Organization.class).equalTo("uuid", refUuid).findFirst();
            if (refItem == null) {
                fail(field, realm);
            } else {
                item.setOrganization(refItem);
            }
        }

        field = "gis_id";
        element = itemObject.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            if (!element.isJsonNull()) {
                item.setGisId(element.getAsString());
            }
        }

        field = "title";
        element = itemObject.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            item.setTitle(element.getAsString());
        }

        field = "createdAt";
        element = itemObject.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            try {
                Date date = dtd.deserialize(element, null, null);
                item.setCreatedAt(date);
            } catch (JsonParseException e) {
                e.printStackTrace();
                fail(field, realm);
            }
        }

        field = "changedAt";
        element = itemObject.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            try {
                Date date = dtd.deserialize(element, null, null);
                item.setChangedAt(date);
            } catch (JsonParseException e) {
                e.printStackTrace();
                fail(field, realm);
            }
        }

        realm.close();

        return item;
    }

    private void fail(String field, Realm realm) {
        realm.close();
        throw new JsonParseException("Unparseable data: " + field);
    }
}
