package ru.shtrm.serviceman.data;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Flat extends RealmObject {

    @PrimaryKey
    private long _id;
    private String uuid;
    private String title;
    private House house;
    private FlatStatus flatStatus;
    private Date createdAt;
    private Date changedAt;

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public FlatStatus getFlatStatus() {
        return flatStatus;
    }

    public void setFlatStatus(FlatStatus flatStatus) {
        this.flatStatus = flatStatus;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getFullTitle() {
        return getHouse().getStreet().getTitle().concat(", ").
                concat(getHouse().getTitle()).concat(", ").concat(getTitle());
    }
}
