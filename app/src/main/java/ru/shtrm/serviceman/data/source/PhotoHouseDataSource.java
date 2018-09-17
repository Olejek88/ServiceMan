package ru.shtrm.serviceman.data.source;

import java.util.List;

import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.PhotoEquipment;
import ru.shtrm.serviceman.data.PhotoHouse;

public interface PhotoHouseDataSource {

    List<PhotoHouse> getPhotoByHouse(House house);

    List<PhotoHouse> getPhotosHouses();

    PhotoHouse getLastPhotoByHouse(House house);

    void savePhotoHouse(PhotoHouse photoHouse);

    long getLastId();
}
