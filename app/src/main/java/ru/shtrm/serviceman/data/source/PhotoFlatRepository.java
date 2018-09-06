package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import ru.shtrm.serviceman.data.Flat;
import ru.shtrm.serviceman.data.Measure;
import ru.shtrm.serviceman.data.PhotoFlat;

public class PhotoFlatRepository implements PhotoFlatDataSource {

    @Nullable
    private static PhotoFlatRepository INSTANCE = null;

    @NonNull
    private final PhotoFlatDataSource localDataSource;

    // Prevent direct instantiation
    private PhotoFlatRepository(@NonNull PhotoFlatDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static PhotoFlatRepository getInstance(@NonNull PhotoFlatDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new PhotoFlatRepository(localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public List<PhotoFlat> getPhotoByFlat(Flat flat){
        return localDataSource.getPhotoByFlat(flat);
    }

    @Override
    public List<PhotoFlat> getPhotosFlats() {
        return localDataSource.getPhotosFlats();
    }

    @Override
    public PhotoFlat getLastPhotoByFlat(Flat flat) {
        return localDataSource.getLastPhotoByFlat(flat);
    }

    @Override
    public long getLastId() {
        return localDataSource.getLastId();
    }
}
