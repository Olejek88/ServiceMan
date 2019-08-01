package ru.shtrm.serviceman.retrofit.deserial;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import java.lang.reflect.Type;

import io.realm.Realm;
import ru.shtrm.serviceman.data.Contragent;
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.Organization;
import ru.shtrm.serviceman.data.Request;
import ru.shtrm.serviceman.data.RequestStatus;
import ru.shtrm.serviceman.data.RequestType;
import ru.shtrm.serviceman.data.Task;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.data.ZhObject;

public class RequestDeserializer extends BaseDeserialzer implements JsonDeserializer<Request> {

    @Override
    public Request deserialize(JsonElement jsonElement, Type typeOF,
                               JsonDeserializationContext context) {

        Request item = new Request();
        JsonElement element;
        JsonObject userObject;
        JsonObject object = jsonElement.getAsJsonObject();
        Realm realm = Realm.getDefaultInstance();
        String field;

        item.set_id(getLong(object, "_id"));
        item.setUuid(getString(object, "uuid"));
        item.setOrganization((Organization) getReference(object, Organization.class, "oid"));
        item.setType(getInt(object, "type"));
        item.setContragent((Contragent) getReference(object, Contragent.class, "contragentUuid"));
        item.setRequestStatus((RequestStatus) getReference(object, RequestStatus.class, "requestStatusUuid"));
        item.setRequestType((RequestType) getReference(object, RequestType.class, "requestTypeUuid"));
        item.setComment(getString(object, "comment"));
        item.setVerdict(getString(object, "verdict"));
        item.setResult(getString(object, "result"));
        item.setEquipment((Equipment) getReference(object, Equipment.class, "equipmentUuid"));
        item.setObject((ZhObject) getReference(object, ZhObject.class, "objectUuid"));
        item.setTask((Task) getReference(object, Task.class, "taskUuid", "uuid", true));
        item.setCloseDate(getDate(object, "closeDate", new DateTypeDeserializer(), true));
        item.setCreatedAt(getDate(object, "createdAt"));
        item.setChangedAt(getDate(object, "changedAt"));

        field = "author";
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
                if (element == null) {
                    fail(field, realm);
                } else {
                    refUuid = userObject.getAsJsonPrimitive("oid");
                    Organization userOrg = realm.where(Organization.class).equalTo("uuid", refUuid.getAsString()).findFirst();
                    if (userOrg == null) {
                        fail(field, realm);
                    } else {
                        newUser.setOrganization(userOrg);
                        item.setAuthor(newUser);
                    }
                }
            } else {
                item.setAuthor(refItem);
            }
        }

        return item;
    }

    private void fail(String field, Realm realm) {
        realm.close();
        throw new JsonParseException("Unparseable data: " + field);
    }
}
