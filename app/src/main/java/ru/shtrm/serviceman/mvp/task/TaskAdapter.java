package ru.shtrm.serviceman.mvp.task;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.Task;
import ru.shtrm.serviceman.data.WorkStatus;
import ru.shtrm.serviceman.interfaces.OnRecyclerViewItemClickListener;

public class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull
    private final LayoutInflater inflater;

    @NonNull
    private List<Task> list;

    @Nullable
    private OnRecyclerViewItemClickListener listener;

    public TaskAdapter(@NonNull Context context, @NonNull List<Task> list) {
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TaskViewHolder(inflater.inflate(R.layout.item_task,
                parent, false), listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Task item = list.get(position);
        TaskViewHolder pvh = (TaskViewHolder) holder;
        pvh.textTaskTitle.setText(item.getTaskTemplate().getTitle());
        pvh.textTaskObject.setText(item.getEquipment().getObject().getFullTitle());
        Date lDate = item.getTaskDate();
        String sDate="-",eDate="-";
        if (lDate != null && lDate.after(new Date(100000))) {
            sDate = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.US).format(lDate);
        }
        lDate = item.getDeadlineDate();
        if (lDate != null && lDate.after(new Date(100000))) {
            eDate = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.US).format(lDate);
        }
        pvh.textTaskDate.setText("Назначен: "+sDate+" | "+"Срок: "+eDate);
        if (item.getWorkStatus().getUuid().equals(WorkStatus.Status.COMPLETE))
            pvh.statusTaskImage.setImageResource(R.drawable.circle_green);
        else
            pvh.statusTaskImage.setImageResource(R.drawable.circle_red);
        pvh.textTaskTitle.setTypeface(null, Typeface.BOLD);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * Update the data. Keep the data is the latest.
     * @param list The data.
     */
    public void updateData(@NonNull List<Task> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        AppCompatTextView textTaskTitle;
        AppCompatTextView textTaskDate;
        AppCompatTextView textTaskObject;
        CircleImageView statusTaskImage;

        private OnRecyclerViewItemClickListener listener;

        TaskViewHolder(View itemView, OnRecyclerViewItemClickListener listener) {
            super(itemView);
            textTaskTitle = itemView.findViewById(R.id.textViewTaskTitle);
            textTaskObject = itemView.findViewById(R.id.textViewTaskObject);
            textTaskDate = itemView.findViewById(R.id.textTaskDate);
            statusTaskImage = itemView.findViewById(R.id.circleTaskStatusIW);

            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (this.listener != null) {
                listener.OnItemClick(v, getLayoutPosition());
            }
        }
    }
}
