package ru.shtrm.serviceman.mvp.equipment;

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
import ru.shtrm.serviceman.data.EquipmentStatus;


public class EquipmentStatusListAdapter extends ArrayAdapter<EquipmentStatus> {

    private List<EquipmentStatus> equipmentStatuses;

    private Context context;
    private final LayoutInflater flater;
    private final int mResource;
    private final int color;

    EquipmentStatusListAdapter(Context context, @LayoutRes int resource,
                               List<EquipmentStatus> equipmentStatuses,
                               int color) {
        super(context, resource, equipmentStatuses);
        this.context = context;
        mResource = resource;
        flater = LayoutInflater.from(context);
        this.equipmentStatuses = equipmentStatuses;
        this.color = color;
    }

    @Override
    public int getCount(){
        return equipmentStatuses.size();
    }

    @Override
    public EquipmentStatus getItem(int position){
        return equipmentStatuses.get(position);
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
        EquipmentStatus item = getItem(position);
        AppCompatTextView textViewTitle;
        textViewTitle = view.findViewById(R.id.spinner_item);
        textViewTitle.setTypeface(null, Typeface.BOLD);
        textViewTitle.setTextColor(context.getResources().getColor(color));
        if (item!=null) {
            textViewTitle.setText(item.getTitle());
        }
        return view;
    }
}
