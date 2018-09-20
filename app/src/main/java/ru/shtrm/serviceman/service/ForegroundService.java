package ru.shtrm.serviceman.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
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
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.GpsTrack;
import ru.shtrm.serviceman.data.IBaseRecord;
import ru.shtrm.serviceman.data.Journal;
import ru.shtrm.serviceman.data.Measure;

public class ForegroundService extends Service {
    private static final String TAG = ForegroundService.class.getSimpleName();
    private static final long START_INTERVAL = 60000;
    private Handler getReference;
    private Handler sendData;
    private static final int LIMIT_SIZE = 100;

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

        // запуск отправки данных на сервер
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startSendData();
            }
        }, 20000);
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

                // получаем данные для отправки координат
                RealmResults<GpsTrack> gpsItems = realm.where(GpsTrack.class).findAll().sort("_id");
                if (gpsItems.size() > 0) {
                    limitIds = getLimitElements(gpsItems);
                    gpsItems = realm.where(GpsTrack.class).in("_id", limitIds).findAll();
                    ids = getIds(gpsItems);
                    bundle.putLongArray(SendDataService.GPS_IDS, ids);
                }

                // получаем данные для отправки журнала
                RealmResults<Journal> logItems = realm.where(Journal.class).findAll().sort("_id");
                if (logItems.size() > 0) {
                    limitIds = getLimitElements(logItems);
                    logItems = realm.where(Journal.class).in("_id", limitIds).findAll();
                    ids = getIds(logItems);
                    bundle.putLongArray(SendDataService.LOG_IDS, ids);
                }

                // получаем данные для отправки аварий
                RealmResults<Alarm> alarmItems = realm.where(Alarm.class).findAll().sort("_id");
                if (alarmItems.size() > 0) {
                    limitIds = getLimitElements(alarmItems);
                    alarmItems = realm.where(Alarm.class).in("_id", limitIds).findAll();
                    ids = getIds(alarmItems);
                    bundle.putLongArray(SendDataService.ALARM_IDS, ids);
                }

                // получаем данные для отправки измерений
                RealmResults<Measure> measureItems = realm.where(Measure.class).findAll().sort("_id");
                if (measureItems.size() > 0) {
                    limitIds = getLimitElements(measureItems);
                    measureItems = realm.where(Measure.class).in("_id", limitIds).findAll();
                    ids = getIds(measureItems);
                    bundle.putLongArray(SendDataService.MEASURE_IDS, ids);
                }

                // получаем данные для отправки оборудования
                RealmResults<Equipment> equItems = realm.where(Equipment.class).findAll().sort("_id");
                if (equItems.size() > 0) {
                    limitIds = getLimitElements(equItems);
                    equItems = realm.where(Equipment.class).in("_id", limitIds).findAll();
                    ids = getIds(equItems);
                    bundle.putLongArray(SendDataService.EQUIPMENT_IDS, ids);
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
