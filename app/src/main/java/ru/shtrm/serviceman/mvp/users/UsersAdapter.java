package ru.shtrm.serviceman.mvp.users;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.interfaces.OnRecyclerViewItemClickListener;
import ru.shtrm.serviceman.util.MainUtil;

public class UsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements FastScrollRecyclerView.SectionedAdapter{

    @NonNull
    private final Context context;

    @NonNull
    private final LayoutInflater inflater;

    @NonNull
    private List<User> list;

    @Nullable
    private OnRecyclerViewItemClickListener listener;

    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_WITH_HEADER = 1;

    UsersAdapter(@NonNull Context context, @NonNull List<User> list) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_NORMAL) {
            return new NormalViewHolder(inflater.inflate(
                    R.layout.item_user, parent, false), listener);
        }
        return new WithHeaderViewHolder(inflater.inflate(
                R.layout.item_user_with_header, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        User user = list.get(position);
        if (holder instanceof NormalViewHolder) {
            NormalViewHolder cvh = (NormalViewHolder) holder;
            if (user.getAvatar()==null)
                cvh.textViewAvatar.setText(user.getName().substring(0, 1).toUpperCase());
            else
                cvh.avatar.setImageBitmap(MainUtil.getBitmapByPath(
                    MainUtil.getPicturesDirectory(context),user.getAvatar()));
            cvh.textViewUserStats.setText(user.getStats());
            cvh.textViewUserName.setText(user.getName());
        } else if (holder instanceof WithHeaderViewHolder) {
            WithHeaderViewHolder wh = (WithHeaderViewHolder) holder;
            if (user.getAvatar()==null)
                wh.textViewAvatar.setText(user.getName().substring(0, 1).toUpperCase());
            else
                wh.avatar.setImageBitmap(MainUtil.getBitmapByPath(
                        MainUtil.getPicturesDirectory(context),user.getAvatar()));
            wh.textViewUserStats.setText(user.getStats());
            wh.textViewUserName.setText(user.getName());
            wh.stickyHeaderText.setText(getSectionName(position));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        return list.get(position).getName().substring(0,1);
    }

    public class NormalViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        CircleImageView avatar;
        AppCompatTextView textViewUserName;
        AppCompatTextView textViewAvatar;
        AppCompatTextView textViewUserStats;

        private OnRecyclerViewItemClickListener listener;

        NormalViewHolder(View itemView, OnRecyclerViewItemClickListener listener) {
            super(itemView);

            avatar = itemView.findViewById(R.id.imageViewAvatar);
            textViewAvatar = itemView.findViewById(R.id.textViewAvatar);
            textViewUserName = itemView.findViewById(R.id.textViewUserName);
            textViewUserStats = itemView.findViewById(R.id.textViewUserStats);

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

    @Override
    public int getItemViewType(int position) {
        // TODO сделать по новой букве с заголовком
        if (position == 0) {
            return TYPE_WITH_HEADER;
        }
        return TYPE_NORMAL;
    }

    public class WithHeaderViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        CircleImageView avatar;
        AppCompatTextView textViewUserName;
        AppCompatTextView textViewAvatar;
        AppCompatTextView textViewUserStats;
        AppCompatTextView stickyHeaderText;

        private OnRecyclerViewItemClickListener listener;

        WithHeaderViewHolder(View itemView, OnRecyclerViewItemClickListener listener) {
            super(itemView);
            avatar = itemView.findViewById(R.id.imageViewAvatar);
            textViewAvatar = itemView.findViewById(R.id.textViewAvatar);
            textViewUserName = itemView.findViewById(R.id.textViewUserName);
            textViewUserStats = itemView.findViewById(R.id.textViewUserStats);
            stickyHeaderText = itemView.findViewById(R.id.headerText);

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
