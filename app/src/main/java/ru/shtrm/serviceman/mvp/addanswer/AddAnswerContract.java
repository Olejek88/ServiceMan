package ru.shtrm.serviceman.mvp.addanswer;

import android.content.Context;

import java.util.ArrayList;

import ru.shtrm.serviceman.data.Image;
import ru.shtrm.serviceman.data.Question;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.mvp.BasePresenter;
import ru.shtrm.serviceman.mvp.BaseView;

public interface AddAnswerContract {

    interface View extends BaseView<Presenter> {
        void showQuestion();
        void showTitleError();
    }

    interface Presenter extends BasePresenter {

        void saveAnswer(Context context, String id, String title, String text,
                        User user, ArrayList<Image> images, Question question);

    }

}
