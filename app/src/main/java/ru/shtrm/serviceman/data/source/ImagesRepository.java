package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import ru.shtrm.serviceman.data.Image;

public class ImagesRepository implements ImagesDataSource {

    @Nullable
    private static ImagesRepository INSTANCE = null;

    @NonNull
    private final ImagesDataSource localDataSource;

    // Prevent direct instantiation
    private ImagesRepository(@NonNull ImagesDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static ImagesRepository getInstance(@NonNull ImagesDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new ImagesRepository(localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public List<Image> getImages() {
        return localDataSource.getImages();
    }

    @Override
    public Image getImage(@NonNull String id) {
        return localDataSource.getImage(id);
    }

    @Override
    public void deleteImage(@NonNull String id) {
        localDataSource.deleteImage(id);
    }

    @Override
    public void saveImage(@NonNull Image Image) {
        localDataSource.saveImage(Image);
    }

    @Override
    public RealmList<Image> saveImages(@NonNull ArrayList<Image> images) {
        return localDataSource.saveImages(images);
    }

}
