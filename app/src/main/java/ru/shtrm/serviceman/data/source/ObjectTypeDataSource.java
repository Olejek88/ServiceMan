package ru.shtrm.serviceman.data.source;

import java.util.List;

import ru.shtrm.serviceman.data.ZhObjectType;

public interface ObjectTypeDataSource {

    List<ZhObjectType> getObjectTypes();

    ZhObjectType getObjectType(String uuid);

}
