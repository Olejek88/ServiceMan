package ru.shtrm.serviceman.data.source;

import java.util.List;

import ru.shtrm.serviceman.data.Flat;
import ru.shtrm.serviceman.data.PhotoFlat;

public interface PhotoFlatDataSource {

    List<PhotoFlat> getPhotoByFlat(Flat flat);

    List<PhotoFlat> getPhotosFlats();

}
