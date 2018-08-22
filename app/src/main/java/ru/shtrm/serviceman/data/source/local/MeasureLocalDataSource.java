package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.Measure;
import ru.shtrm.serviceman.data.source.MeasureDataSource;

public class MeasureLocalDataSource implements MeasureDataSource {

    @Nullable
    private static MeasureLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private MeasureLocalDataSource() {

    }

    public static MeasureLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MeasureLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public List<Measure> getMeasuresByEquipment(Equipment equipment) {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                realm.where(Measure.class).equalTo("equipment", equipment.getUuid()).
                        findAllSorted("date"));
    }

    @Override
    public List<Measure> getMeasures() {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                realm.where(Measure.class).findAllSorted("date"));
    }
}