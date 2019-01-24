package ru.shtrm.serviceman.data.source;

import java.util.List;

import ru.shtrm.serviceman.data.EquipmentStatus;
import ru.shtrm.serviceman.data.WorkStatus;

public interface WorkStatusDataSource {

    List<WorkStatus> getWorkStatuses();

    WorkStatus getWorkStatusByUuid(String uuid);
}
