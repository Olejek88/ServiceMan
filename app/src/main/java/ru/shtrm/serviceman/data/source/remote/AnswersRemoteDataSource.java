package ru.shtrm.serviceman.data.source.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import ru.shtrm.serviceman.data.Answer;
import ru.shtrm.serviceman.data.Image;
import ru.shtrm.serviceman.data.source.AnswersDataSource;
import ru.shtrm.serviceman.retrofit.RetrofitClient;
import ru.shtrm.serviceman.retrofit.RetrofitService;

import static ru.shtrm.serviceman.realm.RealmHelper.DATABASE_NAME;

public class AnswersRemoteDataSource implements AnswersDataSource {

    @Nullable
    private static AnswersRemoteDataSource INSTANCE;

    // Prevent direct instantiation
    private AnswersRemoteDataSource() {

    }

    // Access this instance for outside classes.
    public static AnswersRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AnswersRemoteDataSource();
        }
        return INSTANCE;
    }

    // Destroy the instance.
    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public Observable<List<Answer>> getAnswers(String questionId) {
        // Not required because the {@link AnswersRepository} handles the logic
        // of refreshing the Answers from all available data source
        return null;
    }

    @Override
    public Observable<Answer> getAnswer(@NonNull String id) {
        // Not required because the {@link AnswersRepository} handles the logic
        // of refreshing the Answers from all available data source
        return null;
    }

    @Override
    public void saveAnswer(@NonNull Answer Answer) {
        // Not required because the {@link AnswersRepository} handles the logic
        // of refreshing the Answers from all available data source
    }

    @Override
    public void deleteAnswer(@NonNull String id) {
        // Not required because the {@link AnswersRepository} handles the logic
        // of refreshing the Answers from all available data source
    }

    /**
     * Update and save the Answers' status by accessing the Internet.
     *
     * @return The observable Answers whose status are the latest.
     */
    @Override
    public Observable<List<Answer>> refreshAnswers(final String questionId) {
        // It is necessary to build a new realm instance
        // in a different thread.
        Realm realm = Realm.getInstance(new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .name(DATABASE_NAME)
                .build());

        return Observable.fromIterable(realm.copyFromRealm(realm.where(Answer.class).
                equalTo("question.id", questionId).findAll()))
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<Answer, ObservableSource<Answer>>() {
                    @Override
                    public ObservableSource<Answer> apply(Answer aAnswer) throws Exception {
                        // A nested request.
                        return refreshAnswer(aAnswer.getId());
                    }
                })
                .toList()
                .toObservable();
    }

    /**
     * Update and save Answer by accessing the network.
     *
     * @param id The Answers id. See {@link Answer#id}
     * @return The observable answer.
     */
    private Observable<Answer> refreshAnswer(@NonNull String id) {
        Realm realm = Realm.getInstance(new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .name(DATABASE_NAME)
                .build());

/*
        // Set a copy rather than use the raw data.
        final Answer Answer = realm.copyFromRealm(realm.where(Answer.class)
                .equalTo("id", id)
                .findFirst());
*/

        // Access the network.
        return RetrofitClient.getInstance()
                .create(RetrofitService.class)
                .getAnswer(id)
                .subscribeOn(Schedulers.io())
                .doOnNext(new Consumer<Answer>() {
                    @Override
                    public void accept(Answer aAnswer) throws Exception {
                        // To avoid the server error or other problems
                        // making the data in database being dirty.
                    }
                });
    }

    @Override
    public void updateAnswerTitle(@NonNull String id, @NonNull String title) {
        // Not required
    }

    @Override
    public void updateAnswerText(@NonNull String id, @NonNull String text) {
        // Not required
    }

    @Override
    public Answer getAnswerById(@NonNull String id) {
        // Not required
        return null;
    }

    @Override
    public Observable<List<Answer>> searchAnswers(@NonNull String keyWords) {
        // Not required
        return null;
    }

    @Override
    public void saveAnswer(@NonNull final Answer answer, @NonNull final ArrayList<Image> images){
    }

    @Override
    public void voteUpAnswer(@NonNull Answer answer) {
        // Not required
    }

    @Override
    public void voteDownAnswer(@NonNull Answer answer) {
        // Not required
    }
}