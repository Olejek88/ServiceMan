package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.Street;
import ru.shtrm.serviceman.data.ZhObject;
import ru.shtrm.serviceman.data.ZhObjectStatus;

public class ObjectRepository implements ObjectDataSource {

    @Nullable
    private static ObjectRepository INSTANCE = null;

    @NonNull
    private final ObjectDataSource localDataSource;

    // Prevent direct instantiation
    private ObjectRepository(@NonNull ObjectDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static ObjectRepository getInstance(@NonNull ObjectDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new ObjectRepository(localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public List<ZhObject> getObjectsByHouse(House house) {
        return localDataSource.getObjectsByHouse(house);
    }

    @Override
    public List<ZhObject> getObjects() {
        return localDataSource.getObjects();
    }

    @Override
    public ZhObject getObject(String uuid) {
        return localDataSource.getObject(uuid);
    }

}
