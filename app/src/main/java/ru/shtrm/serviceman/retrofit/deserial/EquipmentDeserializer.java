package ru.shtrm.serviceman.retrofit.deserial;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Date;

import io.realm.Realm;
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.EquipmentStatus;
import ru.shtrm.serviceman.data.EquipmentType;
import ru.shtrm.serviceman.data.Organization;
import ru.shtrm.serviceman.data.ZhObject;

public class EquipmentDeserializer implements JsonDeserializer<Equipment> {

    @Override
    public Equipment deserialize(JsonElement jsonElement, Type typeOF,
                                 JsonDeserializationContext context) throws JsonParseException {

        Equipment item = new Equipment();
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

        field = "oid";
        element = object.get(field);
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
        element = object.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            item.setTitle(element.getAsString());
        }

        field = "equipmentTypeUuid";
        element = object.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            String refUuid = element.getAsString();
            EquipmentType refItem = realm.where(EquipmentType.class).equalTo("uuid", refUuid).findFirst();
            if (refItem == null) {
                fail(field, realm);
            } else {
                item.setEquipmentType(refItem);
            }
        }

        field = "serial";
        element = object.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            item.setSerial(element.getAsString());
        }

        field = "tag";
        element = object.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            item.setTag(element.getAsString());
        }

        field = "equipmentStatusUuid";
        element = object.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            String refUuid = element.getAsString();
            EquipmentStatus refItem = realm.where(EquipmentStatus.class)
                    .equalTo("uuid", refUuid).findFirst();
            if (refItem == null) {
                fail(field, realm);
            } else {
                item.setEquipmentStatus(refItem);
            }
        }

        field = "inputDate";
        element = object.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            try {
                Date date = dtd.deserialize(element, null, null);
                item.setInputDate(date);
            } catch (JsonParseException e) {
                e.printStackTrace();
                fail(field, realm);
            }
        }

        field = "testDate";
        element = object.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            try {
                Date date = dtd.deserialize(element, null, null);
                item.setTestDate(date);
            } catch (JsonParseException e) {
                e.printStackTrace();
                fail(field, realm);
            }
        }

        field = "period";
        element = object.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            item.setPeriod(element.getAsInt());
        }

        field = "replaceDate";
        element = object.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            try {
                Date date = dtd.deserialize(element, null, null);
                item.setReplaceDate(date);
            } catch (JsonParseException e) {
                e.printStackTrace();
                fail(field, realm);
            }
        }

        field = "objectUuid";
        element = object.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            String refUuid = element.getAsString();
            ZhObject refItem = realm.where(ZhObject.class)
                    .equalTo("uuid", refUuid).findFirst();
            if (refItem == null) {
                fail(field, realm);
            } else {
                item.setObject(refItem);
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
