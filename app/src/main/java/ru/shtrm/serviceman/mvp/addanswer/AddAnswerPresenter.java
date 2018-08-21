package ru.shtrm.serviceman.mvp.addanswer;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import ru.shtrm.serviceman.data.Answer;
import ru.shtrm.serviceman.data.Image;
import ru.shtrm.serviceman.data.Question;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.data.source.AnswersDataSource;
import ru.shtrm.serviceman.data.source.ImagesDataSource;
import ru.shtrm.serviceman.data.source.QuestionsDataSource;
import ru.shtrm.serviceman.data.source.UsersDataSource;

public class AddAnswerPresenter implements AddAnswerContract.Presenter{

    @NonNull
    private final AddAnswerContract.View view;

    @NonNull
    private final QuestionsDataSource questionsDataSource;

    @NonNull
    private final AnswersDataSource answersDataSource;

    @NonNull
    private final ImagesDataSource imagesDataSource;

    @NonNull
    private final UsersDataSource usersDataSource;

    @NonNull
    private CompositeDisposable compositeDisposable;

    public AddAnswerPresenter(@NonNull QuestionsDataSource dataSource,
                              @NonNull AnswersDataSource answerDataSource,
                                @NonNull UsersDataSource userDataSource,
                                @NonNull ImagesDataSource imagesDataSource,
                                @NonNull AddAnswerContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
        this.questionsDataSource = dataSource;
        this.answersDataSource = answerDataSource;
        this.imagesDataSource = imagesDataSource;
        this.usersDataSource = userDataSource;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe() {}

    @Override
    public void unsubscribe() {
        compositeDisposable.clear();
    }

    @Override
    public void saveAnswer(Context context, String id, String title, String text, User user,
                             ArrayList<Image> images, Question question) {
        compositeDisposable.clear();
        checkAnswer(context, id, title, text, user, images, question);
    }

    private void checkAnswer(Context context, final String id, final String title, final String text,
                             User user, ArrayList<Image> images, Question question) {
        // TODO id - это uuid так что не сработает
        if (questionsDataSource.isQuestionExist(id)) {
            return;
        }
        Answer answer = new Answer();
        answer.setId(java.util.UUID.randomUUID().toString());
        answer.setUser(user);
        answer.setTitle(title);
        answer.setText(text);
        answer.setDate(new Date());
        answer.setVoteDown(0);
        answer.setVoteUp(1);
        for (int count=0;count < images.size();count++) {
            images.get(count).setTitle(title);
        }
        answersDataSource.saveAnswer(answer, images);
        usersDataSource.addAnswer(answer, user);
        question.getAnswers().add(answer);
        questionsDataSource.saveQuestion(question);
        view.showQuestion();
    }
}
