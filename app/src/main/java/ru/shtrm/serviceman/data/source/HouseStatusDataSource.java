package ru.shtrm.serviceman.data.source;

import java.util.List;

import ru.shtrm.serviceman.data.HouseStatus;

public interface HouseStatusDataSource {

    List<HouseStatus> getHouseStatuses();

    HouseStatus getHouseStatus(String uuid);

}
