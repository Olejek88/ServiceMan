package ru.shtrm.serviceman.mvp.questions;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import ru.shtrm.serviceman.data.Question;
import ru.shtrm.serviceman.data.source.QuestionsRepository;

public class QuestionsPresenter implements QuestionsContract.Presenter {

    @NonNull
    private final QuestionsContract.View view;

    @NonNull
    private final QuestionsRepository questionsRepository;

    @NonNull
    private final CompositeDisposable compositeDisposable;

    @NonNull
    private QuestionFilterType currentFiltering = QuestionFilterType.ALL_QUESTIONS;

    @Nullable
    private Question mayRemovequestion;

    public QuestionsPresenter(@NonNull QuestionsContract.View view,
                              @NonNull QuestionsRepository questionsRepository) {
        this.view = view;
        this.questionsRepository = questionsRepository;
        compositeDisposable = new CompositeDisposable();
        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {
        loadQuestions();
    }

    @Override
    public void unsubscribe() {
        compositeDisposable.clear();
    }

    /**
     * Get all the questions from repository and show on view.
     */
    @Override
    public void loadQuestions() {
        compositeDisposable.clear();
        Disposable disposable = questionsRepository
                .getQuestions()
                .flatMap(new Function<List<Question>, ObservableSource<Question>>() {
                    @Override
                    public ObservableSource<Question> apply(List<Question> list) throws Exception {
                        return Observable.fromIterable(list);
                    }
                })
                .filter(new Predicate<Question>() {
                    @Override
                    public boolean test(Question aQuestion) throws Exception {
                        boolean state = aQuestion.isClosed();
                        switch (currentFiltering) {
                            case CLOSED_QUESTIONS:
                                return !state;
                            case OPENED_QUESTIONS:
                                return state;
                            case ALL_QUESTIONS:
                                return true;
                            default:
                                return true;
                        }
                    }
                })
                .toList()
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Question>>() {
                    @Override
                    public void onNext(List<Question> value) {
                        view.showQuestions(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showEmptyView(true);
                        //view.setLoadingIndicator(false);
                    }

                    @Override
                    public void onComplete() {
//                        view.setLoadingIndicator(false);
                    }
                });
        compositeDisposable.add(disposable);
    }

    /**
     * Force update the questions data by accessing network.
     */
    @Override
    public void refreshQuestions() {
        /*
        Disposable disposable = questionsRepository
                .refreshQuestions()
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Question>>() {
                    @Override
                    public void onNext(List<Question> value) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //view.setLoadingIndicator(false);
                    }

                    @Override
                    public void onComplete() {
                        //view.setLoadingIndicator(false);
                        loadQuestions();
                    }
                });
        compositeDisposable.add(disposable);
        */
    }

    /**
     * Sets the current question filtering type.
     *
     * @param requestType Can be {@link QuestionFilterType#ALL_QUESTIONS},
     *                    {@link QuestionFilterType#OPENED_QUESTIONS}, or
     *                    {@link QuestionFilterType#CLOSED_QUESTIONS}
     */
    @Override
    public void setFiltering(@NonNull QuestionFilterType requestType) {
        currentFiltering = requestType;
    }

    /**
     * Get current question filtering type.
     * @return Current filtering type.
     */
    @Override
    public QuestionFilterType getFiltering() {
        return currentFiltering;
    }

    /**
     * Delete a question from the repository.
     * But this action could revoked.
     * recovering details
     * @param position The position of the question which may
     *                 be delete completely.
     */
    @Override
    public void deleteQuestion(final int position) {
        if (position < 0) {
            return;
        }
        Disposable disposable = questionsRepository
                .getQuestions()
                .flatMap(new Function<List<Question>, ObservableSource<Question>>() {
                    @Override
                    public ObservableSource<Question> apply(List<Question> list) throws Exception {
                        return Observable.fromIterable(list);
                    }
                })
                .filter(new Predicate<Question>() {
                    @Override
                    public boolean test(Question aQuestion) throws Exception {
                        boolean state = aQuestion.isClosed();
                        switch (currentFiltering) {
                            case OPENED_QUESTIONS:
                                return !state;
                            case CLOSED_QUESTIONS:
                                return state;
                            case ALL_QUESTIONS:
                                return true;
                            default:
                                return true;
                        }
                    }
                })
                .toList()
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Question>>() {
                    @Override
                    public void onNext(List<Question> value) {
                        mayRemovequestion = value.get(position);
                        questionsRepository.deleteQuestion(mayRemovequestion.getId());
                        value.remove(position);
                        view.showQuestions(value);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        compositeDisposable.add(disposable);
    }
}
