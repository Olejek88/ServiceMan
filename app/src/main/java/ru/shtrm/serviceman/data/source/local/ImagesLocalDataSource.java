package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.Sort;
import ru.shtrm.serviceman.data.Image;
import ru.shtrm.serviceman.data.source.ImagesDataSource;
import ru.shtrm.serviceman.realm.RealmHelper;

public class ImagesLocalDataSource implements ImagesDataSource {

    @Nullable
    private static ImagesLocalDataSource INSTANCE = null;

    // Prevent direct instantiation
    private ImagesLocalDataSource() {

    }

    public static ImagesLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ImagesLocalDataSource();
        }
        return INSTANCE;
    }

    @Override
    public List<Image> getImages() {
        Realm realm = RealmHelper.newRealmInstance();
        return realm.copyFromRealm(realm.where(Image.class).findAllSorted("date", Sort.DESCENDING));
    }

    @Override
    public Image getImage(@NonNull String id) {
        Realm realm = RealmHelper.newRealmInstance();
        return realm.copyFromRealm(realm.where(Image.class).equalTo("id", id).findFirst());
    }

    public void deleteImage(@NonNull String id) {
        Realm realm = RealmHelper.newRealmInstance();
        final Image Image =  realm.where(Image.class).equalTo("id", id).findFirst();
        if (Image!=null) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Image.deleteFromRealm();
                }
            });
        }
    }

    /**
     * Save a Image to database.
     * @param Image See {@link Image}
     */
    @Override
    public void saveImage(@NonNull final Image Image) {
        Realm realm = RealmHelper.newRealmInstance();
        // DO NOT forget begin and commit the transaction.
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(Image);
            }
        });
        realm.close();
    }

    public RealmList<Image> saveImages(@NonNull final ArrayList<Image> images) {
        Realm realm = RealmHelper.newRealmInstance();
        // DO NOT forget begin and commit the transaction.
        final RealmList<Image> list = new RealmList<>();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                list.addAll(images);
            }
        });
        realm.close();
        return list;
    }
}