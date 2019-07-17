package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import ru.shtrm.serviceman.data.ZhObjectType;

public class ObjectTypeRepository implements ObjectTypeDataSource {

    @Nullable
    private static ObjectTypeRepository INSTANCE = null;

    @NonNull
    private final ObjectTypeDataSource localDataSource;

    // Prevent direct instantiation
    private ObjectTypeRepository(@NonNull ObjectTypeDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static ObjectTypeRepository getInstance(@NonNull ObjectTypeDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new ObjectTypeRepository(localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public List<ZhObjectType> getObjectTypes() {
        return localDataSource.getObjectTypes();
    }

    @Override
    public ZhObjectType getObjectType(String uuid) {
        return localDataSource.getObjectType(uuid);
    }

}
