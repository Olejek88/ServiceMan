package ru.shtrm.serviceman.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Journal extends RealmObject implements ISend, IBaseRecord {
    @PrimaryKey
    private long _id;
    private String userUuid;
    private String description;
    private Date date;
    private String type;
    private String title;
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

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").serializeNulls().create();
        UpdateQuery itemToSend = new UpdateQuery(Journal.class.getSimpleName(), null,
                null, gson.toJson(journal), journal.date);

        boolean isTransaction = realm.isInTransaction();
        if (!isTransaction) {
            realm.beginTransaction();
        }

//        realm.copyToRealmOrUpdate(journal);
        realm.copyToRealmOrUpdate(itemToSend);

        if (!isTransaction) {
            realm.commitTransaction();
        }

        realm.close();
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
