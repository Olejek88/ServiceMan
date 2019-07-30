package ru.shtrm.serviceman.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import ru.shtrm.serviceman.app.App;
import ru.shtrm.serviceman.data.source.local.GpsTrackLocalDataSource;
import ru.shtrm.serviceman.retrofit.serial.PhotoSerializer;

public class Photo extends RealmObject implements ISend, IBaseRecord {

    @Index
    private long _id;
    @PrimaryKey
    private String uuid;
    private String objectUuid;
    private User user;
    private double longitude;
    private double latitude;
    private Date createdAt;
    private Date changedAt;
    private boolean sent;

    public Photo() {
        uuid = UUID.randomUUID().toString().toUpperCase();
        sent = false;
        Date createDate = new Date();
        createdAt = createDate;
        changedAt = createDate;
        user = AuthorizedUser.getInstance().getUser();
    }

    public static long getLastId() {
        Realm realm = Realm.getDefaultInstance();

        Number lastId = realm.where(Photo.class).max("_id");
        if (lastId == null) {
            lastId = 0;
        }

        realm.close();
        return lastId.longValue();
    }

    public static void savePhoto(String photoUuid, String objectUuid) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .registerTypeAdapter(Photo.class, new PhotoSerializer())
                .serializeNulls()
                .create();

        GpsTrackLocalDataSource gpsTrackRepository = GpsTrackLocalDataSource.getInstance();
        GpsTrack lastPosition = gpsTrackRepository.getLastTrack();

        Photo photo = new Photo();
        photo.setUuid(photoUuid);
        photo.setObjectUuid(objectUuid);
        if (lastPosition != null) {
            photo.setLatitude(lastPosition.getLatitude());
            photo.setLongitude(lastPosition.getLongitude());
        } else {
            photo.setLatitude(App.defaultLatitude);
            photo.setLongitude(App.defaultLongitude);
        }

        UpdateQuery query = new UpdateQuery(
                Photo.class.getSimpleName(),
                photoUuid,
                null,
                gson.toJson(photo),
                photo.getChangedAt()
        );
        query.set_id(UpdateQuery.getLastId() + 1);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(query);
        realm.commitTransaction();
        realm.close();
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double lattitude) {
        this.latitude = lattitude;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(Date changedAt) {
        this.changedAt = changedAt;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public String getObjectUuid() {
        return objectUuid;
    }

    public void setObjectUuid(String objectUuid) {
        this.objectUuid = objectUuid;
    }
}
