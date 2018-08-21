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
import ru.shtrm.serviceman.data.Question;

public class QuestionsRepository implements QuestionsDataSource {

    @Nullable
    private static QuestionsRepository INSTANCE = null;

    @NonNull
    private final QuestionsDataSource questionsRemoteDataSource;

    @NonNull
    private final QuestionsDataSource questionsLocalDataSource;

    private Map<String, Question> cachedQuestions;

    // Prevent direct instantiation
    private QuestionsRepository(@NonNull QuestionsDataSource questionsRemoteDataSource,
                               @NonNull QuestionsDataSource questionsLocalDataSource) {
        this.questionsRemoteDataSource = questionsRemoteDataSource;
        this.questionsLocalDataSource = questionsLocalDataSource;
    }

    // The access for other classes.
    public static QuestionsRepository getInstance(@NonNull QuestionsDataSource questionsRemoteDataSource,
                                                 @NonNull QuestionsDataSource questionsLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new QuestionsRepository(questionsRemoteDataSource, questionsLocalDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    /**
     * It is designed to gotten the questions from both database
     * and network. Which are faster then return them.
     * @return Questions from {@link ru.shtrm.serviceman.data.source.local.QuestionsLocalDataSource}.
     */
    @Override
    public Observable<List<Question>> getQuestions() {
        if (cachedQuestions != null) {
            return Observable.fromCallable(new Callable<List<Question>>() {
                @Override
                public List<Question> call() throws Exception {
                    List<Question> arrayList = new ArrayList<Question>(cachedQuestions.values());
                    // Sort by the timestamp to make the list shown in a descend way
                    Collections.sort(arrayList, new Comparator<Question>() {
                        @Override
                        public int compare(Question o1, Question o2) {
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
            cachedQuestions = new LinkedHashMap<>();
            // Return the cached questions.
            return questionsLocalDataSource
                    .getQuestions()
                    .concatMap(new Function<List<Question>, ObservableSource<List<Question>>>() {
                        @Override
                        public ObservableSource<List<Question>> apply(List<Question> questions) throws Exception {
                            return Observable
                                    .fromIterable(questions)
                                    .doOnNext(new Consumer<Question>() {
                                        @Override
                                        public void accept(Question aQuestion) throws Exception {
                                            cachedQuestions.put(aQuestion.getId(), aQuestion);
                                        }
                                    })
                                    .toList()
                                    .toObservable();
                        }
                    });
        }
    }

    /**
     * Get a question of specific number from data source.
     * @param id The primary key or the question id. See {@link Question}.
     * @return The question.
     */
    @Override
    public Observable<Question> getQuestion(@NonNull final String id) {
        Question cachedQuestion = getQuestionWithNumber(id);
        if (cachedQuestion != null) {
            return Observable.just(cachedQuestion);
        }
        return getQuestionWithNumberFromLocalRepository(id);
    }

    /**
     * Get a question of specific id from data source.
     * @param id The primary key or the question id. See {@link Question}.
     * @return The question.
     */
    @Override
    public Question getQuestionById(@NonNull final String id) {
        return questionsLocalDataSource.getQuestionById(id);
    }

    /**
     * Save the question to data source and cache.
     * It is supposed to save it to database and network too.
     * But we have no cloud(The account system) yet.
     * It may change either.
     * @param question The question to save. See more @{@link Question}.
     */
    @Override
    public void saveQuestion(@NonNull Question question) {
        questionsLocalDataSource.saveQuestion(question);
        if (cachedQuestions == null) {
            cachedQuestions = new LinkedHashMap<>();
        }
        if (!isQuestionExist(question.getId())) {
            cachedQuestions.put(question.getId(), question);
        }
    }

    /**
     * Delete a question from data source and cache.
     * @param questionId The primary id or in another words, the question number.
     *                  See more @{@link Question#id}.
     */
    @Override
    public void deleteQuestion(@NonNull String questionId) {
        questionsLocalDataSource.deleteQuestion(questionId);
        cachedQuestions.remove(questionId);
    }

    /**
     * Refresh the questions.
     * Just call the remote data source and it will make everything done.
     * @return The observable questions.
     */
    @Override
    public Observable<List<Question>> refreshQuestions() {
        return questionsRemoteDataSource
                .refreshQuestions()
                .concatMap(new Function<List<Question>, ObservableSource<List<Question>>>() {
                    @Override
                    public ObservableSource<List<Question>> apply(List<Question> questions) throws Exception {

                        return Observable
                                .fromIterable(questions)
                                .doOnNext(new Consumer<Question>() {
                                    @Override
                                    public void accept(Question aQuestion) throws Exception {
                                        Question question = cachedQuestions.get(aQuestion.getId());
                                        if (question != null) {
                                            question.setPushable(true);
                                        }
                                    }
                                })
                                .toList()
                                .toObservable();
                    }
                });
    }

    /**
     * Refresh one question.
     * Just call the remote data source and it will make everything done.
     * @param questionId The primary key(The question number).
     *                  See more @{@link Question#id}.
     * @return The observable question.
     */
    @Override
    public Observable<Question> refreshQuestion(@NonNull final String questionId) {
        return questionsRemoteDataSource
                .refreshQuestion(questionId)
                .concatMap(new Function<Question, ObservableSource<Question>>() {
                    @Override
                    public ObservableSource<Question> apply(Question p) throws Exception {
                        return Observable
                                .just(p)
                                .doOnNext(new Consumer<Question>() {
                                    @Override
                                    public void accept(Question aQuestion) throws Exception {
                                        // TODO empty body
                                    }
                                });
                    }
                });
    }

    /**
     * Checkout out the existence of a question with specific number.
     * @param questionId The primary key or in another words, the question number.
     *                  See more @{@link Question#id}.
     * @return Whether the question exists.
     */
    @Override
    public boolean isQuestionExist(@NonNull String questionId) {
        return getQuestionWithNumber(questionId) != null;
    }

    @Override
    public void updateQuestionTitle(@NonNull String questionId, @NonNull String title) {
        Question question = getQuestionWithNumber(questionId);
        if (question != null) {
            question.setTitle(title);
        }
        questionsLocalDataSource.updateQuestionTitle(questionId, title);
    }

    @Override
    public void updateQuestionText(@NonNull String questionId, @NonNull String text) {
        Question question = getQuestionWithNumber(questionId);
        if (question != null) {
            question.setText(text);
        }
        questionsLocalDataSource.updateQuestionTitle(questionId, text);
    }

    @Override
    public void updateQuestion(@NonNull String questionId, @NonNull String text, @NonNull String title) {
        Question question = getQuestionWithNumber(questionId);
        if (question != null) {
            question.setText(text);
            question.setTitle(title);
        }
        questionsLocalDataSource.updateQuestionTitle(questionId, text);
    }

    @Override
    public void updateQuestionClosed(@NonNull String id, boolean closed) {
        Question question = getQuestionWithNumber(id);
        if (question != null) {
            question.setClosed(closed);
        }
        questionsLocalDataSource.updateQuestionClosed(id, closed);
    }

    @Override
    public Observable<List<Question>> searchQuestions(@NonNull String keyWords) {
        // Do nothing but just let local data source handle it.
        return questionsLocalDataSource.searchQuestions(keyWords);
    }

    /**
     * Get a question with question number.
     * @param id The question id. See more @{@link Question#id}.
     * @return The question with specific number.
     */
    @Nullable
    private Question getQuestionWithNumber(@NonNull String id) {
        if (cachedQuestions == null || cachedQuestions.isEmpty()) {
            return null;
        } else {
            return cachedQuestions.get(id);
        }
    }

    /**
     * As the function name says, get a question from local repository.
     * @param id The question number.
     * @return The question.
     */
    @Nullable
    private Observable<Question> getQuestionWithNumberFromLocalRepository(@NonNull final String id) {
        return questionsLocalDataSource
                .getQuestion(id)
                .doOnNext(new Consumer<Question>() {
                    @Override
                    public void accept(Question aQuestion) throws Exception {
                        if (cachedQuestions == null) {
                            cachedQuestions = new LinkedHashMap<>();
                        }
                        cachedQuestions.put(id, aQuestion);
                    }
                });
    }

}
