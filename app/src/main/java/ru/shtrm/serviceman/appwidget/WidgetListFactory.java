package ru.shtrm.serviceman.appwidget;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.Sort;
import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.Question;

import static ru.shtrm.serviceman.realm.RealmHelper.DATABASE_NAME;

public class WidgetListFactory implements RemoteViewsService.RemoteViewsFactory {

    private final Context context;

    private String statusError;
    //private String[] answers;

    public WidgetListFactory(Context context) {
        this.context = context;
        statusError = context.getString(R.string.get_answers_error);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return Realm.getInstance(new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .name(DATABASE_NAME)
                .build())
                .where(Question.class)
                .equalTo("closed", false)
                .findAllSorted("date", Sort.DESCENDING)
                .size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(
                context.getPackageName(), R.layout.item_question_for_widget);

        Realm rlm = Realm.getInstance(new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .name(DATABASE_NAME)
                .build());
        List<Question> results = rlm.copyFromRealm(rlm.where(Question.class)
                .equalTo("closed", false)
                .findAllSorted("date", Sort.DESCENDING));

        Question question = results.get(position);
        // TODO реализовать обновление ответов
        /*
        if (question.getAnswers() != null && question.getAnswers().size() > 0) {
            remoteViews.setTextViewText(R.id.textViewStatus,
                    String.valueOf(question.getAnswers().get());
            remoteViews.setTextViewText(R.id.textViewTime, p.getData().get(0).getTime());
        } else {
            remoteViews.setTextViewText(R.id.textViewTime, "");
            remoteViews.setTextViewText(R.id.textViewStatus, statusError);
        }

        remoteViews.setTextViewText(R.id.textViewPackageName, p.getName());
        remoteViews.setTextViewText(R.id.textViewAvatar, p.getName().substring(0, 1));
        remoteViews.setImageViewResource(R.id.imageViewAvatar, p.getColorAvatar());

        Intent intent = new Intent();
        intent.putExtra(QuestionDetailsActivity.PACKAGE_ID, p.getNumber());
        remoteViews.setOnClickFillInIntent(R.id.layoutPackageItemMain, intent);
*/
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
