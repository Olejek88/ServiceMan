package ru.shtrm.serviceman.mvp.trickdetails;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import io.realm.RealmList;
import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.Answer;
import ru.shtrm.serviceman.data.Trick;
import ru.shtrm.serviceman.mvp.images.ImageGridAdapter;

public class TrickDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull
    private final Context context;

    @NonNull
    private final LayoutInflater inflater;

    @NonNull
    private final Trick aTrick;

    TrickDetailsAdapter(@NonNull Context context, @NonNull Trick trick) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.aTrick = trick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(
                inflater.inflate(R.layout.trick_details, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder vh = (ItemViewHolder) holder;
        vh.textViewTitle.setText(aTrick.getTitle());
        vh.textViewText.setText(aTrick.getText());
        vh.textViewAuthor.setText(aTrick.getUser().getName());
        String sDate =
                new SimpleDateFormat("dd.MM.yy HH:mm", Locale.US).format(aTrick.getDate());
        vh.textViewDate.setText(sDate);
        vh.photoGridView.setAdapter(new ImageGridAdapter(context, aTrick.getImages()));
        vh.photoGridView.invalidateViews();
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    /**
     * DO NOT cast a RealmList to List or you will got some unexpected bugs.
     * @param list The RealmList.
     */
    public void updateData(@NonNull RealmList<Answer> list) {
        notifyDataSetChanged();
    }

    /**
     * A header view holder of recyclerView.
     */
    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView textViewAuthor;
        TextView textViewText;
        TextView textViewTitle;
        TextView textViewDate;
        GridView photoGridView;

        ItemViewHolder(View itemView) {
            super(itemView);
            textViewAuthor = itemView.findViewById(R.id.trickAuthor);
            textViewText = itemView.findViewById(R.id.trickText);
            textViewTitle = itemView.findViewById(R.id.trickTitle);
            textViewDate = itemView.findViewById(R.id.trickDate);
            photoGridView = itemView.findViewById(R.id.gridview);
        }
    }
}
