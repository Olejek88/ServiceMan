package ru.shtrm.serviceman.data.source.local;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.Sort;
import ru.shtrm.serviceman.data.Answer;
import ru.shtrm.serviceman.data.Image;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.data.source.AnswersDataSource;
import ru.shtrm.serviceman.realm.RealmHelper;

public class AnswersLocalDataSource implements AnswersDataSource {

    @Nullable
    private static AnswersLocalDataSource INSTANCE;

    // Prevent direct instantiation
    private AnswersLocalDataSource() {
    }

    // Access this instance for other classes.
    public static AnswersLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AnswersLocalDataSource();
        }
        return INSTANCE;
    }

    // Destroy the instance.
    public static void destroyInstance() {
        INSTANCE = null;
    }

    /**
     * Get the Answers in database and sort them in timestamp descending.
     * @return The observable packages from database.
     */
    @Override
    public Observable<List<Answer>> getAnswers(String questionId) {
        Realm rlm = RealmHelper.newRealmInstance();

        return Observable.just(rlm.copyFromRealm(rlm.where(Answer.class)
                .equalTo("question.id", questionId)
                .findAllSorted("date", Sort.DESCENDING)));
    }

    /**
     * Get a Answer in database of specific id.
     * @param id The primary key
     *                   or in another words, the package id.
     *                   See {@link Answer#id}
     * @return The observable package from database.
     */
    @Override
    public Observable<Answer> getAnswer(@NonNull String id) {
        Realm rlm = RealmHelper.newRealmInstance();
        return Observable.just(rlm.copyFromRealm(rlm.where(Answer.class)
                .equalTo("id", id)
                .findFirst()));
    }

    /**
     * Save a Answer to database.
     * @param Answer The Answer to save. See {@link Answer}
     */
    @Override
    public void saveAnswer(@NonNull final Answer answer) {
        Realm realm = RealmHelper.newRealmInstance();
        // DO NOT forget begin and commit the transaction.
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(answer);
            }
        });

        updateUserRating(answer.getUser(),realm);
        realm.close();
    }

    /**
     * Delete a Answer with specific id from database.
     * @param id The primary key of a package
     *                  See {@link Answer#id}
     */
    @Override
    public void deleteAnswer(@NonNull String id) {
        Realm realm = RealmHelper.newRealmInstance();
        final Answer Answer = realm.where(Answer.class)
                .equalTo("id", id)
                .findFirst();
        if (Answer != null) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Answer.deleteFromRealm();
                }
            });
        }
        realm.close();
    }

    @Override
    public Observable<List<Answer>> refreshAnswers(String questionId) {
        // Not required because the {@link AnswersRepository} handles the logic
        // of refreshing the packages from all available data source
        return null;
    }

    @Override
    public void updateAnswerTitle(@NonNull String id, @NonNull String title) {
        Realm realm = RealmHelper.newRealmInstance();
        Answer Answer = realm.where(Answer.class)
                .equalTo("id", id)
                .findFirst();
        if (Answer != null) {
            realm.beginTransaction();
            Answer.setTitle(title);
            realm.copyToRealmOrUpdate(Answer);
            realm.commitTransaction();
        }
        realm.close();
    }

    @Override
    public void updateAnswerText(@NonNull String id, @NonNull String text) {
        Realm realm = RealmHelper.newRealmInstance();
        Answer answer = realm.where(Answer.class)
                .equalTo("id", id)
                .findFirst();
        if (answer != null) {
            realm.beginTransaction();
            answer.setTitle(text);
            realm.copyToRealmOrUpdate(answer);
            realm.commitTransaction();
        }
        realm.close();
    }

    @Override
    public Answer getAnswerById(@NonNull String id) {
        Realm realm = RealmHelper.newRealmInstance();
        Answer answer = realm.where(Answer.class).equalTo("id", id).findFirst();
        if (answer!=null)
            return realm.copyFromRealm(answer);
        return null;
    }


    @Override
    public Observable<List<Answer>> searchAnswers(@NonNull String keyWords) {
        Realm realm = RealmHelper.newRealmInstance();
        return Observable.fromIterable(realm.copyFromRealm(
                realm.where(Answer.class)
                        .like("title", "*" + keyWords + "*", Case.INSENSITIVE)
                        .or()
                        .like("text", "*" + keyWords + "*", Case.INSENSITIVE)
                        .findAll()))
                .toList()
                .toObservable();
    }

    /**
     * Save a Answer with images to database.
     * @param answer The Answer to save. See {@link Answer}
     * @param images The images to save. See {@link Image}
     */
    @Override
    public void saveAnswer(@NonNull final Answer answer, @NonNull final ArrayList<Image> images) {
        Realm realm = RealmHelper.newRealmInstance();
        final RealmList<Image> list = new RealmList<>();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                list.addAll(images);
                answer.setImages(list);
                realm.copyToRealmOrUpdate(answer);
            }
        });
        realm.close();
    }

    /**
     * Vote to answer Up.
     * @param answer The Answer to change. See {@link Answer}
     */
    @Override
    public void voteUpAnswer(@NonNull final Answer answer) {
        Realm realm = RealmHelper.newRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                answer.setVoteUp(answer.getVoteUp()+1);
                realm.copyToRealmOrUpdate(answer);
            }
        });

        updateUserRating(answer.getUser(),realm);
        realm.close();
    }

    /**
     * Vote to answer Down.
     * @param answer The Answer to change. See {@link Answer}
     */
    @Override
    public void voteDownAnswer(@NonNull final Answer answer) {
        Realm realm = RealmHelper.newRealmInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                answer.setVoteDown(answer.getVoteDown()+1);
                realm.copyToRealmOrUpdate(answer);
            }
        });

        updateUserRating(answer.getUser(),realm);
        realm.close();
    }

    private void updateUserRating (final User user, Realm realm) {
        RealmList<Answer> answers = user.getAnswers();
        Answer answerU;
        double initialRating=0;
        initialRating = user.getQuestions().size()+user.getTricks().size()*5;
        for (int a=0; a<answers.size(); a++) {
            answerU = answers.get(a);
            initialRating+= (answerU.getVoteUp() - answerU.getVoteDown())*0.5;
        }
        final String rating = String.valueOf(initialRating);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                user.setRating(rating);
                realm.copyToRealmOrUpdate(user);
            }
        });
    }
}
