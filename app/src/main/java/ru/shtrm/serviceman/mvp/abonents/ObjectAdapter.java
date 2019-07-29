package ru.shtrm.serviceman.mvp.abonents;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.Task;
import ru.shtrm.serviceman.data.ZhObject;
import ru.shtrm.serviceman.interfaces.OnRecyclerViewItemClickListener;

public class ObjectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull
    private final Context context;

    @NonNull
    private final LayoutInflater inflater;

    @NonNull
    private List<ZhObject> list;

    @Nullable
    private OnRecyclerViewItemClickListener listener;

    ObjectAdapter(@NonNull Context context, @NonNull List<ZhObject> list) {
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
        ZhObject item = list.get(position);
        FlatsViewHolder pvh = (FlatsViewHolder) holder;
        pvh.textViewType.setText(item.getObjectType().getTitle());
        pvh.textViewTitle.setTypeface(null, Typeface.BOLD);
        pvh.textViewTitle.setText(item.getHouse().getStreet().getTitle().concat(", ").
                concat(item.getHouse().getNumber()).concat(", ").concat(item.getTitle()));
        pvh.textViewImage.setText(item.getTitle().substring(0, 1));
        //Task completeTaskCount = realm
        // TODO выдергивать последнее фото из фото?
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
     *
     * @param list The data.
     */
    public void updateData(@NonNull List<ZhObject> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * The view holder of package in home list.
     */
    public class FlatsViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        AppCompatTextView textViewTitle;
        AppCompatTextView textViewType;
        AppCompatTextView textViewImage;
        CircleImageView circleImageView;
        LinearLayout layoutObjectItem;

        private OnRecyclerViewItemClickListener listener;

        FlatsViewHolder(View itemView, OnRecyclerViewItemClickListener listener) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewObjectTitle);
            textViewType= itemView.findViewById(R.id.textObjectType);
            textViewImage = itemView.findViewById(R.id.textViewImage);
            circleImageView = itemView.findViewById(R.id.circleImageView);
            layoutObjectItem = itemView.findViewById(R.id.layoutObjectItem);

            this.listener = listener;
            itemView.setOnClickListener(this);
            //itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (this.listener != null) {
                listener.OnItemClick(v, getLayoutPosition());
            }
        }
    }
}
