package ru.shtrm.serviceman.mvp.addtrick;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;

import ru.shtrm.serviceman.data.Image;
import ru.shtrm.serviceman.data.Trick;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.data.source.ImagesDataSource;
import ru.shtrm.serviceman.data.source.TricksDataSource;
import ru.shtrm.serviceman.data.source.UsersDataSource;

public class AddTrickPresenter implements AddTrickContract.Presenter{

    @NonNull
    private final AddTrickContract.View view;

    @NonNull
    private final TricksDataSource tricksDataSource;

    @NonNull
    private final ImagesDataSource imagesDataSource;

    @NonNull
    private final UsersDataSource usersDataSource;

    AddTrickPresenter(@NonNull TricksDataSource dataSource,
                      @NonNull UsersDataSource userDataSource,
                      @NonNull ImagesDataSource imagesDataSource,
                      @NonNull AddTrickContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
        this.tricksDataSource = dataSource;
        this.usersDataSource = userDataSource;
        this.imagesDataSource = imagesDataSource;
    }

    @Override
    public void subscribe() {}

    @Override
    public void unsubscribe() {}

    @Override
    public void saveTrick(Context context, String id, String title, String text, User user,
                             ArrayList<Image> images) {
        checkTrick(context, id, title, text, user, images);
    }

    private void checkTrick(Context context, final String id, final String title,
                               final String text, User user, ArrayList<Image> images) {
        Trick trick = new Trick();
        trick.setId(java.util.UUID.randomUUID().toString());
        trick.setUser(user);
        trick.setTitle(title);
        trick.setText(text);
        trick.setDate(new Date());
        trick.setVoteDown(0);
        trick.setVoteUp(0);

        for (int count=0;count < images.size();count++) {
            images.get(count).setTitle(title);
        }
        tricksDataSource.saveTrick(trick, images);
        usersDataSource.addTrick(trick, user);
        view.showTricksList();
    }

/*
    private void saveImage(Context context, String title, String imageName) {
        Bitmap bitmap = MainUtil.getBitmapByPath(MainUtil.getPicturesDirectory(context),imageName);
        if (bitmap!=null) {
            Image image = new Image();
            image.setId(java.util.UUID.randomUUID().toString());
            image.setImageName(imageName);
            image.setDate(new Date());
            image.setTitle(title);
            //imagesDataSource.saveImage(image);
        }
    }
*/

}
