package ru.shtrm.serviceman.retrofit.deserial;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import java.lang.reflect.Type;

import io.realm.Realm;
import ru.shtrm.serviceman.data.Defect;
import ru.shtrm.serviceman.data.DefectType;
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.Organization;
import ru.shtrm.serviceman.data.Task;
import ru.shtrm.serviceman.data.User;

public class DefectDeserializer extends BaseDeserialzer implements JsonDeserializer<Defect> {

    @Override
    public Defect deserialize(JsonElement jsonElement, Type typeOF, JsonDeserializationContext context) {

        Defect item = new Defect();
        JsonElement element;
        JsonObject userObject;
        JsonObject object = jsonElement.getAsJsonObject();
        Realm realm = Realm.getDefaultInstance();
        String field;

        item.set_id(getLong(object, "_id"));
        item.setUuid(getString(object, "uuid"));
        item.setOrganization((Organization) getReference(object, Organization.class, "oid"));
        item.setTitle(getString(object, "title"));
        item.setDate(getDate(object, "date"));
        item.setTask((Task) getReference(object, Task.class, "taskUuid", "uuid", true));
        item.setEquipment((Equipment) getReference(object, Equipment.class, "equipmentUuid"));
        item.setDefectType((DefectType) getReference(object, DefectType.class, "defectTypeUuid"));
        item.setDefectStatus(getInt(object, "defectStatus"));
        item.setCreatedAt(getDate(object, "createdAt"));
        item.setChangedAt(getDate(object, "changedAt"));
        item.setSent(true);

        field = "user";
        userObject = object.getAsJsonObject(field);
        if (userObject == null || userObject.isJsonNull()) {
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
                element = object.get(field);
                if (element == null || element.isJsonNull()) {
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

        super.close();

        return item;
    }

    private void fail(String field, Realm realm) {
        realm.close();
        throw new JsonParseException("Unparseable data: " + field);
    }
}
