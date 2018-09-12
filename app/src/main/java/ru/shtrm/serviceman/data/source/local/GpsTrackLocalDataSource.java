package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
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
        List <GpsTrack> gpsTracks = realm.where(GpsTrack.class).findAllSorted("date", Sort.DESCENDING);
        if (gpsTracks.size()>0)
            return realm.copyFromRealm(gpsTracks.get(0));
        else
            return null;
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