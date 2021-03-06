package ru.shtrm.serviceman.data.source;

import java.util.List;

import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.HouseStatus;
import ru.shtrm.serviceman.data.Street;

public interface HouseDataSource {

    List<House> getHouses();

    List<House> getHousesByStreet(Street street);

    House getHouse(String uuid);

    void updateHouseStatus (House house, HouseStatus houseStatus);
}
