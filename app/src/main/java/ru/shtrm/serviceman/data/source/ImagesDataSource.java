package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import ru.shtrm.serviceman.data.Image;

public interface ImagesDataSource {

    List<Image> getImages();

    Image getImage(@NonNull final String id);

    void saveImage(@NonNull Image image);

    RealmList<Image> saveImages(@NonNull ArrayList<Image> images);

    void deleteImage(@NonNull String id);

}
