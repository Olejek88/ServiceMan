package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import ru.shtrm.serviceman.data.AlarmType;
import ru.shtrm.serviceman.data.Flat;
import ru.shtrm.serviceman.data.House;

public class FlatRepository implements FlatDataSource {

    @Nullable
    private static FlatRepository INSTANCE = null;

    @NonNull
    private final FlatDataSource localDataSource;

    // Prevent direct instantiation
    private FlatRepository(@NonNull FlatDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static FlatRepository getInstance(@NonNull FlatDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new FlatRepository(localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public List<Flat> getFlatsByHouse(House house) {
        return localDataSource.getFlatsByHouse(house);
    }
}
