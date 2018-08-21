package ru.shtrm.serviceman.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.appwidget.AppWidgetProvider;
import ru.shtrm.serviceman.data.Question;
import ru.shtrm.serviceman.mvp.questiondetails.QuestionDetailsActivity;
import ru.shtrm.serviceman.realm.RealmHelper;
import ru.shtrm.serviceman.retrofit.RetrofitClient;
import ru.shtrm.serviceman.retrofit.RetrofitService;
import ru.shtrm.serviceman.util.NetworkUtil;
import ru.shtrm.serviceman.util.PushUtil;
import ru.shtrm.serviceman.util.SettingsUtil;

public class ReminderService extends IntentService {

    private SharedPreferences preference;

    private CompositeDisposable compositeDisposable;

    public static final String TAG = ReminderService.class.getSimpleName();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public ReminderService() {
        super(TAG);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        preference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        compositeDisposable = new CompositeDisposable();
        Log.d(TAG, "onCreate: ");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Log.d(TAG, "onHandleIntent: ");

        boolean isDisturbMode = preference.getBoolean(SettingsUtil.KEY_DO_NOT_DISTURB_MODE, true);

        // The alert mode is off
        // or DO-NOT-DISTURB-MODE is off
        // or time now is not in the DO-NOT-DISTURB-MODE range.
        if (isDisturbMode && PushUtil.isInDisturbTime(this, Calendar.getInstance())) {
            return;
        }

        Realm rlm = RealmHelper.newRealmInstance();

        List<Question> results = rlm.copyFromRealm(
                rlm.where(Question.class)
                        .equalTo("closed", false)
                        .findAll());

        for (int i = 0; i < results.size(); i++){
            Question question = results.get(i);
            // Avoid repeated pushing
            if (question.isPushable()) {
                Realm realm = RealmHelper.newRealmInstance();
                question.setPushable(false);
                pushNotification(i + 1001, setNotifications(i, question));

                realm.beginTransaction();
                realm.copyToRealmOrUpdate(question);
                realm.commitTransaction();
                realm.close();

            } else if (NetworkUtil.networkConnected(getApplicationContext())) {
                refreshQuestion(i, results.get(i));
            }

        }

        rlm.close();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Remove
        //compositeDisposable.clear();
        Log.d(TAG, "onDestroy: ");
    }

    private static Notification buildNotification(Context context, String title, String subject,
                                                  String longText, String time, int icon, int color,
                                                  PendingIntent contentIntent, PendingIntent deleteIntent) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(title);
        builder.setContentText(subject);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setStyle(new NotificationCompat.BigTextStyle(builder).bigText(longText));
        builder.setSmallIcon(icon);
        builder.setShowWhen(true);
        builder.setWhen(System.currentTimeMillis());
        builder.setContentIntent(contentIntent);
        builder.setSubText(time);
        builder.setAutoCancel(true);
        builder.setColor(color);

        return builder.build();

    }

    /**
     * Set the details like title, subject, etc. of notifications.
     * @param position Position.
     * @param question The question.
     * @return The notification.
     */
    private Notification setNotifications(int position, Question question) {
        if (question != null) {

            Intent i = new Intent(getApplicationContext(), QuestionDetailsActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            i.putExtra(QuestionDetailsActivity.QUESTION_ID, question.getId());

            PendingIntent intent = PendingIntent.getActivity(getApplicationContext(),
                    position, i, PendingIntent.FLAG_UPDATE_CURRENT);

            String title = question.getTitle();
            String subject = getString(R.string.notification_new_answer);
            int smallIcon = R.drawable.ic_local_shipping_teal_24dp;

            Notification notification;
            if (question.getAnswers().size()>0) {
                notification = buildNotification(getApplicationContext(),
                        title,
                        subject,
                        question.getAnswers().get(0).getText(),
                        question.getAnswers().get(0).getDate().toString(),
                        smallIcon,
                        ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary),
                        intent,
                        null);
            }
            else {
                notification = buildNotification(getApplicationContext(),
                        title,
                        subject,
                        "",
                        question.getDate().toString(),
                        smallIcon,
                        ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary),
                        intent,
                        null);
            }

            notification.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            notification.tickerText = title;

            return notification;
        }
        return null;
    }

    /**
     * Update the queston by accessing network,
     * then build and send the notifications.
     * @param position Position.
     * @param question The question.
     */
    private void refreshQuestion(final int position, final Question question) {

        Log.d(TAG, "refreshQuestion: ");

        RetrofitClient.getInstance()
                .create(RetrofitService.class)
                .getQuestion(question.getId())
                .subscribeOn(Schedulers.io())
                .doOnNext(new Consumer<Question>() {
                    @Override
                    public void accept(Question aQuestion) throws Exception {
                        if (aQuestion != null &&
                                aQuestion.getAnswers().size() > question.getAnswers().size()) {

                            Realm rlm = RealmHelper.newRealmInstance();
                            question.setPushable(false);
                            question.setAnswers(aQuestion.getAnswers());

                            rlm.beginTransaction();
                            rlm.copyToRealmOrUpdate(question);
                            rlm.commitTransaction();
                            rlm.close();

                            // Send notification
                            pushNotification(position + 1000, setNotifications(position, question));

                            // Update the widget
                            sendBroadcast(AppWidgetProvider.getRefreshBroadcastIntent(getApplicationContext()));
                        }
                    }
                })
                .subscribe();
    }

    private void pushNotification(int i, Notification n) {
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm != null) {
            nm.notify(i, n);
        }
    }

}
