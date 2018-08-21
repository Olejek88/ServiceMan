package ru.shtrm.serviceman.mvp.questiondetails;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.realm.RealmList;
import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.component.Timeline;
import ru.shtrm.serviceman.data.Answer;
import ru.shtrm.serviceman.data.Question;
import ru.shtrm.serviceman.mvp.images.ImageGridAdapter;
import ru.shtrm.serviceman.util.MainUtil;

public class QuestionDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull
    private final Context context;

    @NonNull
    private final LayoutInflater inflater;

    private QuestionDetailsContract.Presenter presenter;

    @NonNull
    private final Question aQuestion;

    private final List<Answer> answers;

    private static final int TYPE_HEADER = 0x00;
    private static final int TYPE_NORMAL = 0x01;
    private static final int TYPE_START = 0x02;
    private static final int TYPE_FINISH = 0x03;
    private static final int TYPE_SINGLE = 0x04;

    QuestionDetailsAdapter(@NonNull Context context, @NonNull Question question,
                           @NonNull QuestionDetailsContract.Presenter presenter) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.aQuestion = question;
        this.presenter = presenter;
        this.answers = new ArrayList<>();

        if (question.getAnswers().size()>0)
            this.answers.addAll(question.getAnswers());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return new HeaderViewHolder(
                    inflater.inflate(R.layout.item_details_header, parent, false));
        }
        return new AnswersViewHolder(
                inflater.inflate(R.layout.item_answer, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == TYPE_HEADER) {
            HeaderViewHolder vh = (HeaderViewHolder) holder;
            vh.textViewTitle.setText(aQuestion.getTitle());
            vh.textViewText.setText(aQuestion.getText());
            String sDate =
                    new SimpleDateFormat("dd.MM.yy HH:mm", Locale.US).format(aQuestion.getDate());
            vh.textViewAuthor.setText(aQuestion.getUser().getName());
            vh.textViewDate.setText(sDate);
            vh.photoGridView.setAdapter(new ImageGridAdapter(context, aQuestion.getImages()));
            vh.photoGridView.invalidateViews();
        } else {
            final Answer item = answers.get(position - 1);
            AnswersViewHolder viewHolder = (AnswersViewHolder) holder;
            String sDate =
                    new SimpleDateFormat("dd.MM.yy HH:mm", Locale.US).format(item.getDate());

/*
            if (getItemViewType(position) == TYPE_SINGLE) {
                viewHolder.timeLine.setStartLine(null);
                viewHolder.timeLine.setFinishLine(null);
            } else if (getItemViewType(position) == TYPE_START) {
                viewHolder.timeLine.setStartLine(null);
            } else if (getItemViewType(position) == TYPE_FINISH) {
                viewHolder.timeLine.setFinishLine(null);
            }
*/

            //answerPhotoGridView
            viewHolder.answerPhotoGridView.setAdapter(new ImageGridAdapter(context, item.getImages()));
            viewHolder.answerPhotoGridView.invalidateViews();
            //viewHolder.textViewDate.setText(sDate);
            viewHolder.textViewDate.setText(item.getUser().getName().concat(" ").concat(sDate));
            viewHolder.textViewText.setText(item.getText());
            viewHolder.imageViewAuthor.setImageBitmap(
                    MainUtil.getBitmapByPath(
                            MainUtil.getPicturesDirectory(context),item.getUser().getAvatar()));
            viewHolder.voteUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.setAnswerVoteUp(item);
                }
            });
            viewHolder.voteDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.setAnswerVoteDown(item);
                }
            });

            if (item.getVoteUp()>0)
                viewHolder.rateUp.setText("".concat("+").concat(String.valueOf(item.getVoteUp())));
            if (item.getVoteDown()>0)
                viewHolder.rateDown.setText("".concat("-").concat(String.valueOf(item.getVoteDown())));
            setStars (viewHolder, item);
        }
    }

    @Override
    public int getItemCount() {
        // Including a header.
        return aQuestion.getAnswers().size() + 1;
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return TYPE_HEADER;
        } else if (position == 1 && position == answers.size()) {
            // The list may contains only one item.
            return TYPE_SINGLE;
        } else if (position == 1) {
            return TYPE_START;
        } else if (position == answers.size()) {
            return TYPE_FINISH;
        }
        return TYPE_NORMAL;
    }

    /**
     * DO NOT cast a RealmList to List or you will got some unexpected bugs.
     * @param list The RealmList.
     */
    public void updateData(@NonNull RealmList<Answer> list) {
        this.answers.clear();
        // See {@link QuestionDetailsAdapter#QuestionDetailsAdapter}
        this.answers.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * The package status view holder of recyclerView.
     */
    public class AnswersViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewDate;
        private TextView textViewText;
        private ImageView imageViewAuthor;
        private Timeline timeLine;
        GridView answerPhotoGridView;
        ImageView voteUp, voteDown;
        ArrayList <ImageView> stars;

        private TextView rateUp, rateDown;

        AnswersViewHolder(View itemView) {
            super(itemView);
            //textViewAuthor = (AppCompatTextView) itemView.findViewById(R.id.answerAuthor);
            textViewText = (AppCompatTextView) itemView.findViewById(R.id.answerText);
            textViewDate = (AppCompatTextView) itemView.findViewById(R.id.answerDate);
            answerPhotoGridView = itemView.findViewById(R.id.answerPhotoGrid);
            //timeLine = (Timeline) itemView.findViewById(R.id.tim);
            imageViewAuthor = itemView.findViewById(R.id.imageViewAvatar);

            voteUp = itemView.findViewById(R.id.answerRatePlusThumb);
            voteDown = itemView.findViewById(R.id.answerRateMinusThumb);

            stars = new ArrayList<>(5);
            stars.add((ImageView) itemView.findViewById(R.id.answerRate1));
            stars.add((ImageView) itemView.findViewById(R.id.answerRate2));
            stars.add((ImageView) itemView.findViewById(R.id.answerRate3));
            stars.add((ImageView) itemView.findViewById(R.id.answerRate4));
            stars.add((ImageView) itemView.findViewById(R.id.answerRate5));

            rateUp = itemView.findViewById(R.id.answerRatePlus);
            rateDown = itemView.findViewById(R.id.answerRateMinus);
        }
    }

    /**
     * A header view holder of recyclerView.
     */
    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView textViewAuthor;
        TextView textViewText;
        TextView textViewTitle;
        TextView textViewDate;
        GridView photoGridView;

        HeaderViewHolder(View itemView) {
            super(itemView);
            textViewAuthor = itemView.findViewById(R.id.questionAuthor);
            textViewText = itemView.findViewById(R.id.questionText);
            textViewTitle = itemView.findViewById(R.id.questionTitle);
            textViewDate = itemView.findViewById(R.id.questionDate);
            photoGridView = itemView.findViewById(R.id.gridview);
        }
    }

    private void setStars (AnswersViewHolder viewHolder, Answer answer) {
        double rating = answer.getRating();
        if (rating>=0.5) viewHolder.stars.get(0).setImageResource(R.drawable.baseline_star_half_black_24dp);
        if (rating>=1) viewHolder.stars.get(0).setImageResource(R.drawable.baseline_star_black_24dp);
        if (rating>=1.5) viewHolder.stars.get(1).setImageResource(R.drawable.baseline_star_half_black_24dp);
        if (rating>=2) viewHolder.stars.get(1).setImageResource(R.drawable.baseline_star_black_24dp);
        if (rating>=2.5) viewHolder.stars.get(2).setImageResource(R.drawable.baseline_star_half_black_24dp);
        if (rating>=3) viewHolder.stars.get(2).setImageResource(R.drawable.baseline_star_black_24dp);
        if (rating>=3.5) viewHolder.stars.get(3).setImageResource(R.drawable.baseline_star_half_black_24dp);
        if (rating>=4) viewHolder.stars.get(3).setImageResource(R.drawable.baseline_star_black_24dp);
        if (rating>=4.5) viewHolder.stars.get(4).setImageResource(R.drawable.baseline_star_half_black_24dp);
        if (rating>=4.8) viewHolder.stars.get(4).setImageResource(R.drawable.baseline_star_black_24dp);
    }
}
