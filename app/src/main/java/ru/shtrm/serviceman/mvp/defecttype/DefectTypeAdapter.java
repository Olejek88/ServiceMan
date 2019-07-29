package ru.shtrm.serviceman.mvp.defecttype;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ru.shtrm.serviceman.data.DefectType;

public class DefectTypeAdapter extends BaseAdapter {

    @NonNull
    private List<DefectType> list;

    public DefectTypeAdapter(@NonNull List<DefectType> list) {
        super();
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public DefectType getItem(int position) {
        DefectType item = null;
        if (list.size() > 0) {
            item = list.get(position);
        }

        return item;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        DefectTypeViewHolder pvh;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
            pvh = new DefectTypeViewHolder();
            pvh.textViewDefectTypeTitle = convertView.findViewById(android.R.id.text1);
            convertView.setTag(pvh);
        } else {
            pvh = (DefectTypeViewHolder) convertView.getTag();
        }

        DefectType item = list.get(position);
        pvh.textViewDefectTypeTitle.setText(item.getTitle());
        return convertView;
    }

    public class DefectTypeViewHolder {
        TextView textViewDefectTypeTitle;
    }
}
