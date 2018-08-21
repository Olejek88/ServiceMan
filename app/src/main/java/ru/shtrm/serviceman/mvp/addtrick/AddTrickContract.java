package ru.shtrm.serviceman.mvp.addtrick;

import android.content.Context;

import java.util.ArrayList;

import ru.shtrm.serviceman.data.Image;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.mvp.BasePresenter;
import ru.shtrm.serviceman.mvp.BaseView;

public interface AddTrickContract {

    interface View extends BaseView<Presenter> {

        void showTitleError();

        void showTricksList();
    }

    interface Presenter extends BasePresenter {

        void saveTrick(Context context, String id, String title, String text,
                          User user, ArrayList<Image> images);

    }

}
