package ru.shtrm.serviceman.data;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class FlatType extends RealmObject {

    @PrimaryKey
    private long _id;
    private String uuid;
    private String title;
    private Date createdAt;
    private Date changedAt;

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
    
    public class Type {
        public static final String FLAT_TYPE_GENERAL = "42686CFC-34D0-45FF-95A4-04B0D865EC35";
        public static final String FLAT_TYPE_COMMERCE = "587B526B-A5C2-4B30-92DD-C63F796333A6";
        public static final String FLAT_TYPE_INPUT = "F68A562B-8F61-476F-A3E7-5666F9CEAFA1";
    }
}
