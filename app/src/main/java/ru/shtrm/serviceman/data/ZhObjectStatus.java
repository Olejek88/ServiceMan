package ru.shtrm.serviceman.data;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ZhObjectStatus extends RealmObject {

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

    public class Status {
        public static final String OBJECT_STATUS_OK = "32562AA9-DE1D-436D-A0ED-5F5789DB8712";
        public static final String OBJECT_STATUS_NO_ENTRANCE = "FEA3CC91-DD48-4264-AEF6-F91947A1B8EB";
        public static final String OBJECT_STATUS_ABSENT = "BB6E24F2-6FA5-4E9A-83C8-5E1F4D51789B";
        public static final String OBJECT_STATUS_DEFAULT = "9D86D530-1910-488E-87D9-FD2FE06CA5E7";
    }
}
