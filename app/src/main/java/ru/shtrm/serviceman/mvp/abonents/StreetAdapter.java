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
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.Street;
import ru.shtrm.serviceman.interfaces.OnRecyclerViewItemClickListener;

public class StreetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull
    private final Context context;

    @NonNull
    private final LayoutInflater inflater;

    @NonNull
    private List<Street> list;

    @Nullable
    private OnRecyclerViewItemClickListener listener;
    
    StreetAdapter(@NonNull Context context, @NonNull List<Street> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StreetsViewHolder(inflater.inflate(R.layout.item_street,
                parent, false), listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Street item = list.get(position);
        StreetsViewHolder pvh = (StreetsViewHolder) holder;
        pvh.textViewTitle.setTypeface(null, Typeface.BOLD);
        pvh.textViewTitle.setText(item.getCity().getTitle().concat(", ул.").concat(item.getTitle()));
        pvh.textViewImage.setText(item.getTitle().substring(0,1));
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
    public void updateData(@NonNull List<Street> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * The view holder of package in home list.
     */
    public class StreetsViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnCreateContextMenuListener {

        AppCompatTextView textViewTitle;
        AppCompatTextView textViewImage;
        CircleImageView circleImageView;

        private OnRecyclerViewItemClickListener listener;

        public StreetsViewHolder(View itemView, OnRecyclerViewItemClickListener listener) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewObjectTitle);
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
