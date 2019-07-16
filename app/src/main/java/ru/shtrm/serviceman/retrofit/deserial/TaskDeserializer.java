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
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.Organization;
import ru.shtrm.serviceman.data.Task;
import ru.shtrm.serviceman.data.TaskTemplate;
import ru.shtrm.serviceman.data.TaskVerdict;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.data.WorkStatus;

public class TaskDeserializer implements JsonDeserializer<Task> {

    @Override
    public Task deserialize(JsonElement jsonElement, Type typeOF,
                            JsonDeserializationContext context) throws JsonParseException {

        Task item = new Task();
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

        field = "comment";
        element = itemObject.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            item.setComment(element.getAsString());
        }

        field = "workStatusUuid";
        element = itemObject.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            String refUuid = element.getAsString();
            WorkStatus refItem = realm.where(WorkStatus.class).equalTo("uuid", refUuid).findFirst();
            if (refItem == null) {
                fail(field, realm);
            } else {
                item.setWorkStatus(refItem);
            }
        }

        field = "authorUuid";
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
                        item.setAuthor(newUser);
                    }
                }
            } else {
                item.setAuthor(refItem);
            }
        }

        field = "equipmentUuid";
        element = itemObject.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            String refUuid = element.getAsString();
            Equipment refItem = realm.where(Equipment.class).equalTo("uuid", refUuid).findFirst();
            if (refItem == null) {
                fail(field, realm);
            } else {
                item.setEquipment(refItem);
            }
        }

        field = "taskVerdictUuid";
        element = itemObject.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            String refUuid = element.getAsString();
            TaskVerdict refItem = realm.where(TaskVerdict.class).equalTo("uuid", refUuid).findFirst();
            if (refItem == null) {
                fail(field, realm);
            } else {
                item.setTaskVerdict(refItem);
            }
        }

        field = "taskTemplateUuid";
        element = itemObject.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            String refUuid = element.getAsString();
            TaskTemplate refItem = realm.where(TaskTemplate.class).equalTo("uuid", refUuid).findFirst();
            if (refItem == null) {
                fail(field, realm);
            } else {
                item.setTaskTemplate(refItem);
            }
        }

        field = "taskDate";
        element = itemObject.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            try {
                Date date = dtd.deserialize(element, null, null);
                item.setTaskDate(date);
            } catch (JsonParseException e) {
                e.printStackTrace();
                fail(field, realm);
            }
        }

        field = "startDate";
        element = itemObject.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            try {
                Date date = dtd.deserialize(element, null, null);
                item.setStartDate(date);
            } catch (JsonParseException e) {
                e.printStackTrace();
                fail(field, realm);
            }
        }

        field = "deadlineDate";
        element = itemObject.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            try {
                Date date = dtd.deserialize(element, null, null);
                item.setDeadlineDate(date);
            } catch (JsonParseException e) {
                e.printStackTrace();
                fail(field, realm);
            }
        }

        field = "endDate";
        element = itemObject.get(field);
        if (element == null) {
            fail(field, realm);
        } else {
            try {
                Date date = dtd.deserialize(element, null, null);
                item.setEndDate(date);
            } catch (JsonParseException e) {
                e.printStackTrace();
                fail(field, realm);
            }
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
