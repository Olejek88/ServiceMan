package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import ru.shtrm.serviceman.data.GpsTrack;
import ru.shtrm.serviceman.data.source.GpsTrackDataSource;

public class GpsTrackLocalDataSource implements GpsTrackDataSource {

    @Nullable
    private static GpsTrackLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private GpsTrackLocalDataSource() {

    }

    public static GpsTrackLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GpsTrackLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public GpsTrack getLastTrack() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<GpsTrack> gpsTracks = realm.where(GpsTrack.class).findAllSorted("date", Sort.DESCENDING);
        GpsTrack list = null;
        if (gpsTracks.size() > 0) {
            list = realm.copyFromRealm(gpsTracks.first());
        }

        realm.close();
        return list;
    }

    @Override
    public void saveTrack(@NonNull final GpsTrack gpsTrack) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(gpsTrack);
            }
        });
        realm.close();
    }

}