package ru.shtrm.serviceman.retrofit.deserial;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import java.lang.reflect.Type;
import java.util.Date;

import io.realm.Realm;
import ru.shtrm.serviceman.data.Defect;
import ru.shtrm.serviceman.data.DefectType;
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.Organization;
import ru.shtrm.serviceman.data.Task;
import ru.shtrm.serviceman.data.User;

public class DefectDeserializer implements JsonDeserializer<Defect> {

    @Override
    public Defect deserialize(JsonElement jsonElement, Type typeOF,
                              JsonDeserializationContext context) throws JsonParseException {

        Defect item = new Defect();
        JsonElement element;
        JsonObject userObject;
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

        field = "userUuid";
        userObject = itemObject.getAsJsonObject(field);
        if (userObject == null) {
            fail(field, realm);
        } else {
            JsonPrimitive refUuid = userObject.getAsJsonPrimitive("uuid");
            User refItem = realm.where(User.class).equalTo("uuid", refUuid.getAsString()).findFirst();
            if (refItem == null) {
                User newUser = new User();
                newUser.set_id(userObject.getAsJsonPrimitive("_id").getAsLong());
                newUser.setUuid(userObject.getAsJsonPrimitive("uuid").getAsString());
                newUser.setName(userObject.getAsJsonPrimitive("name").getAsString());
                newUser.setPin(userObject.getAsJsonPrimitive("pin").getAsString());
                newUser.setImage(userObject.getAsJsonPrimitive("image").getAsString());
                newUser.setContact(userObject.getAsJsonPrimitive("contact").getAsString());

                field = "oid";
                element = itemObject.get(field);
                if (element == null) {
                    fail(field, realm);
                } else {
                    refUuid = userObject.getAsJsonPrimitive("oid");
                    Organization userOrg = realm.where(Organization.class).equalTo("uuid", refUuid.getAsString()).findFirst();
                    if (userOrg == null) {
                        fail(field, realm);
                    } else {
                        newUser.setOrganization(userOrg);
                        item.setUser(newUser);
                    }
                }
            } else {
                item.setUser(refItem);
            }
        }

        field = "title";
        element = itemObject.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            item.setTitle(element.getAsString());
        }

        field = "date";
        element = itemObject.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            try {
                Date date = dtd.deserialize(element, null, null);
                item.setDate(date);
            } catch (JsonParseException e) {
                e.printStackTrace();
                fail(field, realm);
            }
        }

        field = "taskUuid";
        element = itemObject.get(field);
        if (element == null) {
            item.setTask(null);
        } else {
            String refUuid = element.getAsString();
            Task refItem = realm.where(Task.class).equalTo("uuid", refUuid).findFirst();
            if (refItem == null) {
                fail(field, realm);
            } else {
                item.setTask(refItem);
            }
        }

        field = "equipmentUuid";
        element = itemObject.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            String refUuid = element.getAsString();
            Equipment refItem = realm.where(Equipment.class).equalTo("uuid", refUuid).findFirst();
            // TODO: Так как список оборудования не обновляем принутительно, нужно решить как быть с дефектами
            // которые указаны для оборудования которго нет на устройстве.
            if (refItem == null) {
                fail(field, realm);
            } else {
                item.setEquipment(refItem);
            }
        }

        field = "defectTypeUuid";
        element = itemObject.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            String refUuid = element.getAsString();
            DefectType refItem = realm.where(DefectType.class).equalTo("uuid", refUuid).findFirst();
            if (refItem == null) {
                fail(field, realm);
            } else {
                item.setDefectType(refItem);
            }
        }

        field = "status";
        element = itemObject.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            item.setStatus(element.getAsInt());
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

        item.setSent(true);

        realm.close();

        return item;
    }

    private void fail(String field, Realm realm) {
        realm.close();
        throw new JsonParseException("Unparseable data: " + field);
    }
}
