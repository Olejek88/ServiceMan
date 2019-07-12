package ru.shtrm.serviceman.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Organization extends RealmObject {
    @PrimaryKey
    private long _id;
    private String uuid;
    private String title;

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
}
