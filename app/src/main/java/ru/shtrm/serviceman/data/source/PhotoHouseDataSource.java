package ru.shtrm.serviceman.data.source;

import java.util.List;

import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.PhotoHouse;

public interface PhotoHouseDataSource {

    List<PhotoHouse> getPhotoByHouse(House house);

    List<PhotoHouse> getPhotosHouses();

    void savePhotoHouse(PhotoHouse photoHouse);
}
