package ru.shtrm.serviceman.data.source;

import java.util.List;

import ru.shtrm.serviceman.data.EquipmentStatus;

public interface EquipmentStatusDataSource {

    List<EquipmentStatus> getEquipmentStatuses();
}
