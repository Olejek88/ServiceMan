package ru.shtrm.serviceman.mvp.abonents;

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
import ru.shtrm.serviceman.data.Flat;
import ru.shtrm.serviceman.interfaces.OnRecyclerViewItemClickListener;

public class FlatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull
    private final Context context;

    @NonNull
    private final LayoutInflater inflater;

    @NonNull
    private List<Flat> list;

    @Nullable
    private OnRecyclerViewItemClickListener listener;

    FlatAdapter(@NonNull Context context, @NonNull List<Flat> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FlatsViewHolder(inflater.inflate(R.layout.item_abonent,
                parent, false), listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Flat item = list.get(position);
        FlatsViewHolder pvh = (FlatsViewHolder) holder;
        pvh.textViewStatus.setText(item.getFlatStatus().getTitle());
        if (item.getChangedAt()!=null) {
            String sDate = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.US).format(item.getChangedAt());
            pvh.textViewDate.setText(sDate);
        }
        else pvh.textViewDate.setText(R.string.no_last_time);
        pvh.textViewTitle.setTypeface(null, Typeface.BOLD);
        pvh.textViewStatus.setText(item.getFlatStatus().getTitle());
        pvh.textViewTitle.setText(item.getHouse().getStreet().getTitle().concat(", ").
                concat(item.getHouse().getTitle()).concat(", ").concat(item.getTitle()));
        pvh.textViewImage.setText(item.getTitle().substring(0,1));
        // TODO выдергивать последнее фото из фото?
/*
        if (item.getUser()!=null)
            if (item.getUser().getAvatar()!=null)
                pvh.circleImageView.setImageBitmap(MainUtil.getBitmapByPath(
                    MainUtil.getPicturesDirectory(context),item.getUser().getAvatar()));
            else
                pvh.textViewAvatar.setText(item.getTitle().substring(0,1));
*/
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
    public void updateData(@NonNull List<Flat> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * The view holder of package in home list.
     */
    public class FlatsViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnCreateContextMenuListener {

        AppCompatTextView textViewTitle;
        AppCompatTextView textViewDate;
        AppCompatTextView textViewStatus;
        AppCompatTextView textViewImage;
        CircleImageView circleImageView;

        private OnRecyclerViewItemClickListener listener;

        public FlatsViewHolder(View itemView, OnRecyclerViewItemClickListener listener) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewObjectTitle);
            textViewStatus = itemView.findViewById(R.id.textObjectStatus);
            textViewDate = itemView.findViewById(R.id.textObjectTime);
            textViewImage = itemView.findViewById(R.id.textViewImage);
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
