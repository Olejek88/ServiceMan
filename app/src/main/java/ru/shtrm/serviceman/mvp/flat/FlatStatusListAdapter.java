package ru.shtrm.serviceman.mvp.flat;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.FlatStatus;


public class FlatStatusListAdapter extends ArrayAdapter<FlatStatus> {

    private List<FlatStatus> FlatStatuses;

    private final LayoutInflater flater;
    private final int mResource;
    private final int color;

    FlatStatusListAdapter(Context context, @LayoutRes int resource,
                          List<FlatStatus> FlatStatuses,
                          int color) {
        super(context, resource, FlatStatuses);
        mResource = resource;
        flater = LayoutInflater.from(context);
        this.FlatStatuses = FlatStatuses;
        this.color = color;
    }

    @Override
    public int getCount(){
        return FlatStatuses.size();
    }

    @Override
    public FlatStatus getItem(int position){
        return FlatStatuses.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent){
        final View view = flater.inflate(mResource, parent, false);
        FlatStatus item = getItem(position);
        AppCompatTextView textViewTitle;
        textViewTitle = view.findViewById(R.id.spinner_item);
        textViewTitle.setTypeface(null, Typeface.BOLD);
        textViewTitle.setTextColor(color);
        if (item!=null) {
            textViewTitle.setText(item.getTitle());
        }
        return view;
    }
}
