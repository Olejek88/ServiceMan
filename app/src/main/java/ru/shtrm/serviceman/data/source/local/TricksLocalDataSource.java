package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.Sort;
import ru.shtrm.serviceman.data.Image;
import ru.shtrm.serviceman.data.Trick;
import ru.shtrm.serviceman.data.source.TricksDataSource;
import ru.shtrm.serviceman.realm.RealmHelper;

public class TricksLocalDataSource implements TricksDataSource {

    @Nullable
    private static TricksLocalDataSource INSTANCE;

    // Prevent direct instantiation
    private TricksLocalDataSource() {
    }

    // Access this instance for other classes.
    public static TricksLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TricksLocalDataSource();
        }
        return INSTANCE;
    }

    // Destroy the instance.
    public static void destroyInstance() {
        INSTANCE = null;
    }

    /**
     * Get the Tricks in database and sort them in timestamp descending.
     * @return The observable packages from database.
     */
    @Override
    public List<Trick> getTricks() {
        Realm rlm = RealmHelper.newRealmInstance();
        return rlm.copyFromRealm(rlm.where(Trick.class).findAllSorted("date", Sort.DESCENDING));
    }

    /**
     * Get a Trick in database of specific id.
     * @param id The primary key
     *                   or in another words, the package id.
     *                   See {@link Trick#id}
     * @return The observable package from database.
     */
    @Override
    public Trick getTrick(@NonNull String id) {
        Realm rlm = RealmHelper.newRealmInstance();
        return rlm.copyFromRealm(rlm.where(Trick.class)
                .equalTo("id", id)
                .findFirst());
    }

    /**
     * Save a Trick to database.
     * @param trick The Trick to save. See {@link Trick}
     */
    @Override
    public void saveTrick(@NonNull final Trick trick, @NonNull final ArrayList<Image> images) {
        Realm realm = RealmHelper.newRealmInstance();
        final RealmList<Image> list = new RealmList<>();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                list.addAll(images);
                trick.setImages(list);
                realm.copyToRealmOrUpdate(trick);
            }
        });
        realm.close();
    }

    /**
     * Delete a Trick with specific id from database.
     * @param id The primary key of a package
     *                  See {@link Trick#id}
     */
    @Override
    public void deleteTrick(@NonNull String id) {
        Realm realm = RealmHelper.newRealmInstance();
        final Trick trick = realm.where(Trick.class)
                .equalTo("id", id)
                .findFirst();
        if (trick != null) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    trick.deleteFromRealm();
                }
            });
        }
        realm.close();
    }
}
