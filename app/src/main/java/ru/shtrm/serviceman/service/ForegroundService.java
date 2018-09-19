package ru.shtrm.serviceman.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.AuthorizedUser;

public class ForegroundService extends Service {
    private static final String TAG = ForegroundService.class.getSimpleName();
    private static final long START_INTERVAL = 60000;
    private Handler getReference;

//    private Handler sendLog;
//    private Handler sendResult;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate()");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "sman")
                .setSmallIcon(R.mipmap.ic_service)
                .setContentTitle("Сервисмен")
                .setContentText("Получение/отправка данных");
        Notification notification;
        notification = builder.build();
        startForeground(777, notification);

        // запуск получения справочников с сервера
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startGetReference();
            }
        }, 0);

        // запуск отправки логов и координат на сервер
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startSendLog();
            }
        }, 20000);

        // запуск отправки результатов работы на сервер
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startSendResult();
            }
        }, 40000);

    }

    /**
     *
     */
    private void startSendLog() {
/*
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                long[] ids;
                Intent serviceIntent;
                Bundle bundle = null;

                Log.d(TAG, "startSendLog()");

                if (!isValidUser()) {
                    Log.d(TAG, "Нет активного пользователя для отправки логов и координат на сервер.");
                    // взводим следующий запуск
                    sendLog.postDelayed(this, START_INTERVAL);
                    return;
                }

                // получаем данные для отправки
                Realm realm = Realm.getDefaultInstance();
                RealmResults<GpsTrack> gpsItems = realm.where(GpsTrack.class)
                        .equalTo("sent", false).findAll();
                if (gpsItems.size() > 0) {
                    ids = new long[gpsItems.size()];
                    for (int i = 0; i < gpsItems.size(); i++) {
                        ids[i] = gpsItems.get(i).get_id();
                    }

                    bundle = new Bundle();
                    bundle.putLongArray(SendGPSnLogService.GPS_IDS, ids);
                }

                RealmResults<Journal> logItems = realm.where(Journal.class)
                        .equalTo("sent", false).findAll();
                if (logItems.size() > 0) {
                    ids = new long[logItems.size()];
                    for (int i = 0; i < logItems.size(); i++) {
                        ids[i] = logItems.get(i).get_id();
                    }

                    if (bundle == null) {
                        bundle = new Bundle();
                        bundle.putLongArray(SendGPSnLogService.LOG_IDS, ids);
                    }
                }

                // стартуем сервис отправки данных на сервер
                Context context = getApplicationContext();
                if (bundle != null) {
                    serviceIntent = new Intent(context, SendGPSnLogService.class);
                    serviceIntent.setAction(SendGPSnLogService.ACTION);
                    serviceIntent.putExtras(bundle);
                    context.startService(serviceIntent);
                }

                realm.close();

                // взводим следующий запуск
                sendLog.postDelayed(this, START_INTERVAL);
            }
        };
        sendLog = new Handler();
        sendLog.postDelayed(runnable, START_INTERVAL);
*/
    }

    /**
     *
     */
    private void startSendResult() {
/*
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                long[] ids;
                Intent serviceIntent;

                Log.d(TAG, "startSendResult()");

                if (!isValidUser()) {
                    Log.d(TAG, "Нет активного пользователя для отправки нарядов на сервер.");
                    // взводим следующий запуск
                    sendResult.postDelayed(this, START_INTERVAL);
                    return;
                }

                // получаем данные для отправки
                AuthorizedUser user = AuthorizedUser.getInstance();
                Realm realm = Realm.getDefaultInstance();
                RealmResults<Orders> orders = realm.where(Orders.class)
                        .beginGroup()
                        .equalTo("user.uuid", user.getUuid())
                        .equalTo("sent", false)
                        .endGroup()
                        .beginGroup()
                        .equalTo("orderStatus.uuid", OrderStatus.Status.COMPLETE).or()
                        .equalTo("orderStatus.uuid", OrderStatus.Status.UN_COMPLETE).or()
//                        .equalTo("orderStatus.uuid", OrderStatus.Status.IN_WORK).or()
                        .equalTo("orderStatus.uuid", OrderStatus.Status.CANCELED)
                        .endGroup()
                        .findAll();
                if (orders.size() == 0) {
                    Log.d(TAG, "Нет нарядов для отправки.");
                    ids = null;
                } else {
                    ids = new long[orders.size()];
                    for (int i = 0; i < orders.size(); i++) {
                        ids[i] = orders.get(i).get_id();
                    }
                }

                // стартуем сервис отправки данных на сервер
                Context context = getApplicationContext();
                serviceIntent = new Intent(context, SendOrdersService.class);
                serviceIntent.setAction(SendOrdersService.ACTION);
                Bundle bundle = new Bundle();
                bundle.putLongArray(SendOrdersService.ORDERS_IDS, ids);
                serviceIntent.putExtras(bundle);
                context.startService(serviceIntent);
                realm.close();

                // взводим следующий запуск
                sendResult.postDelayed(this, START_INTERVAL);
            }
        };
        sendResult = new Handler();
        sendResult.postDelayed(runnable, START_INTERVAL);
*/
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
}
