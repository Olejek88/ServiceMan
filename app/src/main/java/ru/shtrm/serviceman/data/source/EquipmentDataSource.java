package ru.shtrm.serviceman.data.source;

import java.util.Date;
import java.util.List;

import ru.shtrm.serviceman.data.AlarmType;
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.EquipmentStatus;
import ru.shtrm.serviceman.data.EquipmentType;
import ru.shtrm.serviceman.data.Flat;
import ru.shtrm.serviceman.data.House;

public interface EquipmentDataSource {

    Equipment getEquipmentByUuid(String uuid);

    List<Equipment> getAllEquipment();

    List<Equipment> getEquipmentByHouse(House house);

    List<Equipment> getEquipmentByFlat(Flat flat);

    List<Equipment> getEquipmentByType(EquipmentType equipmentType);

}
