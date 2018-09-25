package ru.shtrm.serviceman.data;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Flat extends RealmObject implements ISend, IBaseRecord {

    @PrimaryKey
    private long _id;
    private String uuid;
    private String number;
    private House house;
    private FlatStatus flatStatus;
    private FlatType flatType;
    private int inhabitants;
    private Date createdAt;
    private Date changedAt;
    private boolean sent;

    public FlatType getFlatType() {
        return flatType;
    }

    public House getHouse() {
        return house;
    }

    public void setFlatType(FlatType flatType) {
        this.flatType = flatType;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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
                concat(getHouse().getNumber()).concat(" - ").concat(getNumber());
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public int getInhabitants() {
        return inhabitants;
    }

    public void setInhabitants(int inhabitants) {
        this.inhabitants = inhabitants;
    }
}
