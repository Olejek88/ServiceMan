package ru.shtrm.serviceman.data;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UserSystem extends RealmObject {

    @PrimaryKey
    private long _id;
    private String uuid;
    private Organization organization;
    private User user;
    private EquipmentSystem equipmentSystem;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public EquipmentSystem getEquipmentSystem() {
        return equipmentSystem;
    }

    public void setEquipmentSystem(EquipmentSystem equipmentSystem) {
        this.equipmentSystem = equipmentSystem;
    }

    public class Type {
        public static final int REQUEST_PAY = 1;
        public static final int REQUEST_FREE = 0;
    }
}
