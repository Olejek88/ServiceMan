package ru.shtrm.serviceman.mvp.alarm;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.Alarm;
import ru.shtrm.serviceman.interfaces.OnRecyclerViewItemClickListener;

public class AlarmAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull
    private final Context context;

    @NonNull
    private final LayoutInflater inflater;

    @NonNull
    private List<Alarm> list;

    @Nullable
    private OnRecyclerViewItemClickListener listener;

    public AlarmAdapter(@NonNull Context context, @NonNull List<Alarm> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AlarmViewHolder(inflater.inflate(R.layout.item_alarm,
                parent, false), listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Alarm item = list.get(position);
        AlarmViewHolder pvh = (AlarmViewHolder) holder;
        pvh.textAlarmStatus.setText(item.getAlarmStatus().getTitle());
        if (item.getDate()!=null) {
            String sDate = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.US).format(item.getDate());
            pvh.textAlarmDate.setText(sDate);
        }
        else pvh.textAlarmDate.setText(R.string.no_last_time);
        pvh.textAlarmTitle.setTypeface(null, Typeface.BOLD);
        pvh.textAlarmTitle.setText(item.getAlarmType().getTitle());
        pvh.textAlarmImage.setText(item.getAlarmType().getTitle().substring(0, 1));
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
    public void updateData(@NonNull List<Alarm> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class AlarmViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnCreateContextMenuListener {

        AppCompatTextView textAlarmTitle;
        AppCompatTextView textAlarmDate;
        AppCompatTextView textAlarmStatus;
        AppCompatTextView textAlarmImage;
        CircleImageView circleImageView;

        private OnRecyclerViewItemClickListener listener;

        public AlarmViewHolder(View itemView, OnRecyclerViewItemClickListener listener) {
            super(itemView);
            textAlarmTitle = itemView.findViewById(R.id.textAlarmTitle);
            textAlarmStatus = itemView.findViewById(R.id.textAlarmStatus);
            textAlarmDate = itemView.findViewById(R.id.textAlarmTime);
            textAlarmImage = itemView.findViewById(R.id.textViewImage);
            circleImageView = itemView.findViewById(R.id.circleImageView);

            this.listener = listener;
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (this.listener != null) {
                listener.OnItemClick(v, getLayoutPosition());
            }
        }

        /**
         * Create the context menu.
         * @param menu The context menu.
         * @param v The view.
         * @param menuInfo The menu information.
         */
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            if (menu != null) {
                //((MainActivity)context).setSelectedPackageId(list.get(getLayoutPosition()).getId());
                //menu.add(Menu.NONE, R.id.action_share, 0, R.string.share);
            }
        }
    }

}
