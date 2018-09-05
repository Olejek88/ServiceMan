package ru.shtrm.serviceman.data.source;

import java.util.List;

import ru.shtrm.serviceman.data.Flat;
import ru.shtrm.serviceman.data.FlatStatus;
import ru.shtrm.serviceman.data.House;

public interface FlatDataSource {

    List<Flat> getFlatsByHouse(House house);

    Flat getFlat(String uuid);

    void updateFlatStatus (Flat flat, FlatStatus flatStatus);

    void addFlat(Flat flat);
}
