package ru.shtrm.serviceman.data.source;

import java.util.List;

import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.ZhObject;
import ru.shtrm.serviceman.data.ZhObjectStatus;

public interface ObjectDataSource {

    List<ZhObject> getObjects();

    ZhObject getObject(String uuid);
    List<ZhObject> getObjectsByHouse(House house);

}
