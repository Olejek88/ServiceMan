package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import ru.shtrm.serviceman.data.Defect;

public class DefectRepository implements DefectDataSource {

    @Nullable
    private static DefectRepository INSTANCE = null;

    @NonNull
    private final DefectDataSource localDataSource;

    // Prevent direct instantiation
    private DefectRepository(@NonNull DefectDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static DefectRepository getInstance(@NonNull DefectDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new DefectRepository(localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public List<Defect> getDefects() {
        return localDataSource.getDefects();
    }

    @Override
    public Defect getDefect(String uuid) {
        return localDataSource.getDefect(uuid);
    }

    @Override
    public void addDefect(@NonNull final Defect defect) {
        localDataSource.addDefect(defect);
    }

    @Override
    public long getLastId() {
        return localDataSource.getLastId();
    }
}
