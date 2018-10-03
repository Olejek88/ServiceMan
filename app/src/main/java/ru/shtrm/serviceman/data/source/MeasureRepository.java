package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.Flat;
import ru.shtrm.serviceman.data.House;
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
    public void addMeasure(Measure measure) {
        localDataSource.addMeasure(measure);
    }

    @Override
    public List<Measure> getMeasures() {
        return localDataSource.getMeasures();
    }

    @Override
    public long getLastId() {
        return localDataSource.getLastId();
    }

    @Override
    public Measure getLastMeasureByFlat(Flat flat){
        return localDataSource.getLastMeasureByFlat(flat);
    }

    @Override
    public Measure getLastMeasureByHouse(House house){
        return localDataSource.getLastMeasureByHouse(house);
    }

    @Override
    public long getUnsentMeasuresCount() {
        return localDataSource.getUnsentMeasuresCount();
    }
}
