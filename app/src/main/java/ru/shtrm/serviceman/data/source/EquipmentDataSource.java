package ru.shtrm.serviceman.data.source;

import java.util.List;

import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.EquipmentStatus;
import ru.shtrm.serviceman.data.EquipmentType;
import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.ZhObject;

public interface EquipmentDataSource {

    Equipment getEquipmentByUuid(String uuid);

    List<Equipment> getAllEquipment();

    List<Equipment> getEquipmentByObject(ZhObject object);

    List<Equipment> getEquipmentByType(EquipmentType equipmentType);

    long getLastId();
}
