package ru.shtrm.serviceman.data;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class HouseType extends RealmObject {

    @PrimaryKey
    private long _id;
    private String uuid;
    private Organization organization;
    private String gisId;
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

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public String getGisId() {
        return gisId;
    }

    public void setGisId(String gisId) {
        this.gisId = gisId;
    }

    public class Type {
        public static final String HOUSE_TYPE_PRIVATE = "6A0AB43B-AEA7-44BE-8E6C-001F4F854A4F";
        public static final String HOUSE_TYPE_MKD = "583C8D20-CE23-401D-8E90-0FFCDAA6BE50";
        public static final String HOUSE_TYPE_COMMERCE = "A156A75E-CE7A-4ED9-87D5-0FCEF89DBC9F";
        public static final String HOUSE_TYPE_BUDGET = "9AA5A1B4-224C-4BF4-B79D-2039CF314C40";
    }
}
