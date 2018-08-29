package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import ru.shtrm.serviceman.data.GpsTrack;
import ru.shtrm.serviceman.data.Image;

public interface GpsTrackDataSource {

    void saveTrack(@NonNull GpsTrack gpsTrack);

    GpsTrack getLastTrack();

}
