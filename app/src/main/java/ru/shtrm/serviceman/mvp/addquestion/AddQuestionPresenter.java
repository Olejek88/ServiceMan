package ru.shtrm.serviceman.mvp.addquestion;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;

import io.reactivex.disposables.CompositeDisposable;
import ru.shtrm.serviceman.data.Image;
import ru.shtrm.serviceman.data.Question;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.data.source.ImagesDataSource;
import ru.shtrm.serviceman.data.source.QuestionsDataSource;
import ru.shtrm.serviceman.data.source.UsersDataSource;
import ru.shtrm.serviceman.util.MainUtil;

public class AddQuestionPresenter implements AddQuestionContract.Presenter{

    @NonNull
    private final AddQuestionContract.View view;

    @NonNull
    private final QuestionsDataSource questionsDataSource;

    @NonNull
    private final ImagesDataSource imagesDataSource;

    @NonNull
    private final UsersDataSource usersDataSource;

    @NonNull
    private CompositeDisposable compositeDisposable;

    public AddQuestionPresenter(@NonNull QuestionsDataSource dataSource,
                                @NonNull UsersDataSource userDataSource,
                                @NonNull ImagesDataSource imagesDataSource,
                                @NonNull AddQuestionContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
        this.questionsDataSource = dataSource;
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
    public void saveQuestion(Context context, String id, String title, String text, User user,
                             ArrayList<Image> images) {
        compositeDisposable.clear();
        checkQuestion(context, id, title, text, user, images);
    }

    private void checkQuestion(Context context, final String id, final String title,
                               final String text, User user, ArrayList<Image> images) {
        // TODO id - это uuid так что не сработает
        if (questionsDataSource.isQuestionExist(id)) {
            return;
        }
        Question question = new Question();
        question.setId(java.util.UUID.randomUUID().toString());
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
        questionsDataSource.saveQuestion(question);
        user.getQuestions().add(question);
        usersDataSource.saveUser(user);
        view.showQuestionsList();
    }
}
