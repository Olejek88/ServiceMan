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
import ru.shtrm.serviceman.data.Street;

public class StreetDeserializer implements JsonDeserializer<Street> {

    @Override
    public Street deserialize(JsonElement jsonElement, Type typeOF,
                              JsonDeserializationContext context) throws JsonParseException {

        Street item = new Street();
        JsonElement element;
        JsonObject streetObject = jsonElement.getAsJsonObject();
        Realm realm = Realm.getDefaultInstance();
        String field;
        DateTypeDeserializer dtd = new DateTypeDeserializer();

        field = "_id";
        element = streetObject.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            item.set_id(element.getAsLong());
        }

        field = "uuid";
        element = streetObject.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            item.setUuid(element.getAsString());
        }

        field = "oid";
        element = streetObject.get(field);
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

        field = "title";
        element = streetObject.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            item.setTitle(element.getAsString());
        }

        field = "cityUuid";
        element = streetObject.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            String cityUuid = element.getAsString();
            City city = realm.where(City.class).equalTo("uuid", cityUuid).findFirst();
            if (city == null) {
                fail(field, realm);
            } else {
                item.setCity(city);
            }
        }

        field = "createdAt";
        element = streetObject.get(field);
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
        element = streetObject.get(field);
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
