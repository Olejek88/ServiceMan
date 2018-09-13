package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import ru.shtrm.serviceman.data.Street;

public class StreetRepository implements StreetDataSource {

    @Nullable
    private static StreetRepository INSTANCE = null;

    @NonNull
    private final StreetDataSource localDataSource;

    // Prevent direct instantiation
    private StreetRepository(@NonNull StreetDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static StreetRepository getInstance(@NonNull StreetDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new StreetRepository(localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public List<Street> getStreets() {
        return localDataSource.getStreets();
    }

    @Override
    public List<Street> getStreetsByCurrentUser() {
        return localDataSource.getStreetsByCurrentUser();
    }

}
