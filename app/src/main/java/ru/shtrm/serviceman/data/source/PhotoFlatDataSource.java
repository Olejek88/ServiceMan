package ru.shtrm.serviceman.data.source;

import java.util.List;

import ru.shtrm.serviceman.data.Flat;
import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.PhotoFlat;
import ru.shtrm.serviceman.data.PhotoHouse;

public interface PhotoFlatDataSource {

    List<PhotoFlat> getPhotoByFlat(Flat flat);

    List<PhotoFlat> getPhotosFlats();

}
