package ru.shtrm.serviceman.data;

import java.util.Date;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class Message extends RealmObject implements ISend, IBaseRecord {
    @Index
    private long _id;
    @PrimaryKey
    private String uuid;
    private Organization organization;
    private User fromUser;
    private User toUser;
    private Date date;
    private String text;
    private int status;
    private Date createdAt;
    private Date changedAt;
    private boolean sent;

    public Message() {
        uuid = UUID.randomUUID().toString().toUpperCase();
        Date createDate = new Date();
        date = createDate;
        sent = false;
        createdAt = createDate;
        changedAt = createDate;
    }

    public static long getLastId() {
        Realm realm = Realm.getDefaultInstance();

        Number lastId = realm.where(Message.class).max("_id");
        if (lastId == null) {
            lastId = 0;
        }

        realm.close();
        return lastId.longValue();
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

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User user) {
        this.fromUser = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String message) {
        this.text = message;
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

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public class Status {
        public static final int MESSAGE_NEW = 0;
        public static final int MESSAGE_READ = 1;
        public static final int MESSAGE_DELETED = 2;
    }
}
