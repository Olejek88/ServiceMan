package ru.shtrm.serviceman.mvp.questions;

import android.support.annotation.NonNull;

import java.util.List;

import ru.shtrm.serviceman.data.Question;
import ru.shtrm.serviceman.mvp.BasePresenter;
import ru.shtrm.serviceman.mvp.BaseView;

public interface QuestionsContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showEmptyView(boolean toShow);

        void showQuestions(@NonNull List<Question> list);
    }

    interface Presenter extends BasePresenter {

        void loadQuestions();

        void refreshQuestions();

        void setFiltering(@NonNull QuestionFilterType requestType);

        QuestionFilterType getFiltering();

        void deleteQuestion(int position);
    }

}
