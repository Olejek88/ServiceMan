package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.reactivex.Observable;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import ru.shtrm.serviceman.data.Question;
import ru.shtrm.serviceman.data.source.QuestionsDataSource;
import ru.shtrm.serviceman.realm.RealmHelper;

public class QuestionsLocalDataSource implements QuestionsDataSource {

    @Nullable
    private static QuestionsLocalDataSource INSTANCE;

    // Prevent direct instantiation
    private QuestionsLocalDataSource() {
    }

    // Access this instance for other classes.
    public static QuestionsLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new QuestionsLocalDataSource();
        }
        return INSTANCE;
    }

    // Destroy the instance.
    public static void destroyInstance() {
        INSTANCE = null;
    }

    /**
     * Get the questions in database and sort them in timestamp descending.
     * @return The observable packages from database.
     */
    @Override
    public Observable<List<Question>> getQuestions() {
        Realm rlm = RealmHelper.newRealmInstance();

        return Observable.just(rlm.copyFromRealm(rlm.where(Question.class)
                .findAllSorted("date", Sort.DESCENDING)));
    }

    /**
     * Get a question in database of specific id.
     * @param id The primary key
     *                   or in another words, the package id.
     *                   See {@link Question#id}
     * @return The observable package from database.
     */
    @Override
    public Observable<Question> getQuestion(@NonNull String id) {
        Realm rlm = RealmHelper.newRealmInstance();
        return Observable.just(rlm.copyFromRealm(rlm.where(Question.class)
                .equalTo("id", id)
                .findFirst()));
    }

    /**
     * Save a question to database.
     * @param question The question to save. See {@link Question}
     */
    @Override
    public void saveQuestion(@NonNull final Question question) {
        Realm realm = RealmHelper.newRealmInstance();
        // DO NOT forget begin and commit the transaction.
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(question);
            }
        });
        realm.close();
    }

    /**
     * Delete a question with specific id from database.
     * @param id The primary key of a package
     *                  See {@link Question#id}
     */
    @Override
    public void deleteQuestion(@NonNull String id) {
        Realm realm = RealmHelper.newRealmInstance();
        final Question question = realm.where(Question.class)
                .equalTo("id", id)
                .findFirst();
        if (question != null) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    question.deleteFromRealm();
                }
            });
        }
        realm.close();
    }

    @Override
    public Observable<List<Question>> refreshQuestions() {
        // Not required because the {@link QuestionsRepository} handles the logic
        // of refreshing the packages from all available data source
        return null;
    }

    @Override
    public Observable<Question> refreshQuestion(@NonNull String id) {
        // Not required because the {@link QuestionsRepository} handles the logic
        // of refreshing the packages from all available data source
        return null;
    }

    /**
     * Query the existence of a specific id.
     * @param id the question number to query.
     *                  See {@link Question#id}
     * @return whether the number is in the database.
     */
    @Override
    public boolean isQuestionExist(@NonNull String id) {
        Realm realm = RealmHelper.newRealmInstance();
        RealmResults<Question> results = realm.where(Question.class)
                .equalTo("id", id)
                .findAll();
        return (results != null) && (!results.isEmpty());
    }

    @Override
    public void updateQuestionTitle(@NonNull String id, @NonNull String title) {
        Realm realm = RealmHelper.newRealmInstance();
        Question question = realm.where(Question.class)
                .equalTo("id", id)
                .findFirst();
        if (question != null) {
            realm.beginTransaction();
            question.setTitle(title);
            realm.copyToRealmOrUpdate(question);
            realm.commitTransaction();
        }
        realm.close();
    }

    @Override
    public void updateQuestionText(@NonNull String id, @NonNull String text) {
        Realm realm = RealmHelper.newRealmInstance();
        Question question = realm.where(Question.class)
                .equalTo("id", id)
                .findFirst();
        if (question != null) {
            realm.beginTransaction();
            question.setTitle(text);
            realm.copyToRealmOrUpdate(question);
            realm.commitTransaction();
        }
        realm.close();
    }

    @Override
    public void updateQuestion(@NonNull String id, @NonNull String title, @NonNull String text) {
        Realm realm = RealmHelper.newRealmInstance();
        Question question = realm.where(Question.class)
                .equalTo("id", id)
                .findFirst();
        if (question != null) {
            realm.beginTransaction();
            question.setTitle(title);
            question.setTitle(text);
            realm.copyToRealmOrUpdate(question);
            realm.commitTransaction();
        }
        realm.close();
    }

    /**
     * Set a question of specific id to closed state
     * @param id The primary key or the question id.
     *                  See {@link Question#id}
     * @param closed Read or unread new.
     *                 See {@link Question#closed}
     */

    @Override
    public void updateQuestionClosed(@NonNull String id, final boolean closed) {
        Realm realm = RealmHelper.newRealmInstance();
        final Question question = realm.copyFromRealm(realm.where(Question.class)
                .equalTo("id", id)
                .findFirst());
        if (question != null) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    question.setClosed(closed);
                    realm.copyToRealmOrUpdate(question);
                }
            });
            realm.close();
        }
    }

    @Override
    public Question getQuestionById(@NonNull String id) {
        Realm realm = RealmHelper.newRealmInstance();
        Question question = realm.where(Question.class).equalTo("id", id).findFirst();
        if (question!=null) {
            final Question question2 = realm.copyFromRealm(question);
            realm.close();
            return question2;
        }
        return null;
    }

    @Override
    public Observable<List<Question>> searchQuestions(@NonNull String keyWords) {
        Realm realm = RealmHelper.newRealmInstance();
        return Observable.fromIterable(realm.copyFromRealm(
                realm.where(Question.class)
                        .like("title", "*" + keyWords + "*", Case.INSENSITIVE)
                        .or()
                        .like("text", "*" + keyWords + "*", Case.INSENSITIVE)
                        .findAll()))
                .toList()
                .toObservable();
    }

}
