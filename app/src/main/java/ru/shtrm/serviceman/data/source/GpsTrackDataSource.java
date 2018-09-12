package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;

import ru.shtrm.serviceman.data.GpsTrack;

public interface GpsTrackDataSource {

    void saveTrack(@NonNull GpsTrack gpsTrack);

    GpsTrack getLastTrack();

}
