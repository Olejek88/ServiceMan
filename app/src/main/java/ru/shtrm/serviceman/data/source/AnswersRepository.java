package ru.shtrm.serviceman.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import ru.shtrm.serviceman.data.Answer;
import ru.shtrm.serviceman.data.Image;

public class AnswersRepository implements AnswersDataSource {
    
    @Nullable
    private static AnswersRepository INSTANCE = null;

    @NonNull
    private final AnswersDataSource answersRemoteDataSource;

    @NonNull
    private final AnswersDataSource answersLocalDataSource;

    private Map<String, Answer> cachedAnswers;

    // Prevent direct instantiation
    private AnswersRepository(@NonNull AnswersDataSource answersRemoteDataSource,
                              @NonNull AnswersDataSource answersLocalDataSource) {
        this.answersRemoteDataSource = answersRemoteDataSource;
        this.answersLocalDataSource = answersLocalDataSource;
    }

    // The access for other classes.
    public static AnswersRepository getInstance(@NonNull AnswersDataSource AnswersRemoteDataSource,
                                                @NonNull AnswersDataSource AnswersLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new AnswersRepository(AnswersRemoteDataSource, AnswersLocalDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    /**
     * It is designed to gotten the Answers from both database
     * and network. Which are faster then return them.
     * @return Answers from {@link ru.shtrm.serviceman.data.source.local.AnswersLocalDataSource}.
     */
    @Override
    public Observable<List<Answer>> getAnswers(String questionId) {
        if (cachedAnswers != null) {
            return Observable.fromCallable(new Callable<List<Answer>>() {
                @Override
                public List<Answer> call() throws Exception {
                    List<Answer> arrayList = new ArrayList<>(cachedAnswers.values());
                    // Sort by the timestamp to make the list shown in a descend way
                    Collections.sort(arrayList, new Comparator<Answer>() {
                        @Override
                        public int compare(Answer o1, Answer o2) {
                            if (o1.getDate().compareTo(o2.getDate())>0) {
                                return -1;
                            } else if (o1.getDate().compareTo(o2.getDate())<0) {
                                return 1;
                            }
                            return 0;
                        }
                    });
                    return arrayList;
                }
            });
        } else {
            cachedAnswers = new LinkedHashMap<>();
            // Return the cached Answers.
            return answersLocalDataSource
                    .getAnswers(questionId)
                    .concatMap(new Function<List<Answer>, ObservableSource<List<Answer>>>() {
                        @Override
                        public ObservableSource<List<Answer>> apply(List<Answer> Answers) throws Exception {
                            return Observable
                                    .fromIterable(Answers)
                                    .doOnNext(new Consumer<Answer>() {
                                        @Override
                                        public void accept(Answer aAnswer) throws Exception {
                                            cachedAnswers.put(aAnswer.getId(), aAnswer);
                                        }
                                    })
                                    .toList()
                                    .toObservable();
                        }
                    });
        }
    }

    /**
     * Get a Answer of specific number from data source.
     * @param id The primary key or the Answer id. See {@link Answer}.
     * @return The Answer.
     */
    @Override
    public Observable<Answer> getAnswer(@NonNull final String id) {
        Answer cachedAnswer = getAnswerWithNumber(id);
        if (cachedAnswer != null) {
            return Observable.just(cachedAnswer);
        }
        return getAnswerWithNumberFromLocalRepository(id);
    }

    /**
     * Get a Answer of specific id from data source.
     * @param id The primary key or the Answer id. See {@link Answer}.
     * @return The Answer.
     */
    @Override
    public Answer getAnswerById(@NonNull final String id) {
        return answersLocalDataSource.getAnswerById(id);
    }

    /**
     * Save the Answer to data source and cache.
     * It is supposed to save it to database and network too.
     * But we have no cloud(The account system) yet.
     * It may change either.
     * @param Answer The Answer to save. See more @{@link Answer}.
     */
    @Override
    public void saveAnswer(@NonNull Answer Answer) {
        answersLocalDataSource.saveAnswer(Answer);
        if (cachedAnswers == null) {
            cachedAnswers = new LinkedHashMap<>();
        }
        cachedAnswers.put(Answer.getId(), Answer);
    }

    /**
     * Delete a Answer from data source and cache.
     * @param AnswerId The primary id or in another words, the Answer number.
     *                  See more @{@link Answer#id}.
     */
    @Override
    public void deleteAnswer(@NonNull String AnswerId) {
        answersLocalDataSource.deleteAnswer(AnswerId);
        cachedAnswers.remove(AnswerId);
    }

    /**
     * Refresh the Answers.
     * Just call the remote data source and it will make everything done.
     * @return The observable Answers.
     */
    @Override
    public Observable<List<Answer>> refreshAnswers(String questionId) {
        return answersRemoteDataSource
                .refreshAnswers(questionId)
                .concatMap(new Function<List<Answer>, ObservableSource<List<Answer>>>() {
                    @Override
                    public ObservableSource<List<Answer>> apply(List<Answer> Answers) throws Exception {
                        return Observable
                                .fromIterable(Answers)
                                .toList()
                                .toObservable();
                    }
                });
    }

    @Override
    public void updateAnswerTitle(@NonNull String answerId, @NonNull String title) {
        if (getAnswerWithNumber(answerId) != null) {
            getAnswerWithNumber(answerId).setTitle(title);
        }
        answersLocalDataSource.updateAnswerTitle(answerId, title);
    }

    @Override
    public void updateAnswerText(@NonNull String answerId, @NonNull String text) {
        if (getAnswerWithNumber(answerId) != null) {
            getAnswerWithNumber(answerId).setText(text);
        }
        answersLocalDataSource.updateAnswerTitle(answerId, text);
    }

    @Override
    public Observable<List<Answer>> searchAnswers(@NonNull String keyWords) {
        // Do nothing but just let local data source handle it.
        return answersLocalDataSource.searchAnswers(keyWords);
    }

    @Override
    public void saveAnswer(@NonNull final Answer answer, @NonNull final ArrayList<Image> images) {
        answersLocalDataSource.saveAnswer(answer,images);
    }


    /**
     * Get a Answer with Answer number.
     * @param id The Answer id. See more @{@link Answer#id}.
     * @return The Answer with specific number.
     */
    @Nullable
    private Answer getAnswerWithNumber(@NonNull String id) {
        if (cachedAnswers == null || cachedAnswers.isEmpty()) {
            return null;
        } else {
            return cachedAnswers.get(id);
        }
    }

    /**
     * As the function name says, get a Answer from local repository.
     * @param id The Answer number.
     * @return The Answer.
     */
    @Nullable
    private Observable<Answer> getAnswerWithNumberFromLocalRepository(@NonNull final String id) {
        return answersLocalDataSource
                .getAnswer(id)
                .doOnNext(new Consumer<Answer>() {
                    @Override
                    public void accept(Answer aAnswer) throws Exception {
                        if (cachedAnswers == null) {
                            cachedAnswers = new LinkedHashMap<>();
                        }
                        cachedAnswers.put(id, aAnswer);
                    }
                });
    }

    @Override
    public void voteUpAnswer(@NonNull Answer answer) {
        answersLocalDataSource.voteUpAnswer(answer);
    }

    @Override
    public void voteDownAnswer(@NonNull Answer answer) {
        answersLocalDataSource.voteDownAnswer(answer);
    }

}
