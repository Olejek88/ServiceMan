package ru.shtrm.serviceman.retrofit.deserial;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Date;

import io.realm.Realm;
import ru.shtrm.serviceman.data.Documentation;
import ru.shtrm.serviceman.data.DocumentationType;
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.EquipmentType;
import ru.shtrm.serviceman.data.Organization;

public class DocumentationDeserializer implements JsonDeserializer<Documentation> {

    @Override
    public Documentation deserialize(JsonElement jsonElement, Type typeOF,
                                     JsonDeserializationContext context) throws JsonParseException {

        Documentation item = new Documentation();
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

        field = "equipmentUuid";
        element = itemObject.get(field);
        if (element == null) {
            // документация может быть не привязана к оборудованию, а привязана к типу оборудования
            item.setEquipment(null);
        } else {
            String refUuid = element.getAsString();
            Equipment refItem = realm.where(Equipment.class).equalTo("uuid", refUuid).findFirst();
            if (refItem == null) {
                fail(field, realm);
            } else {
                item.setEquipment(refItem);
            }
        }

        field = "equipmentTypeUuid";
        element = itemObject.get(field);
        if (element == null) {
            // документация может быть не привязана к типу оборудования, а привязана к оборудованию
            item.setEquipmentType(null);
        } else {
            String refUuid = element.getAsString();
            EquipmentType refItem = realm.where(EquipmentType.class).equalTo("uuid", refUuid).findFirst();
            if (refItem == null) {
                fail(field, realm);
            } else {
                item.setEquipmentType(refItem);
            }
        }

        field = "documentationTypeUuid";
        element = itemObject.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            String refUuid = element.getAsString();
            DocumentationType refItem = realm.where(DocumentationType.class).equalTo("uuid", refUuid).findFirst();
            if (refItem == null) {
                fail(field, realm);
            } else {
                item.setDocumentationType(refItem);
            }
        }

        field = "title";
        element = itemObject.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            item.setTitle(element.getAsString());
        }

        field = "path";
        element = itemObject.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            item.setPath(element.getAsString());
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
