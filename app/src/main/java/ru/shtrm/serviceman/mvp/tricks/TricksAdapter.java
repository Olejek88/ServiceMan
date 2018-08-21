package ru.shtrm.serviceman.mvp.tricks;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.Trick;
import ru.shtrm.serviceman.interfaces.OnRecyclerViewItemClickListener;
import ru.shtrm.serviceman.util.MainUtil;

public class TricksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull
    private final Context context;

    @NonNull
    private final LayoutInflater inflater;

    @NonNull
    private List<Trick> list;

    @Nullable
    private OnRecyclerViewItemClickListener listener;

    TricksAdapter(@NonNull Context context, @NonNull List<Trick> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TrickViewHolder(inflater.inflate(R.layout.item_trick,
                parent, false), listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Trick item = list.get(position);
        TrickViewHolder pvh = (TrickViewHolder) holder;
        String sDate = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.US).format(item.getDate());
        pvh.textViewDate.setText(sDate);
        pvh.textViewTrickTitle.setTypeface(null, Typeface.BOLD);
        pvh.textViewTrickTitle.setText(item.getTitle());
        if (item.getUser()!=null)
            pvh.textViewAuthor.setText(item.getUser().getName());
            if (item.getUser().getAvatar()!=null)
                pvh.circleImageView.setImageBitmap(MainUtil.getBitmapByPath(
                    MainUtil.getPicturesDirectory(context),item.getUser().getAvatar()));
            else
                pvh.textViewAvatar.setText(item.getTitle().substring(0,1));

        if (item.getImages().size()>0)
            pvh.circleImageView.setImageBitmap(MainUtil.getBitmapByPath(
                    MainUtil.getPicturesDirectory(context),item.getImages().first().getImageName()));
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
    public void updateData(@NonNull List<Trick> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * The view holder of package in home list.
     */
    public class TrickViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        AppCompatTextView textViewTrickTitle;
        AppCompatTextView textViewDate;
        AppCompatTextView textViewAvatar;
        AppCompatTextView textViewAuthor;
        CircleImageView circleImageView;

        private OnRecyclerViewItemClickListener listener;

        TrickViewHolder(View itemView, OnRecyclerViewItemClickListener listener) {
            super(itemView);
            textViewTrickTitle = itemView.findViewById(R.id.textViewTrickTitle);
            textViewDate = itemView.findViewById(R.id.textTrickTime);
            textViewAvatar = itemView.findViewById(R.id.textViewAvatar);
            circleImageView = itemView.findViewById(R.id.circleImageView);
            textViewAuthor = itemView.findViewById(R.id.textTrickAuthor);

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
