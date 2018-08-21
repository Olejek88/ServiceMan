package ru.shtrm.serviceman.mvp.search;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.Question;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.interfaces.OnRecyclerViewItemClickListener;
import ru.shtrm.serviceman.util.MainUtil;

public class SearchResultsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull
    private final Context context;

    @NonNull
    private LayoutInflater inflater;

    private List<Question> questions;
    private List<User> users;
    private List<ItemWrapper> list;

    private OnRecyclerViewItemClickListener listener;

    SearchResultsAdapter(@NonNull Context context,
                         @NonNull List<Question> questions,
                         @NonNull List<User> users) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.questions = questions;
        this.users = users;
        this.list = new ArrayList<>();
        this.list = new ArrayList<>();
        this.list.add(new ItemWrapper(ItemWrapper.TYPE_CATEGORY));
        if (questions.size() > 0) {
            for (int i = 0; i < questions.size(); i++) {
                ItemWrapper wrapper = new ItemWrapper(ItemWrapper.TYPE_QUESTION);
                wrapper.index = i;
                list.add(wrapper);
            }
        } else {
            list.add(new ItemWrapper(ItemWrapper.TYPE_EMPTY));
        }

        this.list.add(new ItemWrapper(ItemWrapper.TYPE_CATEGORY));
        if (users.size() > 0) {
            for (int i = 0; i < users.size(); i++) {
                ItemWrapper wrapper = new ItemWrapper(ItemWrapper.TYPE_USER);
                wrapper.index = i;
                list.add(wrapper);
            }
        } else {
            list.add(new ItemWrapper(ItemWrapper.TYPE_EMPTY));
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case ItemWrapper.TYPE_EMPTY:
                viewHolder = new EmptyHolder(inflater.inflate(
                        R.layout.item_search_result_empty, parent, false));
                break;

            case ItemWrapper.TYPE_CATEGORY:
                viewHolder = new ResultCategoryHolder(inflater.inflate(
                        R.layout.item_search_result_category, parent, false));
                break;

            case ItemWrapper.TYPE_QUESTION:
                viewHolder = new QuestionHolder(inflater.inflate(
                        R.layout.item_question, parent, false), listener);
                break;

            case ItemWrapper.TYPE_USER:
                viewHolder = new UserHolder(inflater.inflate(
                        R.layout.item_user, parent, false), listener);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemWrapper iw = list.get(position);
        switch (iw.viewType) {
            case ItemWrapper.TYPE_EMPTY:
                EmptyHolder emptyHolder = (EmptyHolder) holder;
                emptyHolder.textView.setText(
                        (position == 1 ? questions == null : users == null) ?
                        R.string.item_loading :
                        R.string.no_result);
                break;

            case ItemWrapper.TYPE_QUESTION:
                QuestionHolder questionHolder = (QuestionHolder) holder;
                Question question = questions.get(iw.index);
                String sDate = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.US)
                        .format(question.getDate());
                questionHolder.textViewStatus.setText(question.getUser().getName().concat(" Ответов: нет"));
                questionHolder.textViewDate.setText(sDate);
                questionHolder.textViewTitle.setText(question.getTitle());
                if (question.getAnswers() != null && question.getAnswers().size() > 0) {
                    questionHolder.textViewStatus.setText(question.getUser().getName().
                            concat(" Ответов:").concat(Integer.toString(question.getAnswers().size())));
                }
                if (question.getUser().getAvatar()!=null)
                    questionHolder.avatar.setImageBitmap(MainUtil.getBitmapByPath(
                        MainUtil.getPicturesDirectory(context),question.getUser().getAvatar()));
                else
                    questionHolder.avatar.setImageResource(R.drawable.user_random_icon_2);
                break;

            case ItemWrapper.TYPE_USER:
                User user = users.get(iw.index);
                UserHolder userHolder = (UserHolder) holder;
                userHolder.textViewAvatar.setText(user.getName().substring(0, 1).toUpperCase());
                userHolder.textViewName.setText(user.getName());
                // TODO заменить на строчку статистики, формируемую через функцию из утилит
                userHolder.textViewRate.setText("Q&A");
                userHolder.avatar.setColorFilter(Color.BLUE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).viewType;
    }

    public void updateData(List<Question> questions, List<User> users) {
        this.questions.clear();
        this.users.clear();
        this.list.clear();
        this.list.add(new ItemWrapper(ItemWrapper.TYPE_CATEGORY));
        if (questions.size() > 0) {
            for (int i = 0; i < questions.size(); i++) {
                ItemWrapper wrapper = new ItemWrapper(ItemWrapper.TYPE_QUESTION);
                wrapper.index = i;
                list.add(wrapper);
                this.questions.add(questions.get(i));
            }
        } else {
            list.add(new ItemWrapper(ItemWrapper.TYPE_EMPTY));
        }

        this.list.add(new ItemWrapper(ItemWrapper.TYPE_CATEGORY));
        if (users.size() > 0) {
            for (int i = 0; i < users.size(); i++) {
                ItemWrapper wrapper = new ItemWrapper(ItemWrapper.TYPE_USER);
                wrapper.index = i;
                list.add(wrapper);
                this.users.add(users.get(i));
            }
        } else {
            list.add(new ItemWrapper(ItemWrapper.TYPE_EMPTY));
        }
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    public int getOriginalIndex(int position) {
        return list.get(position).index;
    }

    private class QuestionHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        CircleImageView avatar;
        AppCompatTextView textViewDate, textViewStatus;
        AppCompatTextView textViewTitle;

        private OnRecyclerViewItemClickListener listener;

        QuestionHolder(View itemView, OnRecyclerViewItemClickListener listener) {
            super(itemView);
            avatar = itemView.findViewById(R.id.circleImageView);
            textViewTitle = itemView.findViewById(R.id.textViewQuestionTitle);
            textViewDate = itemView.findViewById(R.id.textQuestionTime);
            textViewStatus = itemView.findViewById(R.id.textQuestionStatus);
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

    private class EmptyHolder extends RecyclerView.ViewHolder {

        AppCompatTextView textView;

        EmptyHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_title);
        }

    }

    private class ResultCategoryHolder extends RecyclerView.ViewHolder {

        AppCompatTextView textView;

        ResultCategoryHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_title);
        }

    }

    private class UserHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        CircleImageView avatar;
        TextView textViewName, textViewAvatar, textViewRate;

        private OnRecyclerViewItemClickListener listener;

        UserHolder(View itemView, OnRecyclerViewItemClickListener listener) {
            super(itemView);
            avatar = itemView.findViewById(R.id.imageViewAvatar);
            textViewName = (AppCompatTextView) itemView.findViewById(R.id.textViewUserName);
            textViewRate = (AppCompatTextView) itemView.findViewById(R.id.textViewUserStats);
            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.OnItemClick(v, getLayoutPosition());
            }
        }
    }

    public static class ItemWrapper {

        final static int TYPE_QUESTION = 0, TYPE_USER = 1, TYPE_ANSWER = 2,
                TYPE_CATEGORY = 3, TYPE_EMPTY = 4;

        int viewType;

        // Optional
        int index;

        ItemWrapper(int viewType) {
            this.viewType = viewType;
        }

    }

}
