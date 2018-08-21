package ru.shtrm.serviceman.mvp.questiondetails;

import android.content.Context;
import android.content.Intent;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.Answer;
import ru.shtrm.serviceman.interfaces.OnRecyclerViewItemClickListener;
import ru.shtrm.serviceman.mvp.addanswer.AddAnswerActivity;
import ru.shtrm.serviceman.mvp.questionedit.QuestionEditActivity;
import ru.shtrm.serviceman.util.MainUtil;

public class AnswersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull
    QuestionDetailsContract.Presenter presenter;

    @NonNull
    private final Context context;

    @NonNull
    private final LayoutInflater inflater;

    @NonNull
    private List<Answer> list;
    
    @Nullable
    private OnRecyclerViewItemClickListener listener;

    AnswersAdapter(@NonNull Context context, @NonNull List<Answer> list,
                   @NonNull QuestionDetailsContract.Presenter presenter) {
        this.context = context;
        this.presenter = presenter;
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AnswerViewHolder(inflater.inflate(R.layout.item_answer,
                parent, false), listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Answer item = list.get(position);
        AnswerViewHolder pvh = (AnswerViewHolder) holder;
        String sDate = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.US).format(item.getDate());
        pvh.textViewDate.setText(sDate);
        pvh.textViewAnswerTitle.setTypeface(null, Typeface.BOLD);
        pvh.textViewAnswerTitle.setText(item.getTitle());
        pvh.textViewText.setText(item.getText());
        if (item.getUser()!=null)
            if (item.getUser().getAvatar()!=null)
                pvh.circleImageView.setImageBitmap(MainUtil.getBitmapByPath(
                    MainUtil.getPicturesDirectory(context),item.getUser().getAvatar()));
            else
                pvh.textViewAvatar.setText(item.getTitle().substring(0,1));

        // TODO добавить подсветку звезд
        // TODO добавить обработчики нажатий
        pvh.voteUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.setAnswerVoteUp(item);
            }
        });

        pvh.voteDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.setAnswerVoteDown(item);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * The view holder of package in home list.
     */
    public class AnswerViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnCreateContextMenuListener {

        AppCompatTextView textViewAnswerTitle;
        AppCompatTextView textViewDate;
        AppCompatTextView textViewText;
        AppCompatTextView textViewAvatar;
        ImageView imageViewAnswer;
        CircleImageView circleImageView;

        ImageView voteUp, voteDown;
        ArrayList <ImageView> stars;

        private OnRecyclerViewItemClickListener listener;

        public AnswerViewHolder(final View itemView, OnRecyclerViewItemClickListener listener) {
            super(itemView);
            //textViewAnswerTitle = itemView.findViewById(R.id.answerTitle);
            textViewDate = itemView.findViewById(R.id.answerDate);
            textViewText = itemView.findViewById(R.id.answerText);
            textViewAvatar = itemView.findViewById(R.id.textViewAvatar);
            imageViewAnswer = itemView.findViewById(R.id.imageViewAnswer);
            circleImageView = itemView.findViewById(R.id.circleImageView);

            voteUp = itemView.findViewById(R.id.answerRatePlusThumb);
            voteDown = itemView.findViewById(R.id.answerRateMinusThumb);
            stars = new ArrayList<>(5);
            stars.add((ImageView) itemView.findViewById(R.id.star1));
            stars.add((ImageView) itemView.findViewById(R.id.star2));
            stars.add((ImageView) itemView.findViewById(R.id.star3));
            stars.add((ImageView) itemView.findViewById(R.id.star4));
            stars.add((ImageView) itemView.findViewById(R.id.star5));



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
