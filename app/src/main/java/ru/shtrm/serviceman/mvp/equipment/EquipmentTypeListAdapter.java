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
import ru.shtrm.serviceman.data.EquipmentType;


public class EquipmentTypeListAdapter extends ArrayAdapter<EquipmentType> {

    private List<EquipmentType> equipmentTypes;

    private final LayoutInflater flater;
    private final int mResource;

    EquipmentTypeListAdapter(Context context, @LayoutRes int resource, List<EquipmentType> equipmentTypes) {
        super(context, resource, equipmentTypes);
        mResource = resource;
        flater = LayoutInflater.from(context);
        this.equipmentTypes = equipmentTypes;
    }

    @Override
    public int getCount(){
        return equipmentTypes.size();
    }

    @Override
    public EquipmentType getItem(int position){
        return equipmentTypes.get(position);
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
        EquipmentType item = getItem(position);
        AppCompatTextView textViewTitle;
        textViewTitle = view.findViewById(R.id.spinner_item);
        textViewTitle.setTypeface(null, Typeface.BOLD);
        if (item!=null) {
            textViewTitle.setText(item.getTitle());
        }
        return view;
    }
}
