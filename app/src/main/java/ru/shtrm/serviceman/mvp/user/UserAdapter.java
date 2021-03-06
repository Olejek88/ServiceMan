package ru.shtrm.serviceman.mvp.user;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.User;

public class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull
    private final Context context;

    @NonNull
    private final LayoutInflater inflater;

    @NonNull
    private List<User> list;

    public UserAdapter(@NonNull Context context, @NonNull List<User> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UsersViewHolder(inflater.inflate(R.layout.item_user,
                parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        User item = list.get(position);
        UsersViewHolder pvh = (UsersViewHolder) holder;
        pvh.textViewTitle.setTypeface(null, Typeface.BOLD);
        pvh.textViewTitle.setText(item.getName());
        pvh.textViewAvatar.setText(item.getName().substring(0,1));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView textViewTitle;
        AppCompatTextView textViewAvatar;
        CircleImageView circleImageView;

        UsersViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewObjectTitle);
            textViewAvatar = itemView.findViewById(R.id.textViewAvatar);
            circleImageView = itemView.findViewById(R.id.circleImageView);
        }
    }

}
