package ru.shtrm.serviceman.gps;

import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import io.realm.Realm;
import ru.shtrm.serviceman.data.AuthorizedUser;
import ru.shtrm.serviceman.data.GpsTrack;
import ru.shtrm.serviceman.data.UpdateQuery;
import ru.shtrm.serviceman.data.User;

import static java.lang.Math.abs;

public class GPSListener implements LocationListener, GpsStatus.Listener {

    private String userUuid = null;
    private Location prevLocation = null;

    @Override
    public void onGpsStatusChanged(int event) {
    }

    @Override
    public void onLocationChanged(Location location) {
        if (prevLocation != null) {
            if (abs(prevLocation.getLatitude() - location.getLatitude()) > 0.001 || abs(prevLocation.getLongitude() - location.getLongitude()) > 0.001) {
                RecordGPSData(location.getLatitude(), location.getLongitude());
            }
        }

        if (location != null) {
            prevLocation = location;
        }
    }

    @Override
    public void onProviderDisabled(String arg0) {
    }

    @Override
    public void onProviderEnabled(String arg0) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle boundle) {
    }

    private void RecordGPSData(Double Latitude, Double Longitude) {
        User user = AuthorizedUser.getInstance().getUser();
        String uuid;
        if (user != null) {
            userUuid = user.getUuid();
            uuid = user.getUuid();
        } else {
            if (userUuid != null) {
                uuid = userUuid;
            } else {
                // нет ни текущего, ни "предыдущего" пользователя,
                // координаты "привязать" не к кому.
                return;
            }
        }

        Realm realmDB = Realm.getDefaultInstance();

        long next_id = GpsTrack.getLastId() + 1;

        realmDB.beginTransaction();
//        GpsTrack gpstrack = realmDB.createObject(GpsTrack.class, next_id);
        GpsTrack gpstrack = new GpsTrack();
        gpstrack.set_id(next_id);
        gpstrack.setDate(new Date());
        gpstrack.setUserUuid(uuid);
        gpstrack.setLatitude(Latitude);
        gpstrack.setLongitude(Longitude);

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").serializeNulls().create();
        UpdateQuery itemToSend = new UpdateQuery(GpsTrack.class.getSimpleName(), null,
                null, gson.toJson(gpstrack), gpstrack.getDate());
        itemToSend.set_id(UpdateQuery.getLastId() + 1);

//        realmDB.copyToRealmOrUpdate(gpstrack);
        realmDB.copyToRealmOrUpdate(itemToSend);
        realmDB.commitTransaction();

        realmDB.close();
    }
}
