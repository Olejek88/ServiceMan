package ru.shtrm.serviceman.retrofit.deserial;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import java.lang.reflect.Type;

import io.realm.Realm;
import io.realm.RealmList;
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.Operation;
import ru.shtrm.serviceman.data.OperationTemplate;
import ru.shtrm.serviceman.data.Organization;
import ru.shtrm.serviceman.data.Task;
import ru.shtrm.serviceman.data.TaskTemplate;
import ru.shtrm.serviceman.data.TaskVerdict;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.data.WorkStatus;

public class TaskDeserializer extends BaseDeserialzer implements JsonDeserializer<Task> {

    @Override
    public Task deserialize(JsonElement jsonElement, Type typeOF,
                            JsonDeserializationContext context) {

        Task item = new Task();
        JsonElement element;
        JsonObject userObject;
        JsonObject object = jsonElement.getAsJsonObject();
        Realm realm = Realm.getDefaultInstance();
        String field;

        item.set_id(getLong(object, "_id"));
        item.setUuid(getString(object, "uuid"));
        item.setOrganization((Organization) getReference(object, Organization.class, "oid"));
        item.setComment(getString(object, "comment"));
        item.setWorkStatus((WorkStatus) getReference(object, WorkStatus.class, "workStatusUuid"));
        item.setEquipment((Equipment) getReference(object, Equipment.class, "equipmentUuid"));
        item.setTaskVerdict((TaskVerdict) getReference(object, TaskVerdict.class, "taskVerdictUuid"));
        item.setTaskTemplate((TaskTemplate) getReference(object, TaskTemplate.class, "taskTemplateUuid"));
        item.setTaskDate(getDate(object, "taskDate"));
        item.setStartDate(getDate(object, "startDate", new DateTypeDeserializer(), true));
        item.setDeadlineDate(getDate(object, "deadlineDate"));
        item.setEndDate(getDate(object, "endDate", new DateTypeDeserializer(), true));
        item.setCreatedAt(getDate(object, "createdAt"));
        item.setChangedAt(getDate(object, "changedAt"));

        field = "author";
        userObject = object.getAsJsonObject(field);
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

        RealmList<Operation> opList = new RealmList<>();
        JsonArray operationsArray = object.getAsJsonArray("operations");
        for (JsonElement arrayElement : operationsArray) {
            JsonObject opObject = arrayElement.getAsJsonObject();
            Operation op = new Operation();
            op.set_id(getLong(opObject, "_id"));
            op.setUuid(getString(opObject, "uuid"));
            op.setOrganization((Organization) getReference(opObject, Organization.class, "oid"));
            op.setTaskUuid(getString(opObject, "taskUuid"));
            op.setWorkStatus((WorkStatus) getReference(opObject, WorkStatus.class, "workStatusUuid"));
            op.setOperationTemplate((OperationTemplate) getReference(opObject, OperationTemplate.class, "operationTemplateUuid"));
            op.setCreatedAt(getDate(opObject, "createdAt"));
            op.setChangedAt(getDate(opObject, "changedAt"));
            opList.add(op);
        }

        item.setOperations(opList);

        return item;
    }

    private void fail(String field, Realm realm) {
        realm.close();
        throw new JsonParseException("Unparseable data: " + field);
    }
}
