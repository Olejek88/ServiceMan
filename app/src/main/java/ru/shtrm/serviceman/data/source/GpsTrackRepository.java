package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import ru.shtrm.serviceman.data.GpsTrack;

public class GpsTrackRepository implements GpsTrackDataSource {

    @Nullable
    private static GpsTrackRepository INSTANCE = null;

    @NonNull
    private final GpsTrackDataSource localDataSource;

    // Prevent direct instantiation
    private GpsTrackRepository(@NonNull GpsTrackDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public static GpsTrackRepository getInstance(@NonNull GpsTrackDataSource localDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new GpsTrackRepository(localDataSource);
        }
        return INSTANCE;
    }

    @Override
    public GpsTrack getLastTrack() {
        return localDataSource.getLastTrack();
    }

    @Override
    public void saveTrack(@NonNull GpsTrack gpsTrack) {
        localDataSource.saveTrack(gpsTrack);
    }
}
