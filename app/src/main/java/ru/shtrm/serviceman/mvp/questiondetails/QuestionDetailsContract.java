package ru.shtrm.serviceman.mvp.questiondetails;

import android.support.annotation.NonNull;

import ru.shtrm.serviceman.data.Answer;
import ru.shtrm.serviceman.data.Question;
import ru.shtrm.serviceman.mvp.BasePresenter;
import ru.shtrm.serviceman.mvp.BaseView;

public interface QuestionDetailsContract {

    interface View extends BaseView<Presenter> {

        void showNetworkError();

        void showQuestionDetails(@NonNull Question question);

        void exit();

    }

    interface Presenter extends BasePresenter {

        void refreshQuestion();

        void deleteQuestion();

        String getQuestionTitle();

        void updateQuestionTitle(String newTitle);

        void setAnswerVoteUp(Answer answer);

        void setAnswerVoteDown(Answer answer);
    }

}
