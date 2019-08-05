package ru.shtrm.serviceman.mvp.task;

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
import ru.shtrm.serviceman.data.TaskVerdict;


public class TaskVerdictListAdapter extends ArrayAdapter<TaskVerdict> {

    private final LayoutInflater flater;
    private final int mResource;
    private final int color;
    private List<TaskVerdict> taskVerdicts;
    private Context context;

    TaskVerdictListAdapter(Context context, @LayoutRes int resource,
                           List<TaskVerdict> taskVerdicts,
                           int color) {
        super(context, resource, taskVerdicts);
        this.context = context;
        mResource = resource;
        flater = LayoutInflater.from(context);
        this.taskVerdicts = taskVerdicts;
        this.color = color;
    }

    @Override
    public int getCount() {
        return taskVerdicts.size();
    }

    @Override
    public TaskVerdict getItem(int position) {
        return taskVerdicts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public @NonNull
    View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent) {
        final View view = flater.inflate(mResource, parent, false);
        TaskVerdict item = getItem(position);
        AppCompatTextView textViewTitle;
        textViewTitle = view.findViewById(R.id.spinner_item);
        textViewTitle.setTypeface(null, Typeface.BOLD);
        textViewTitle.setTextColor(context.getResources().getColor(color));
        if (item != null) {
            textViewTitle.setText(item.getTitle());
        }
        return view;
    }
}
