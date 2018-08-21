package ru.shtrm.serviceman.mvp.questionedit;

import android.support.annotation.NonNull;

import java.util.Date;

import io.realm.RealmList;
import ru.shtrm.serviceman.data.Answer;
import ru.shtrm.serviceman.data.Image;
import ru.shtrm.serviceman.data.Question;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.mvp.BasePresenter;
import ru.shtrm.serviceman.mvp.BaseView;

public interface QuestionEditContract {

    interface View extends BaseView<Presenter> {

        void showQuestionEdit(@NonNull Question question);

        void showQuestion();

        void showErrorMsg();

        void exit();

    }

    interface Presenter extends BasePresenter {

        void saveQuestion(String id, String title, String text, Date date, boolean closed,
                          RealmList<Image> images,
                          RealmList<Answer> answers,
                          User user);
    }

}
