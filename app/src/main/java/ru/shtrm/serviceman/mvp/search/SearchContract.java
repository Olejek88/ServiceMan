package ru.shtrm.serviceman.mvp.search;

import java.util.List;

import ru.shtrm.serviceman.data.Question;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.mvp.BasePresenter;
import ru.shtrm.serviceman.mvp.BaseView;

public interface SearchContract {

    interface View extends BaseView<Presenter> {

        void showResult(List<Question> questions, List<User> users);

    }

    interface Presenter extends BasePresenter {

        void search(String keyWords);

    }

}
