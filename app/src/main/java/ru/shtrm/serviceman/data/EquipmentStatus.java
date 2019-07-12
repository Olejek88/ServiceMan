package ru.shtrm.serviceman.data;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class EquipmentStatus extends RealmObject {

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

    class Status {
        public static final String NOT_MOUNTED = "A01B7550-4211-4D7A-9935-80A2FC257E92";
        public static final String WORK = "E681926C-F4A3-44BD-9F96-F0493712798D";
        public static final String NOT_WORK = "D5D31037-6640-4A8B-8385-355FC71DEBD7";
        public static final String UNKNOWN = "ED20012C-629A-4275-9BFA-A81D08B45758";
    }
}
