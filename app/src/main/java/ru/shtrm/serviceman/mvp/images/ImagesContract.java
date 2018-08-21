package ru.shtrm.serviceman.mvp.images;

import java.util.List;

import ru.shtrm.serviceman.data.Image;
import ru.shtrm.serviceman.mvp.BasePresenter;
import ru.shtrm.serviceman.mvp.BaseView;

public interface ImagesContract {

    interface View extends BaseView<Presenter> {

        void showImagesList();

    }

    interface Presenter extends BasePresenter {
        List<Image> getImages();
        Image getImage(String id);
    }

}
