package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import ru.shtrm.serviceman.data.MeasureType;


public class MeasureTypeRepository implements MeasureTypeDataSource {

    @Nullable
    private static MeasureTypeRepository INSTANCE = null;

    @NonNull
    private final MeasureTypeDataSource localDataSource;

    // Prevent direct instantiation
    private MeasureTypeRepository(@NonNull MeasureTypeDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static MeasureTypeRepository getInstance(@NonNull MeasureTypeDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new MeasureTypeRepository(localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public List<MeasureType> getMeasureTypes() {
        return localDataSource.getMeasureTypes();
    }

    @Override
    public MeasureType getMeasureType(String uuid) {
        return localDataSource.getMeasureType(uuid);
    }

}
