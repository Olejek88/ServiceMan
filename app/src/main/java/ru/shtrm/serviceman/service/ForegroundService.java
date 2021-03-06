package ru.shtrm.serviceman.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import io.realm.Realm;
import io.realm.RealmResults;
import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.Alarm;
import ru.shtrm.serviceman.data.AuthorizedUser;
import ru.shtrm.serviceman.data.IBaseRecord;
import ru.shtrm.serviceman.data.Message;

public class ForegroundService extends Service {
    private static final String TAG = ForegroundService.class.getSimpleName();
    private static final long START_INTERVAL = 60000;
    private static final int LIMIT_SIZE = 100;
    private Handler getReference;
    private Handler sendData;
    private Handler serviceUserToken;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate()");

        String channelId;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId = createNotificationChannel();
        } else {
            channelId = "sman";
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_service)
                .setContentTitle("Сервисмен")
                .setContentText("Получение/отправка данных");
        Notification notification;
        notification = builder.build();
        startForeground(777, notification);

        // запуск получения токена для сервисного пользователя
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startGetServiceToken();
            }
        }, 0);

        // запуск получения справочников с сервера
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startGetReference();
            }
        }, 20000);

        // запуск отправки данных на сервер
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startSendData();
            }
        }, 40000);
    }

    @TargetApi(Build.VERSION_CODES.O)
    private String createNotificationChannel() {
        String channelId = "sman";
        String channelName = "My Background Service";
        NotificationChannel channel = new NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_NONE);
        channel.setLightColor(Color.BLUE);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager service = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (service != null) {
            service.createNotificationChannel(channel);
        }

        return channelId;
    }

    /**
     *
     */
    private void startSendData() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                long[] ids;
                Intent serviceIntent;
                Bundle bundle = new Bundle();

                Log.d(TAG, "startSendData()");

                if (!isValidUser()) {
                    Log.d(TAG, "Нет активного пользователя для отправки данных на сервер.");
                    // взводим следующий запуск
                    sendData.postDelayed(this, START_INTERVAL);
                    return;
                }

                Realm realm = Realm.getDefaultInstance();
                Long[] limitIds;

                // получаем данные для отправки аварий
                RealmResults<Alarm> alarmItems = realm.where(Alarm.class)
                        .equalTo("sent", false)
                        .findAll().sort("_id");
                if (alarmItems.size() > 0) {
                    limitIds = getLimitElements(alarmItems);
                    alarmItems = realm.where(Alarm.class).in("_id", limitIds).findAll();
                    ids = getIds(alarmItems);
                    bundle.putLongArray(SendDataService.ALARM_IDS, ids);
                }

                // получаем данные для отправки сообщений
                RealmResults<Message> messages = realm.where(Message.class)
                        .equalTo("sent", false)
                        .findAll().sort("_id");

                if (messages.size() > 0) {
                    limitIds = getLimitElements(messages);
                    messages = realm.where(Message.class).in("_id", limitIds).findAll();
                    ids = getIds(messages);
                    bundle.putLongArray(SendDataService.MESSAGE_IDS, ids);
                }

                realm.close();

                // стартуем сервис отправки данных на сервер
                Context context = getApplicationContext();
                serviceIntent = new Intent(context, SendDataService.class);
                serviceIntent.setAction(SendDataService.ACTION);
                serviceIntent.putExtras(bundle);
                context.startService(serviceIntent);


                // взводим следующий запуск
                sendData.postDelayed(this, START_INTERVAL);
            }
        };
        sendData = new Handler();
        sendData.postDelayed(runnable, START_INTERVAL);
    }

    /**
     *
     */
    private void startGetReference() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "startGetReference()");

                if (isValidUser()) {
                    // стартуем сервис получения справочников
                    Context context = getApplicationContext();
                    Intent serviceIntent = new Intent(context, GetReferenceService.class);
                    serviceIntent.setAction(GetReferenceService.ACTION);
                    context.startService(serviceIntent);
                } else {
                    Log.d(TAG, "Нет активного пользователя для получения справочников.");
                }

                // взводим следующий запуск
                getReference.postDelayed(this, START_INTERVAL);
            }
        };
        getReference = new Handler();
        getReference.postDelayed(runnable, START_INTERVAL);
    }

    /**
     *
     */
    private void startGetServiceToken() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "startGetServiceToken()");
                Runnable runnable = new GetServiceToken(getApplicationContext());
                new Thread(runnable).start();

                // взводим следующий запуск
                serviceUserToken.postDelayed(this, START_INTERVAL);
            }
        };
        Log.d(TAG, "Взвели получение токена и пользоваетлей для сервисного пользователя.");
        serviceUserToken = new Handler();
        serviceUserToken.postDelayed(runnable, 0);
    }

    private boolean isValidUser() {
        AuthorizedUser user = AuthorizedUser.getInstance();
        Log.e(TAG, "isValidToken = " + user.isValidToken());
        return user.isValidToken();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Long[] getLimitElements(RealmResults<?> results) {
        int limitElements = results.size() >= LIMIT_SIZE ? LIMIT_SIZE : results.size();
        Long[] limitIds = new Long[limitElements];
        for (int idx = 0; idx < limitIds.length; idx++) {
            limitIds[idx] = ((IBaseRecord) (results.get(idx))).get_id();
        }

        return limitIds;
    }

    private long[] getIds(RealmResults<?> results) {
        long[] ids = new long[results.size()];
        for (int idx = 0; idx < ids.length; idx++) {
            ids[idx] = ((IBaseRecord) (results.get(idx))).get_id();
        }

        return ids;
    }
}
