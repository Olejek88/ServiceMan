package ru.shtrm.serviceman.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Response;
import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.Alarm;
import ru.shtrm.serviceman.data.AuthorizedUser;
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.Flat;
import ru.shtrm.serviceman.data.GpsTrack;
import ru.shtrm.serviceman.data.IBaseRecord;
import ru.shtrm.serviceman.data.Journal;
import ru.shtrm.serviceman.data.Measure;
import ru.shtrm.serviceman.data.Message;
import ru.shtrm.serviceman.data.PhotoAlarm;
import ru.shtrm.serviceman.data.PhotoEquipment;
import ru.shtrm.serviceman.data.PhotoFlat;
import ru.shtrm.serviceman.data.PhotoHouse;
import ru.shtrm.serviceman.data.PhotoMessage;
import ru.shtrm.serviceman.data.ReferenceUpdate;
import ru.shtrm.serviceman.data.Token;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.retrofit.SManApiFactory;
import ru.shtrm.serviceman.retrofit.ServiceApiFactory;

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
                RealmResults<Alarm> alarmItems = realm.where(Alarm.class)
                        .equalTo("sent", false)
                        .findAll().sort("_id");
                if (alarmItems.size() > 0) {
                    limitIds = getLimitElements(alarmItems);
                    alarmItems = realm.where(Alarm.class).in("_id", limitIds).findAll();
                    ids = getIds(alarmItems);
                    bundle.putLongArray(SendDataService.ALARM_IDS, ids);
                }

                // получаем данные для отправки измерений
                RealmResults<Measure> measureItems = realm.where(Measure.class)
                        .equalTo("sent", false)
                        .findAll().sort("_id");
                if (measureItems.size() > 0) {
                    limitIds = getLimitElements(measureItems);
                    measureItems = realm.where(Measure.class).in("_id", limitIds).findAll();
                    ids = getIds(measureItems);
                    bundle.putLongArray(SendDataService.MEASURE_IDS, ids);
                }

                // получаем данные для отправки оборудования
                RealmResults<Equipment> equItems = realm.where(Equipment.class)
                        .equalTo("sent", false)
                        .findAll().sort("_id");
                if (equItems.size() > 0) {
                    limitIds = getLimitElements(equItems);
                    equItems = realm.where(Equipment.class).in("_id", limitIds).findAll();
                    ids = getIds(equItems);
                    bundle.putLongArray(SendDataService.EQUIPMENT_IDS, ids);
                }

                // получаем данные для отправки фотографий аварий
                RealmResults<PhotoAlarm> photoAlarms = realm.where(PhotoAlarm.class)
                        .equalTo("sent", false)
                        .findAll().sort("_id");
                if (photoAlarms.size() > 0) {
                    limitIds = getLimitElements(photoAlarms);
                    photoAlarms = realm.where(PhotoAlarm.class).in("_id", limitIds).findAll();
                    ids = getIds(photoAlarms);
                    bundle.putLongArray(SendDataService.PHOTO_ALARM_IDS, ids);
                }

                // получаем данные для отправки фототографий домов
                RealmResults<PhotoHouse> photoHouses = realm.where(PhotoHouse.class)
                        .equalTo("sent", false)
                        .findAll().sort("_id");
                if (photoHouses.size() > 0) {
                    limitIds = getLimitElements(photoHouses);
                    photoHouses = realm.where(PhotoHouse.class).in("_id", limitIds).findAll();
                    ids = getIds(photoHouses);
                    bundle.putLongArray(SendDataService.PHOTO_HOUSE_IDS, ids);
                }

                // получаем данные для отправки фотографий квартир
                RealmResults<PhotoFlat> photoFlats = realm.where(PhotoFlat.class)
                        .equalTo("sent", false)
                        .findAll().sort("_id");
                if (photoFlats.size() > 0) {
                    limitIds = getLimitElements(photoFlats);
                    photoFlats = realm.where(PhotoFlat.class).in("_id", limitIds).findAll();
                    ids = getIds(photoFlats);
                    bundle.putLongArray(SendDataService.PHOTO_FLAT_IDS, ids);
                }

                // получаем данные для отправки фотографий оборудования
                RealmResults<PhotoEquipment> photoEquipments = realm.where(PhotoEquipment.class)
                        .equalTo("sent", false)
                        .findAll().sort("_id");
                if (photoEquipments.size() > 0) {
                    limitIds = getLimitElements(photoEquipments);
                    photoEquipments = realm.where(PhotoEquipment.class).in("_id", limitIds).findAll();
                    ids = getIds(photoEquipments);
                    bundle.putLongArray(SendDataService.PHOTO_EQUIPMENT_IDS, ids);
                }

                // получаем данные для отправки квартиры (их нужно отправлять если у них сменился статус)
                RealmResults<Flat> flats = realm.where(Flat.class)
                        .equalTo("sent", false)
                        .findAll().sort("_id");

                if (flats.size() > 0) {
                    limitIds = getLimitElements(flats);
                    flats = realm.where(Flat.class).in("_id", limitIds).findAll();
                    ids = getIds(flats);
                    bundle.putLongArray(SendDataService.FLAT_IDS, ids);
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

                // получаем данные для отправки фотографий сообщений
                RealmResults<PhotoMessage> photoMessages = realm.where(PhotoMessage.class)
                        .equalTo("sent", false)
                        .findAll().sort("_id");

                if (photoMessages.size() > 0) {
                    limitIds = getLimitElements(photoMessages);
                    photoMessages = realm.where(PhotoMessage.class).in("_id", limitIds).findAll();
                    ids = getIds(photoMessages);
                    bundle.putLongArray(SendDataService.PHOTO_MESSAGE_IDS, ids);
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
                Runnable runnable = new Runnable() {
                    String getToken(String uuid, String hash) {
                        String token = null;
                        Call<Token> call = SManApiFactory.getTokenService()
                                .getToken(uuid, hash);
                        try {
                            Response<Token> response = call.execute();
                            if (response.isSuccessful()) {
                                token = response.body().getToken();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return token;
                    }

                    void getUsers() {
                        if (!AuthorizedUser.getInstance().isValidToken()) {
                            String lastUpdateDate = ReferenceUpdate.lastChangedAsStr(User.class.getSimpleName());
                            Date updateDate = new Date();
                            Call<List<User>> res = ServiceApiFactory.getUsersService().getData(lastUpdateDate);
                            try {
                                Response<List<User>> response = res.execute();
                                if (response.isSuccessful()) {
                                    List<User> users = response.body();
                                    Realm realm = Realm.getDefaultInstance();
                                    ReferenceUpdate.saveReferenceData(User.class.getSimpleName(), updateDate);
                                    realm.beginTransaction();
                                    realm.copyToRealmOrUpdate(users);
                                    realm.commitTransaction();
                                    realm.close();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void run() {
                        SharedPreferences sp = getSharedPreferences(User.SERVICE_USER_UUID, Context.MODE_PRIVATE);
                        String token = sp.getString("token", null);
                        SharedPreferences spd = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        String name;
                        name = getString(R.string.api_oid_key);
                        String oid = spd.getString(name, null);
                        name = getString(R.string.api_organization_secret_key);
                        String secret = spd.getString(name, null);
                        ServiceApiFactory.setOid(oid);
                        ServiceApiFactory.setSecret(secret);

                        String pinHash;
                        Realm realm = Realm.getDefaultInstance();
                        User sUser = realm.where(User.class)
                                .equalTo("uuid", User.SERVICE_USER_UUID).findFirst();
                        if (sUser != null) {
                            pinHash = sUser.getPin();
                            realm.close();
                        } else {
                            realm.close();
                            return;
                        }

                        if (token == null) {
                            token = getToken(User.SERVICE_USER_UUID, pinHash);
                            if (token != null) {
                                sp.edit().putString("token", token).commit();
                            }
                        }

                        if (token != null) {
                            boolean isPingOk = false;
                            ServiceApiFactory.setToken(token);
                            Call<Void> call = ServiceApiFactory.getPingService().ping();
                            try {
                                isPingOk = call.execute().isSuccessful();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if (isPingOk) {
                                getUsers();
                            } else {
                                token = getToken(User.SERVICE_USER_UUID, pinHash);
                                if (token != null) {
                                    sp.edit().putString("token", token).commit();
                                    ServiceApiFactory.setToken(token);
                                    getUsers();
                                }
                            }
                        }
                    }
                };
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
