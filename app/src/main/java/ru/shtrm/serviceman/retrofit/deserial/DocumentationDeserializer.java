package ru.shtrm.serviceman.retrofit.deserial;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;

import ru.shtrm.serviceman.data.Documentation;
import ru.shtrm.serviceman.data.DocumentationType;
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.EquipmentType;
import ru.shtrm.serviceman.data.Organization;

public class DocumentationDeserializer extends BaseDeserialzer
        implements JsonDeserializer<Documentation> {

    @Override
    public Documentation deserialize(JsonElement jsonElement, Type typeOF,
                                     JsonDeserializationContext context) {

        Documentation item = new Documentation();
        JsonObject object = jsonElement.getAsJsonObject();

        item.set_id(getLong(object, "_id"));
        item.setUuid(getString(object, "uuid"));
        item.setOrganization((Organization) getReference(object, Organization.class, "oid"));
        item.setEquipment((Equipment) getReference(object, Equipment.class, "equipmentUuid", "uuid", true));
        item.setEquipmentType((EquipmentType) getReference(object, EquipmentType.class, "equipmentTypeUuid", "uuid", true));
        item.setDocumentationType((DocumentationType) getReference(object, DocumentationType.class, "documentationTypeUuid"));
        item.setTitle(getString(object, "title"));
        item.setPath(getString(object, "path"));
        item.setCreatedAt(getDate(object, "createdAt"));
        item.setChangedAt(getDate(object, "changedAt"));

        super.close();

        return item;
    }

}
