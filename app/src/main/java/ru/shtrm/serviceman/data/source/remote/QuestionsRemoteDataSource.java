package ru.shtrm.serviceman.data.source.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import ru.shtrm.serviceman.data.Question;
import ru.shtrm.serviceman.data.source.QuestionsDataSource;
import ru.shtrm.serviceman.retrofit.RetrofitClient;
import ru.shtrm.serviceman.retrofit.RetrofitService;

import static ru.shtrm.serviceman.realm.RealmHelper.DATABASE_NAME;

public class QuestionsRemoteDataSource implements QuestionsDataSource {

    @Nullable
    private static QuestionsRemoteDataSource INSTANCE;

    // Prevent direct instantiation
    private QuestionsRemoteDataSource() {

    }

    // Access this instance for outside classes.
    public static QuestionsRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new QuestionsRemoteDataSource();
        }
        return INSTANCE;
    }

    // Destroy the instance.
    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public Observable<List<Question>> getQuestions() {
        // Not required because the {@link QuestionsRepository} handles the logic
        // of refreshing the questions from all available data source
        return null;
    }

    @Override
    public Observable<Question> getQuestion(@NonNull String id) {
        // Not required because the {@link QuestionsRepository} handles the logic
        // of refreshing the questions from all available data source
        return null;
    }

    @Override
    public void saveQuestion(@NonNull Question question) {
        // Not required because the {@link QuestionsRepository} handles the logic
        // of refreshing the questions from all available data source
    }

    @Override
    public void deleteQuestion(@NonNull String id) {
        // Not required because the {@link QuestionsRepository} handles the logic
        // of refreshing the questions from all available data source
    }

    @Override
    public void updateQuestion(@NonNull String id, @NonNull String title, @NonNull String text) {
    }

    /**
     * Update and save the questions' status by accessing the Internet.
     *
     * @return The observable questions whose status are the latest.
     */
    @Override
    public Observable<List<Question>> refreshQuestions() {
        // It is necessary to build a new realm instance
        // in a different thread.
        Realm realm = Realm.getInstance(new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .name(DATABASE_NAME)
                .build());

        return Observable.fromIterable(realm.copyFromRealm(realm.where(Question.class).findAll()))
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<Question, ObservableSource<Question>>() {
                    @Override
                    public ObservableSource<Question> apply(Question aQuestion) throws Exception {
                        // A nested request.
                        return refreshQuestion(aQuestion.getId());
                    }
                })
                .toList()
                .toObservable();
    }

    /**
     * Update and save a question status (and answers) by accessing the network.
     * @param id The questions id. See {@link Question#id}
     * @return The observable package of latest status.
     */
    @Override
    public Observable<Question> refreshQuestion(@NonNull String id) {
        Realm realm = Realm.getInstance(new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .name(DATABASE_NAME)
                .build());

        // Set a copy rather than use the raw data.
        final Question question = realm.copyFromRealm(realm.where(Question.class)
                .equalTo("id", id)
                .findFirst());

        // Access the network.
        return RetrofitClient.getInstance()
                .create(RetrofitService.class)
                .getQuestion(id)
                .filter(new Predicate<Question>() {
                    @Override
                    public boolean test(Question aQuestion) throws Exception {
                        return aQuestion != null &&
                                aQuestion.getAnswers().size() > question.getAnswers().size();
                    }
                })
                .subscribeOn(Schedulers.io())
                .doOnNext(new Consumer<Question>() {
                    @Override
                    public void accept(Question aQuestion) throws Exception {
                        // To avoid the server error or other problems
                        // making the data in database being dirty.
                        if (aQuestion != null && aQuestion.getAnswers() != null) {
                            // It is necessary to build a new realm instance
                            // in a different thread.
                            Realm rlm = Realm.getInstance(new RealmConfiguration.Builder()
                                    .deleteRealmIfMigrationNeeded()
                                    .name(DATABASE_NAME)
                                    .build());

                            // Only when the origin data is null or the origin
                            // data's size is less than the latest data's size
                            // set the question pushable
                            if (question.getAnswers().size() > question.getAnswers().size()) {
                                question.setPushable(true);
                            }
                            // TODO !!!!
                            question.setAnswers(aQuestion.getAnswers());
                            // DO NOT forget to begin a transaction.
                            rlm.beginTransaction();
                            rlm.copyToRealmOrUpdate(question);
                            rlm.commitTransaction();

                            rlm.close();
                        }
                    }
                });
    }

    @Override
    public boolean isQuestionExist(@NonNull String id) {
        // Not required
        return false;
    }

    @Override
    public void updateQuestionTitle(@NonNull String id, @NonNull String title) {
        // Not required
    }

    @Override
    public void updateQuestionText(@NonNull String id, @NonNull String text) {
        // Not required
    }

    @Override
    public void updateQuestionClosed(@NonNull String id, boolean closed) {
        // Not required
    }

    @Override
    public Question getQuestionById(@NonNull String id) {
        // Not required
        return null;
    }

    @Override
    public Observable<List<Question>> searchQuestions(@NonNull String keyWords) {
        // Not required
        return null;
    }

}