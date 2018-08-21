package ru.shtrm.serviceman.mvp.search;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import ru.shtrm.serviceman.data.Question;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.data.source.QuestionsRepository;
import ru.shtrm.serviceman.data.source.UsersRepository;

public class SearchPresenter implements SearchContract.Presenter{

    @NonNull
    private SearchContract.View view;

    @NonNull
    private QuestionsRepository questionsRepository;

    @NonNull
    private UsersRepository usersRepository;

    private CompositeDisposable compositeDisposable;

    private String queryWords = null;

    public SearchPresenter(@NonNull SearchContract.View view,
                           @NonNull QuestionsRepository questionsRepository,
                           @NonNull UsersRepository usersRepository) {
        this.view = view;
        this.questionsRepository = questionsRepository;
        this.usersRepository = usersRepository;
        this.view.setPresenter(this);
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe() {
        search(queryWords);
    }

    @Override
    public void unsubscribe() {
        compositeDisposable.clear();
    }

    @Override
    public void search(String keyWords) {

        if (keyWords == null || keyWords.isEmpty()) {
            view.showResult(null, null);
            return;
        }
        queryWords = keyWords;

        Observable<List<User>> userObservable = usersRepository
                .searchUsers(keyWords)
                .subscribeOn(Schedulers.io());

        Observable<List<Question>> questionObservable = questionsRepository
                .searchQuestions(keyWords)
                .subscribeOn(Schedulers.io());

        Disposable disposable = Observable
                .zip(questionObservable, userObservable, new BiFunction<List<Question>,
                        List<User>, QuestionsAndUsersPairs>() {
                    @Override
                    public QuestionsAndUsersPairs apply(List<Question> questions, List<User> users)
                            throws Exception {
                        return new QuestionsAndUsersPairs(questions, users);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<QuestionsAndUsersPairs>() {
                    @Override
                    public void onNext(QuestionsAndUsersPairs value) {
                        view.showResult(value.getQuestions(), value.getUsers());
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
