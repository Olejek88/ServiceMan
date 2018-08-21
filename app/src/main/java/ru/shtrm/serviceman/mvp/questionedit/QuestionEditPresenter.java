package ru.shtrm.serviceman.mvp.questionedit;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.RealmList;
import ru.shtrm.serviceman.data.Answer;
import ru.shtrm.serviceman.data.Image;
import ru.shtrm.serviceman.data.Question;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.data.source.QuestionsDataSource;
import ru.shtrm.serviceman.data.source.QuestionsRepository;

public class QuestionEditPresenter implements QuestionEditContract.Presenter {

    @NonNull
    private QuestionEditContract.View view;

    @NonNull
    private QuestionsRepository questionsRepository;

    @NonNull
    private CompositeDisposable compositeDisposable;

    private String questionTitle;
    private String questionText;
    private ArrayList<Image> images;

    @NonNull
    private String questionId;

    public QuestionEditPresenter(@NonNull String id,
                                 @NonNull QuestionsRepository questionsRepository,
                                 @NonNull QuestionEditContract.View questionDetailView) {
        this.questionId = id;
        this.view = questionDetailView;
        this.questionsRepository = questionsRepository;
        compositeDisposable = new CompositeDisposable();
        if (questionDetailView!=null)
            this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {
        openDetail();
    }

    @Override
    public void unsubscribe() {
        compositeDisposable.clear();
    }

    /**
     * Load data from repository.
     */
    private void openDetail() {
/*
        Question currentQuestion;
        if (questionId!=null) {
            currentQuestion = questionsRepository.getQuestionById(questionId);
            view.showQuestionEdit(currentQuestion);
        }
*/
        Disposable disposable = questionsRepository
                .getQuestion(questionId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Question>() {
                    @Override
                    public void onNext(Question value) {
                        questionTitle = value.getTitle();
                        questionText = value.getText();
                        if (value.getImages().size()>0)
                            images = new ArrayList<>(value.getImages());
                        view.showQuestionEdit(value);
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

    @Override
    public void saveQuestion(String id, String title, String text, Date date, boolean closed,
                             RealmList<Image> images, RealmList<Answer> answers, User user) {
/*
        Question question = new Question();
        question.setId(id);
        question.setPushable(true);
        question.setClosed(false);
        question.setUser(user);
        question.setTitle(title);
        question.setText(text);
        question.setDate(new Date());
        for (int count=0;count < images.size();count++) {
            images.get(count).setTitle(title);
        }
        question.setImages(imagesDataSource.saveImages(images));
*/
        questionsRepository.updateQuestion(id,text,title);
        //user.getQuestions().add(question);
        //usersDataSource.saveUser(user);
        view.showQuestion();
    }
}
