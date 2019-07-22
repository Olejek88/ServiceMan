package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import ru.shtrm.serviceman.data.MeasureType;
import ru.shtrm.serviceman.data.source.MeasureTypeDataSource;


public class MeasureTypeLocalDataSource implements MeasureTypeDataSource {

    @Nullable
    private static MeasureTypeLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private MeasureTypeLocalDataSource() {

    }

    public static MeasureTypeLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MeasureTypeLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public List<MeasureType> getMeasureTypes() {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                realm.where(MeasureType.class).findAllSorted("title"));
    }

    @Override
    public MeasureType getMeasureType(String uuid) {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                realm.where(MeasureType.class).equalTo("uuid", uuid).findFirst());
    }
}