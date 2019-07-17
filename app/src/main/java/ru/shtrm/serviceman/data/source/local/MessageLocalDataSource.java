package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import ru.shtrm.serviceman.data.AuthorizedUser;
import ru.shtrm.serviceman.data.Message;
import ru.shtrm.serviceman.data.source.MessageDataSource;

public class MessageLocalDataSource implements MessageDataSource {

    @Nullable
    private static MessageLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private MessageLocalDataSource() {

    }

    public static MessageLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MessageLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public List<Message> getMessages() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Message> messages = realm.where(Message.class)
                .equalTo("organization.uuid", AuthorizedUser.getInstance().getUser().getUuid())
                .findAll();
        realm.close();
        return realm.copyFromRealm(messages);
    }

    @Override
    public Message getMessage(String uuid) {
        Realm realm = Realm.getDefaultInstance();
        Message message = realm.where(Message.class).equalTo("uuid", uuid)
                .equalTo("organization.uuid", AuthorizedUser.getInstance().getUser().getUuid())
                .findFirst();
        realm.close();
        return realm.copyFromRealm(message);
    }

    /**
     * Save a photo to database.
     * @param message The photo to save. See {@link Message}
     */
    @Override
    public void saveMessage(@NonNull final Message message) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(message);
            }
        });
        realm.close();
    }

    @Override
    public long getLastId() {
        Realm realm = Realm.getDefaultInstance();
        Number lastId = realm.where(Message.class).max("_id");
        if (lastId == null) {
            lastId = 0;
        }
        realm.close();
        return lastId.longValue();
    }
}