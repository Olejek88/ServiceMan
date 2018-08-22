package ru.shtrm.serviceman.data.source;

import java.util.List;

import ru.shtrm.serviceman.data.AlarmType;
import ru.shtrm.serviceman.data.Flat;
import ru.shtrm.serviceman.data.House;

public interface FlatDataSource {

    List<Flat> getFlatsByHouse(House house);
}
