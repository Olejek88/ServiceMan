package ru.shtrm.serviceman.data.source;

import java.util.List;

import ru.shtrm.serviceman.data.MeasureType;

public interface MeasureTypeDataSource {

    List<MeasureType> getMeasureTypes();

    MeasureType getMeasureType(String uuid);

}
