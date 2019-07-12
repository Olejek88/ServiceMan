package ru.shtrm.serviceman.data;

import java.util.Date;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class Photo extends RealmObject implements ISend, IBaseRecord {

    @Index
    private long _id;
    @PrimaryKey
    private String uuid;
    private ZhObject object;
    private User user;
    private Double longitude;
    private Double lattitude;
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

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLattitude() {
        return lattitude;
    }

    public void setLattitude(Double lattitude) {
        this.lattitude = lattitude;
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

    public ZhObject getObject() {
        return object;
    }

    public void setObject(ZhObject object) {
        this.object = object;
    }
}
