package ru.shtrm.serviceman.mvp.addquestion;

import android.content.Context;

import java.util.ArrayList;

import ru.shtrm.serviceman.data.Image;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.mvp.BasePresenter;
import ru.shtrm.serviceman.mvp.BaseView;

public interface AddQuestionContract {

    interface View extends BaseView<Presenter> {

        void showTitleExistError();

        void showTitleError();

        void showQuestionsList();

    }

    interface Presenter extends BasePresenter {

        void saveQuestion(Context context, String id, String title, String text,
                          User user, ArrayList<Image> images);

    }

}
