package ru.shtrm.serviceman.data.source;

import java.util.List;

import ru.shtrm.serviceman.data.ZhObjectStatus;

public interface ObjectStatusDataSource {

    List<ZhObjectStatus> getObjectStatuses();

    ZhObjectStatus getObjectStatus(String uuid);

}
