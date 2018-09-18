package ru.shtrm.serviceman.data;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Journal extends RealmObject implements ISend {
    @PrimaryKey
    private long _id;
    private String userUuid;
    private String description;
    private Date date;
    private boolean sent;

    public static long getLastId() {
        Realm realm = Realm.getDefaultInstance();

        Number lastId = realm.where(Journal.class).max("_id");
        if (lastId == null) {
            lastId = 0;
        }

        realm.close();
        return lastId.longValue();
    }

    @Override
    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String uuid) {
        this.userUuid = uuid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isSent() {
        return sent;
    }

    @Override
    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public static void add(String message) {
        User user = AuthorizedUser.getInstance().getUser();
        String userUuid;

        if (user != null) {
            userUuid = user.getUuid();
        } else {
            userUuid = User.SERVICE_USER_UUID;
        }

        if (userUuid == null) {
            userUuid = User.SERVICE_USER_UUID;
        }

        Realm realm = Realm.getDefaultInstance();

        Journal journal = new Journal();
        journal.set_id(getLastId() + 1);
        journal.setUserUuid(userUuid);
        journal.setDescription(message);
        journal.setDate(new Date());
        journal.setSent(false);

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(journal);
        realm.commitTransaction();

        realm.close();
    }
}
