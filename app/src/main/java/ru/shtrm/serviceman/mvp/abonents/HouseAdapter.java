package ru.shtrm.serviceman.mvp.abonents;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.Measure;
import ru.shtrm.serviceman.data.source.local.MeasureLocalDataSource;
import ru.shtrm.serviceman.interfaces.OnRecyclerViewItemClickListener;

public class HouseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull
    private final Context context;

    @NonNull
    private final LayoutInflater inflater;

    @NonNull
    private List<House> list;

    @Nullable
    private OnRecyclerViewItemClickListener listener;

    public HouseAdapter(@NonNull Context context, @NonNull List<House> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HousesViewHolder(inflater.inflate(R.layout.item_abonent,
                parent, false), listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        House item = list.get(position);
        HousesViewHolder pvh = (HousesViewHolder) holder;
        pvh.textObjectType.setText(item.getHouseType().getTitle());
        pvh.textViewTitle.setTypeface(null, Typeface.BOLD);
        if (item.getStreet()!=null)
            pvh.textViewTitle.setText(item.getStreet().getTitle().concat(", ").concat(item.getNumber()));
        if (item.getNumber().length() > 1) {
            pvh.textViewImage.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16.0f);
            pvh.textViewImage.setText(item.getNumber().substring(0, 2));
        } else pvh.textViewImage.setText(item.getNumber().substring(0, 1));
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
    public void updateData(@NonNull List<House> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * The view holder of package in home list.
     */
    public class HousesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        AppCompatTextView textViewTitle;
        AppCompatTextView textObjectType;
        AppCompatTextView textViewImage;
        CircleImageView circleImageView;
        LinearLayout layoutObjectItem;

        private OnRecyclerViewItemClickListener listener;

        HousesViewHolder(View itemView, OnRecyclerViewItemClickListener listener) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewObjectTitle);
            textObjectType = itemView.findViewById(R.id.textObjectType);
            textViewImage = itemView.findViewById(R.id.textViewImage);
            circleImageView = itemView.findViewById(R.id.circleImageView);
            layoutObjectItem = itemView.findViewById(R.id.layoutObjectItem);

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
