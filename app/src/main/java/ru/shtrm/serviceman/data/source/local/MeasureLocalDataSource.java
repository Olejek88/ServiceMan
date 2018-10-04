package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.Flat;
import ru.shtrm.serviceman.data.House;
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
                realm.where(Measure.class).equalTo("equipment.uuid", equipment.getUuid()).
                        findAllSorted("date"));
    }

    @Override
    public List<Measure> getMeasures() {
        Realm realm = Realm.getDefaultInstance();
        return realm.copyFromRealm(
                realm.where(Measure.class).findAllSorted("date"));
    }

    @Override
    public void addMeasure(@NonNull final Measure measure) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(measure);
            }
        });
        realm.close();
    }

    @Override
    public long getLastId() {
        Realm realm = Realm.getDefaultInstance();
        Number lastId = realm.where(Measure.class).max("_id");
        if (lastId == null) {
            lastId = 0;
        }
        realm.close();
        return lastId.longValue();
    }

    @Override
    public Measure getLastMeasureByFlat(Flat flat) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Measure> measures  = realm.where(Measure.class).
                equalTo("equipment.flat.uuid", flat.getUuid()).
                findAllSorted("date");
        if (measures.size()>0)
            return realm.copyFromRealm(measures.first());
        else
            return null;
    }

    @Override
    public Measure getLastMeasureByHouse(House house) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Measure> measures  = realm.where(Measure.class).
                equalTo("equipment.flat.house.uuid", house.getUuid()).
                findAllSorted("date");
        if (measures.size()>0)
            return realm.copyFromRealm(measures.first());
        else
            return null;
    }

    @Override
    public long getUnsentMeasuresCount() {
        Realm realm = Realm.getDefaultInstance();
        //TODO uncomment when ready
        return realm.where(Measure.class).equalTo("sent",false).count();
    }
}