package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.realm.Realm;
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