package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import ru.shtrm.serviceman.data.Flat;
import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.Street;

public class HouseRepository implements HouseDataSource {

    @Nullable
    private static HouseRepository INSTANCE = null;

    @NonNull
    private final HouseDataSource localDataSource;

    // Prevent direct instantiation
    private HouseRepository(@NonNull HouseDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static HouseRepository getInstance(@NonNull HouseDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new HouseRepository(localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public List<House> getHousesByStreet(Street street) {
        return localDataSource.getHousesByStreet(street);
    }
}