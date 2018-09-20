package ru.shtrm.serviceman.data;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class GpsTrack extends RealmObject implements ISend, IBaseRecord {
    @PrimaryKey
    private long _id;
    private String userUuid;
    private double latitude;
    private double longitude;
    private Date date;
    private boolean sent;

    public GpsTrack() {
        sent = false;
        date = new Date();
        userUuid = getActiveUserUuid();
    }

    public GpsTrack(double lat, double lon) {
        latitude = lat;
        longitude = lon;
        sent = false;
        date = new Date();
        userUuid = getActiveUserUuid();
    }

    public static long getLastId() {
        Realm realm = Realm.getDefaultInstance();

        Number lastId = realm.where(GpsTrack.class).max("_id");
        if (lastId == null) {
            lastId = 0;
        }

        realm.close();
        return lastId.longValue();
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String uuid) {
        this.userUuid = uuid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    private String getActiveUserUuid() {
        User user = AuthorizedUser.getInstance().getUser();
        String uuid;
        if (user != null) {
            uuid = user.getUuid();
        } else {
            uuid = User.SERVICE_USER_UUID;
        }

        return uuid;
    }

}
