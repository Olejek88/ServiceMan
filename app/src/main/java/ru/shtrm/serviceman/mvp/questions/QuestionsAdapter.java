package ru.shtrm.serviceman.mvp.questions;

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
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.Question;
import ru.shtrm.serviceman.interfaces.OnRecyclerViewItemClickListener;
import ru.shtrm.serviceman.util.MainUtil;

public class QuestionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull
    private final Context context;

    @NonNull
    private final LayoutInflater inflater;

    @NonNull
    private List<Question> list;

    @Nullable
    private OnRecyclerViewItemClickListener listener;

    private String[] packageStatus;

    QuestionsAdapter(@NonNull Context context, @NonNull List<Question> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
        packageStatus = context.getResources().getStringArray(R.array.question_status);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new QuestionViewHolder(inflater.inflate(R.layout.item_question,
                parent, false), listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Question item = list.get(position);
        QuestionViewHolder pvh = (QuestionViewHolder) holder;
        if (item.isClosed())
            pvh.textViewStatus.setText(String.valueOf(packageStatus[1]));
        else
            pvh.textViewStatus.setText(String.valueOf(packageStatus[0]));
        String sDate = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.US).format(item.getDate());
        pvh.textViewDate.setText(sDate);
        pvh.textViewQuestionTitle.setTypeface(null, Typeface.BOLD);
        pvh.textViewQuestionTitle.setText(item.getTitle());
        if (item.getUser()!=null)
            if (item.getUser().getAvatar()!=null)
                pvh.circleImageView.setImageBitmap(MainUtil.getBitmapByPath(
                    MainUtil.getPicturesDirectory(context),item.getUser().getAvatar()));
            else
                pvh.textViewAvatar.setText(item.getTitle().substring(0,1));
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
    public void updateData(@NonNull List<Question> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * The view holder of package in home list.
     */
    public class QuestionViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnCreateContextMenuListener {

        AppCompatTextView textViewQuestionTitle;
        AppCompatTextView textViewDate;
        AppCompatTextView textViewStatus;
        AppCompatTextView textViewAvatar;
        ImageView imageViewAnswer;
        CircleImageView circleImageView;
        LinearLayout layoutMain;
        View wrapperView;

        private OnRecyclerViewItemClickListener listener;

        public QuestionViewHolder(View itemView, OnRecyclerViewItemClickListener listener) {
            super(itemView);
            textViewQuestionTitle = itemView.findViewById(R.id.textViewQuestionTitle);
            textViewStatus = itemView.findViewById(R.id.textQuestionStatus);
            textViewDate = itemView.findViewById(R.id.textQuestionTime);
            textViewAvatar = itemView.findViewById(R.id.textViewAvatar);
            imageViewAnswer = itemView.findViewById(R.id.imageViewAnswer);
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
