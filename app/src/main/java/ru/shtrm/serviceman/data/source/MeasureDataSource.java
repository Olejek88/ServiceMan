package ru.shtrm.serviceman.data.source;

import java.util.List;

import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.Flat;
import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.Measure;

public interface MeasureDataSource {

    List<Measure> getMeasuresByEquipment(Equipment equipment);

    Measure getLastMeasureByFlat(Flat flat);

    Measure getLastMeasureByHouse(House house);

    List<Measure> getMeasures();

    void addMeasure(Measure measure);

    long getLastId();

    long getUnsentMeasuresCount();
}
