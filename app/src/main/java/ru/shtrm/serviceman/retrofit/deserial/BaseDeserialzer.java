package ru.shtrm.serviceman.retrofit.deserial;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmObject;

public class BaseDeserialzer {

    public double getDouble(JsonObject object, String field) throws JsonParseException {
        JsonElement element = object.get(field);
        if (element == null || element.isJsonNull()) {
            throw new JsonParseException("Unparseable data: " + field);
        }

        return element.getAsDouble();
    }

    public int getInt(JsonObject object, String field) throws JsonParseException {
        return getInt(object, field, false);
    }

    public int getInt(JsonObject object, String field, boolean isNullable) throws JsonParseException {
        JsonElement element = object.get(field);
        if (element == null || element.isJsonNull()) {
            if (isNullable) {
                return -1;
            } else {
                throw new JsonParseException("Unparseable data: " + field);
            }
        }

        return element.getAsInt();
    }

    public long getLong(JsonObject object, String field) throws JsonParseException {
        JsonElement element = object.get(field);
        if (element == null || element.isJsonNull()) {
            throw new JsonParseException("Unparseable data: " + field);
        }

        return element.getAsLong();
    }

    public String getString(JsonObject object, String field) throws JsonParseException {
        return getString(object, field, false);
    }

    public String getString(JsonObject object, String field, boolean isNullable) throws JsonParseException {
        JsonElement element = object.get(field);
        if (element == null || element.isJsonNull()) {
            if (isNullable) {
                return null;
            } else {
                throw new JsonParseException("Unparseable data: " + field);
            }
        }

        return element.getAsString();
    }

    RealmObject getReference(JsonObject object, Class<? extends RealmObject> realmObject, String field) throws JsonParseException {
        return getReference(object, realmObject, field, "uuid", false);
    }

    RealmObject getReference(JsonObject object, Class<? extends RealmObject> realmObject, String field, String key) throws JsonParseException {
        return getReference(object, realmObject, field, key, false);
    }

    RealmObject getReference(JsonObject object, Class<? extends RealmObject> realmObject, String field, String key, boolean isNullable) throws JsonParseException {
        JsonElement element = object.get(field);
        if (element == null || element.isJsonNull()) {
            if (isNullable) {
                return null;
            } else {
                throw new JsonParseException("Unparseable data: " + field);
            }
        }

        String refUuid = element.getAsString();
        RealmObject refItem = realm.where(realmObject).equalTo(key, refUuid).findFirst();
        if (refItem == null) {
            realm.close();
            throw new JsonParseException("Unparseable data: " + field);
        }

        realm.close();
        return refItem;
    }

    Date getDate(JsonObject object, String field) throws JsonParseException {
        return getDate(object, field, new DateTypeDeserializer(), false);
    }

    Date getDate(JsonObject object, String field, JsonDeserializer<Date> deserializer, boolean isNullable) throws JsonParseException {
        JsonElement element = object.get(field);
        if (element == null || element.isJsonNull()) {
            if (isNullable) {
                return null;
            } else {
                throw new JsonParseException("Unparseable data: " + field);
            }
        }

        try {
            return deserializer.deserialize(element, null, null);
        } catch (JsonParseException e) {
            e.printStackTrace();
            throw new JsonParseException("Unparseable data: " + field);
        }
    }
}
