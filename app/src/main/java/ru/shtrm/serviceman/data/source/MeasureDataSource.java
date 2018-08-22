package ru.shtrm.serviceman.data.source;

import java.util.List;

import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.Measure;
import ru.shtrm.serviceman.data.Street;

public interface MeasureDataSource {

    List<Measure> getMeasuresByEquipment(Equipment equipment);

    List<Measure> getMeasures();

}