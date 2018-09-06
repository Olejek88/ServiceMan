package ru.shtrm.serviceman.data.source;

import java.util.List;

import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.EquipmentStatus;
import ru.shtrm.serviceman.data.FlatStatus;

public interface EquipmentStatusDataSource {

    List<EquipmentStatus> getEquipmentStatuses();
}
