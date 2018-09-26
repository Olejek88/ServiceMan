package ru.shtrm.serviceman.retrofit.deserial;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Date;

import io.realm.Realm;
import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.HouseStatus;
import ru.shtrm.serviceman.data.HouseType;
import ru.shtrm.serviceman.data.Street;

public class HouseDeserializer implements JsonDeserializer<House> {

    @Override
    public House deserialize(JsonElement jsonElement, Type typeOF,
                             JsonDeserializationContext context) throws JsonParseException {

        House item = new House();
        JsonElement element;
        JsonObject object = jsonElement.getAsJsonObject();
        Realm realm = Realm.getDefaultInstance();
        String field;
        DateTypeDeserializer dtd = new DateTypeDeserializer();

        field = "_id";
        element = object.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            item.set_id(element.getAsLong());
        }

        field = "uuid";
        element = object.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            item.setUuid(element.getAsString());
        }

        field = "number";
        element = object.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            item.setNumber(element.getAsString());
        }

        field = "streetUuid";
        element = object.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            String refUuid = element.getAsString();
            Street refItem = realm.where(Street.class).equalTo("uuid", refUuid).findFirst();
            if (refItem == null) {
                fail(field, realm);
            } else {
                item.setStreet(refItem);
            }
        }

        field = "houseStatusUuid";
        element = object.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            String refUuid = element.getAsString();
            HouseStatus refItem = realm.where(HouseStatus.class).equalTo("uuid", refUuid).findFirst();
            if (refItem == null) {
                fail(field, realm);
            } else {
                item.setHouseStatus(refItem);
            }
        }

        field = "houseTypeUuid";
        element = object.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            String refUuid = element.getAsString();
            HouseType refItem = realm.where(HouseType.class).equalTo("uuid", refUuid).findFirst();
            if (refItem == null) {
                fail(field, realm);
            } else {
                item.setHouseType(refItem);
            }
        }

        field = "createdAt";
        element = object.get(field);
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
        element = object.get(field);
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