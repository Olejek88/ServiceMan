package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import ru.shtrm.serviceman.data.DefectType;

public class DefectTypeRepository implements DefectTypeDataSource {

    @Nullable
    private static DefectTypeRepository INSTANCE = null;

    @NonNull
    private final DefectTypeDataSource localDataSource;

    // Prevent direct instantiation
    private DefectTypeRepository(@NonNull DefectTypeDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static DefectTypeRepository getInstance(@NonNull DefectTypeDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new DefectTypeRepository(localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public List<DefectType> getAllDefectTypes() {
        return localDataSource.getAllDefectTypes();
    }
}
