package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import ru.shtrm.serviceman.data.Message;
import ru.shtrm.serviceman.data.PhotoMessage;
import ru.shtrm.serviceman.data.source.PhotoMessageDataSource;

public class PhotoMessageLocalDataSource implements PhotoMessageDataSource {

    @Nullable
    private static PhotoMessageLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private PhotoMessageLocalDataSource() {

    }

    public static PhotoMessageLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PhotoMessageLocalDataSource();
        }
        return INSTANCE;
    }


    /**
     * Save a photo to database.
     * @param photoMessage The photo to save. See {@link PhotoMessage}
     */
    @Override
    public void savePhotoMessage(@NonNull final PhotoMessage photoMessage) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(photoMessage);
            }
        });
        realm.close();
    }

    @Override
    public long getLastId() {
        Realm realm = Realm.getDefaultInstance();
        Number lastId = realm.where(PhotoMessage.class).max("_id");
        if (lastId == null) {
            lastId = 0;
        }
        realm.close();
        return lastId.longValue();
    }
}