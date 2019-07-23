package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import io.realm.Sort;
import ru.shtrm.serviceman.data.DefectType;
import ru.shtrm.serviceman.data.source.DefectTypeDataSource;

public class DefectTypeLocalDataSource implements DefectTypeDataSource {

    @Nullable
    private static DefectTypeLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private DefectTypeLocalDataSource() {

    }

    public static DefectTypeLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DefectTypeLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public List<DefectType> getAllDefectTypes() {
        Realm realm = Realm.getDefaultInstance();
        List<DefectType> list = realm.where(DefectType.class)
                .findAllSorted("title", Sort.ASCENDING);
        list = realm.copyFromRealm(list);
        realm.close();
        return list;
    }
}