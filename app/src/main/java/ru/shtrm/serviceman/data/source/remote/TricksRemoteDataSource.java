package ru.shtrm.serviceman.data.source.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import ru.shtrm.serviceman.data.Image;
import ru.shtrm.serviceman.data.Trick;
import ru.shtrm.serviceman.data.source.TricksDataSource;

public class TricksRemoteDataSource implements TricksDataSource {

    @Nullable
    private static TricksRemoteDataSource INSTANCE;

    // Prevent direct instantiation
    private TricksRemoteDataSource() {

    }

    // Access this instance for outside classes.
    public static TricksRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TricksRemoteDataSource();
        }
        return INSTANCE;
    }

    // Destroy the instance.
    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public List<Trick> getTricks() {
        // Not required because the {@link TricksRepository} handles the logic
        // of refreshing the Tricks from all available data source
        return null;
    }

    @Override
    public Trick getTrick(@NonNull String id) {
        // Not required because the {@link TricksRepository} handles the logic
        // of refreshing the Tricks from all available data source
        return null;
    }

    @Override
    public void saveTrick(@NonNull Trick trick, @NonNull final ArrayList<Image> images) {
        // Not required because the {@link TricksRepository} handles the logic
        // of refreshing the Tricks from all available data source
    }

    @Override
    public void deleteTrick(@NonNull String id) {
        // Not required because the {@link TricksRepository} handles the logic
        // of refreshing the Tricks from all available data source
    }

/*
    */
/**
     * Update and save the Tricks' status by accessing the Internet.
     * @return The observable Tricks whose status are the latest.
     *//*

    @Override
    public Observable<List<Trick>> refreshTricks() {
        // It is necessary to build a new realm instance
        // in a different thread.
        Realm realm = Realm.getInstance(new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .name(DATABASE_NAME)
                .build());

        return Observable.fromIterable(realm.copyFromRealm(realm.where(Trick.class).findAll()))
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<Trick, ObservableSource<Trick>>() {
                    @Override
                    public ObservableSource<Trick> apply(Trick aTrick) throws Exception {
                        // A nested request.
                        return refreshTrick(aTrick.getId());
                    }
                })
                .toList()
                .toObservable();
    }

    */
/**
     * Update and save a Trick status (and answers) by accessing the network.
     * @param id The Tricks id. See {@link Trick#id}
     * @return The observable package of latest status.
     *//*

    @Override
    public Observable<Trick> refreshTrick(@NonNull String id) {
        Realm realm = Realm.getInstance(new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .name(DATABASE_NAME)
                .build());

        // Set a copy rather than use the raw data.
        final Trick Trick = realm.copyFromRealm(realm.where(Trick.class)
                .equalTo("id", id)
                .findFirst());

        // Access the network.
        return RetrofitClient.getInstance()
                .create(RetrofitService.class)
                .getTrick(id)
                .filter(new Predicate<Trick>() {
                    @Override
                    public boolean test(Trick aTrick) throws Exception {
                        return aTrick != null &&
                                aTrick.getAnswers().size() > Trick.getAnswers().size();
                    }
                })
                .subscribeOn(Schedulers.io())
                .doOnNext(new Consumer<Trick>() {
                    @Override
                    public void accept(Trick aTrick) throws Exception {
                        // To avoid the server error or other problems
                        // making the data in database being dirty.
                        if (aTrick != null && aTrick.getAnswers() != null) {
                            // It is necessary to build a new realm instance
                            // in a different thread.
                            Realm rlm = Realm.getInstance(new RealmConfiguration.Builder()
                                    .deleteRealmIfMigrationNeeded()
                                    .name(DATABASE_NAME)
                                    .build());

                            // Only when the origin data is null or the origin
                            // data's size is less than the latest data's size
                            // set the Trick pushable
                            if (Trick.getAnswers().size() > Trick.getAnswers().size()) {
                                Trick.setPushable(true);
                            }
                            // TODO !!!!
                            Trick.setAnswers(aTrick.getAnswers());
                            // DO NOT forget to begin a transaction.
                            rlm.beginTransaction();
                            rlm.copyToRealmOrUpdate(Trick);
                            rlm.commitTransaction();

                            rlm.close();
                        }
                    }
                });
    }
*/

}