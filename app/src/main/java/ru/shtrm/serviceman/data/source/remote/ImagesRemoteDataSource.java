package ru.shtrm.serviceman.data.source.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import ru.shtrm.serviceman.data.Image;
import ru.shtrm.serviceman.data.source.ImagesDataSource;

public class ImagesRemoteDataSource implements ImagesDataSource {

    @Nullable
    private static ImagesRemoteDataSource INSTANCE;

    // Prevent direct instantiation
    private ImagesRemoteDataSource() {

    }

    // Access this instance for outside classes.
    public static ImagesRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ImagesRemoteDataSource();
        }
        return INSTANCE;
    }

    // Destroy the instance.
    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public List<Image> getImages() {
        // Not required because the {@link ImagesRepository} handles the logic
        // of refreshing the Images from all available data source
        return null;
    }

    @Override
    public Image getImage(@NonNull String id) {
        // Not required because the {@link ImagesRepository} handles the logic
        // of refreshing the Images from all available data source
        return null;
    }

    @Override
    public void saveImage(@NonNull Image Image) {
        // Not required because the {@link ImagesRepository} handles the logic
        // of refreshing the Images from all available data source
    }

    @Override
    public void deleteImage(@NonNull String id) {
        // Not required because the {@link ImagesRepository} handles the logic
        // of refreshing the Images from all available data source
    }

    public RealmList<Image> saveImages(@NonNull ArrayList<Image> images) {
        return null;
    }
}