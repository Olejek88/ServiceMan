package ru.shtrm.serviceman.mvp.images;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.Image;
import ru.shtrm.serviceman.util.MainUtil;
import static ru.shtrm.serviceman.mvp.images.ImageGridAdapter.IMAGE_ID;

public class ImagePreviewActivity extends AppCompatActivity {

    //private ImagesContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_image_full);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

/*
        final ImagesDataSource imagesDataSource =
            ImagesRepository.getInstance(ImagesLocalDataSource.getInstance());
*/
        Intent intent = getIntent();
/*
        if (intent!=null && imagesDataSource!=null) {
            String imageId = intent.getStringExtra(IMAGE_ID);
             if (imageId !=null) {
                 Image image = imagesDataSource.getImage(imageId);
                 if (image !=null) {
                     ImageView imageView = findViewById(R.id.fullImage);
                     imageView.setImageBitmap(
                             MainUtil.getBitmapByPath(
                                     MainUtil.getPicturesDirectory
                                             (getApplicationContext()), image.getImageName()));
                 }
             }
        }
*/

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

}
