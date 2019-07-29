package ru.shtrm.serviceman.data;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UpdateQuery extends RealmObject {

    @PrimaryKey
    private long _id;
    private String modelClass;
    private String modelUuid;
    private String attribute;
    private String value;
    private Date createdAt;
    private Date changedAt;

    public UpdateQuery() {
        createdAt = new Date();
    }

    public UpdateQuery(String modelClass, String modelUuid, String attribute, String value, Date changedAt) {
        this.modelClass = modelClass;
        this.modelUuid = modelUuid;
        this.attribute = attribute;
        this.value = value;
        this.changedAt = changedAt;
        createdAt = new Date();
    }

    public static long getLastId() {
        Realm realm = Realm.getDefaultInstance();

        Number lastId = realm.where(UpdateQuery.class).max("_id");
        if (lastId == null) {
            lastId = 0;
        }

        realm.close();
        return lastId.longValue();
    }

    public static void addToQuery(UpdateQuery updateQuery) {
        Realm realm = Realm.getDefaultInstance();
        updateQuery.set_id(getLastId() + 1);
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(updateQuery);
        realm.commitTransaction();
        realm.close();
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getModelClass() {
        return modelClass;
    }

    public void setModelClass(String modelClass) {
        this.modelClass = modelClass;
    }

    public String getModelUuid() {
        return modelUuid;
    }

    public void setModelUuid(String modelUuid) {
        this.modelUuid = modelUuid;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
}
