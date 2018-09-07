package ru.shtrm.serviceman.data.source;

import java.util.List;

import ru.shtrm.serviceman.data.EquipmentStatus;
import ru.shtrm.serviceman.data.EquipmentType;

public interface EquipmentTypeDataSource {

    List<EquipmentType> getEquipmentTypes();
}
