package ru.shtrm.serviceman.data;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class EquipmentRegisterType extends RealmObject {

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

    class Type {
        public static final String REGISTER_TYPE_CHANGE_STATUS = "2D3AD301-FD41-4A45-A18B-6CD13526CFDD";
        public static final String REGISTER_TYPE_CHANGE_PLACE = "BE1D4149-2563-4771-88DC-2EB8B3DA684F";
        public static final String REGISTER_TYPE_CHANGE_PROPERTIES = "4C74019F-45A9-43Ab-9B97-4D077F8BF3FA";
    }
}
