package ru.shtrm.serviceman.mvp.images;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.Image;
import ru.shtrm.serviceman.util.MainUtil;

public final class ImageGridAdapter extends BaseAdapter {
    @NonNull
    private final Context context;

    private static final String IMAGE_ID = "IMAGE_ID";

    private List<Image> mItems;
    private final LayoutInflater mInflater;

    public ImageGridAdapter(@NonNull Context context, List<Image> images) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.mItems = images;
    }

    @Override
    public int getCount() {
        if (mItems!=null)
            return mItems.size();
        else
            return 0;
    }

    @Override
    public Image getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return -1;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        ImageView picture;

        if (v == null) {
            v = mInflater.inflate(R.layout.item_image, viewGroup, false);
            v.setTag(R.id.gridViewImage, v.findViewById(R.id.gridViewImage));
        }
        picture = (ImageView) v.getTag(R.id.gridViewImage);
        final Image item = getItem(i);
        Bitmap bitmap = MainUtil.getBitmapByPath(
                MainUtil.getPicturesDirectory(context),item.getImageName());
        if (bitmap!=null)
            picture.setImageBitmap(bitmap);
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ImagePreviewActivity.class);
                intent.putExtra(IMAGE_ID,item.getId());
                v.getContext().startActivity(intent);
            }
        });

        return v;
    }
}
