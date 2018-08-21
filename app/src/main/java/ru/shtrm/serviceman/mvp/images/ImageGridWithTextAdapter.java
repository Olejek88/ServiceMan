package ru.shtrm.serviceman.mvp.images;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.Image;
import ru.shtrm.serviceman.util.MainUtil;

public final class ImageGridWithTextAdapter extends BaseAdapter {
    @NonNull
    private final Context context;

    private List<Image> mItems;
    private final LayoutInflater mInflater;

    public ImageGridWithTextAdapter(@NonNull Context context, List<Image> images) {
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
        TextView description;

        if (v == null) {
            v = mInflater.inflate(R.layout.item_image_text, viewGroup, false);
            v.setTag(R.id.gridViewImage, v.findViewById(R.id.gridViewImage));
            v.setTag(R.id.gridViewText, v.findViewById(R.id.gridViewText));
        }
        picture = (ImageView) v.getTag(R.id.gridViewImage);
        description = (TextView) v.getTag(R.id.gridViewText);

        Image item = getItem(i);
        Bitmap bitmap = MainUtil.getBitmapByPath(
                MainUtil.getPicturesDirectory(context),item.getImageName());
        if (bitmap!=null) {
            picture.setImageBitmap(bitmap);
            // TODO реализовать переход по клику на совет или как определить откуда изображение
            picture.setOnClickListener(null);
        }
        description.setText(item.getTitle());
        return v;
    }
}
