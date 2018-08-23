package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.Measure;

public class MeasureRepository implements MeasureDataSource {

    @Nullable
    private static MeasureRepository INSTANCE = null;

    @NonNull
    private final MeasureDataSource localDataSource;

    // Prevent direct instantiation
    private MeasureRepository(@NonNull MeasureDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static MeasureRepository getInstance(@NonNull MeasureDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new MeasureRepository(localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public List<Measure> getMeasuresByEquipment(Equipment equipment){
        return localDataSource.getMeasuresByEquipment(equipment);
    }

    @Override
    public List<Measure> getMeasures() {
        return localDataSource.getMeasures();
    }
}
