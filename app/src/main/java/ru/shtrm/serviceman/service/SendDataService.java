package ru.shtrm.serviceman.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import ru.shtrm.serviceman.data.Alarm;
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.GpsTrack;
import ru.shtrm.serviceman.data.Journal;
import ru.shtrm.serviceman.data.Measure;
import ru.shtrm.serviceman.retrofit.SManApiFactory;

public class SendDataService extends Service {
    public static final String ACTION = "ru.shtrm.serviceman.service.SEND_DATA";
    public static final String GPS_IDS = "gpsIds";
    public static final String LOG_IDS = "logIds";
    public static final String ALARM_IDS = "alramIds";
    public static final String MEASURE_IDS = "measureIds";
    public static final String EQUIPMENT_IDS = "equipmentIds";
    private static final String TAG = GetReferenceService.class.getSimpleName();
    private boolean isRunning;

    private long gpsIds[];
    private long logIds[];
    private long alarmIds[];
    private long measureIds[];
    private long equipmentIds[];

    /**
     * Метод для выполнения отправки данных на сервер.
     */
    private Runnable task = new Runnable() {
        @Override
        public void run() {
            Realm realm = Realm.getDefaultInstance();

            // отправка координат
            if (gpsIds != null && gpsIds.length > 0) {
                sendGpsTrack(realm, gpsIds);
            }

            // отправка журнала
            if (logIds != null && logIds.length > 0) {
                sendLog(realm, logIds);
            }

            // отправка аварий
            if (alarmIds != null && alarmIds.length > 0) {
                sendAlarm(realm, alarmIds);
            }

            // отправка оборудования
            if (equipmentIds != null && equipmentIds.length > 0) {
                sendEquipment(realm, equipmentIds);
            }

            // отправка измерений
            if (measureIds != null && measureIds.length > 0) {
                sendMeasure(realm, measureIds);
            }

            realm.close();

            // останавливаем сервис
            stopSelf();
        }

        void sendGpsTrack(Realm realm, long[] array) {
            int count = array.length;
            Long[] data = new Long[count];
            for (int i = 0; i < count; i++) {
                data[i] = array[i];
            }

            RealmResults<GpsTrack> items = realm.where(GpsTrack.class)
                    .in("_id", data)
                    .findAll();
            // отправляем данные с координатами
            Call<ResponseBody> call = SManApiFactory.getGpsTrackService().sendData(realm.copyFromRealm(items));
            try {
                Response response = call.execute();
                ResponseBody result = (ResponseBody) response.body();
                if (response.isSuccessful()) {
                    JSONObject jObj = new JSONObject(result.string());
                    // при сохранении данных на сервере произошли ошибки
                    // данный флаг пока не используем
//                        boolean success = (boolean) jObj.get("success");
                    JSONArray jData = (JSONArray) jObj.get("data");
                    Long[] ids = new Long[jData.length()];
                    for (int idx = 0; idx < jData.length(); idx++) {
                        JSONObject item = (JSONObject) jData.get(idx);
                        Long _id = Long.parseLong(item.get("_id").toString());
                        ids[idx] = _id;
                    }

                    // так как на клиенте не используем эту информацию, удаляем
                    // после успешной отправки и сохранения
                    realm.beginTransaction();
                    realm.where(GpsTrack.class).in("_id", ids)
                            .findAll()
                            .deleteAllFromRealm();
                    realm.commitTransaction();
                }
            } catch (Exception e) {
                Log.e(TAG, "Ошибка при отправке GPS лога.");
                e.printStackTrace();
            }
        }

        void sendLog(Realm realm, long[] array) {
            int count = array.length;
            Long[] data = new Long[count];
            for (int i = 0; i < count; i++) {
                data[i] = array[i];
            }

            RealmResults<Journal> items = realm.where(Journal.class).in("_id", data)
                    .findAll();
            // отправляем данные с логами
            Call<ResponseBody> call = SManApiFactory.getJournalService().sendData(realm.copyFromRealm(items));
            try {
                Response response = call.execute();
                ResponseBody result = (ResponseBody) response.body();
                if (response.isSuccessful()) {
                    JSONObject jObj = new JSONObject(result.string());
                    // при сохранении данных на сервере произошли ошибки
                    // данный флаг пока не используем
//                        boolean success = (boolean) jObj.get("success");
                    JSONArray jData = (JSONArray) jObj.get("data");
                    Long[] ids = new Long[jData.length()];
                    for (int idx = 0; idx < jData.length(); idx++) {
                        JSONObject item = (JSONObject) jData.get(idx);
                        Long _id = Long.parseLong(item.get("_id").toString());
                        ids[idx] = _id;
                    }

                    // так как на клиенте не используем эту информацию, удаляем
                    // после успешной отправки и сохранения
                    realm.beginTransaction();
                    realm.where(Journal.class).in("_id", ids)
                            .findAll()
                            .deleteAllFromRealm();
                    realm.commitTransaction();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Ошибка при отправке журнала.");
            }
        }

        void sendAlarm(Realm realm, long[] array) {
            int count = array.length;
            Long[] data = new Long[count];
            for (int i = 0; i < count; i++) {
                data[i] = array[i];
            }

            RealmResults<Alarm> items = realm.where(Alarm.class).in("_id", data)
                    .findAll();
            // отправляем данные с логами
            Call<ResponseBody> call = SManApiFactory.getAlarmService().sendData(realm.copyFromRealm(items));
            try {
                Response response = call.execute();
                ResponseBody result = (ResponseBody) response.body();
                if (response.isSuccessful()) {
                    JSONObject jObj = new JSONObject(result.string());
                    // при сохранении данных на сервере произошли ошибки
                    // данный флаг пока не используем
//                        boolean success = (boolean) jObj.get("success");
                    JSONArray jData = (JSONArray) jObj.get("data");
                    // устанавливаем флаг отправки записям которые подтвердил сервер
                    realm.beginTransaction();
                    for (int idx = 0; idx < jData.length(); idx++) {
                        JSONObject item = (JSONObject) jData.get(idx);
                        Long _id = Long.parseLong(item.get("_id").toString());
                        String uuid = item.get("uuid").toString();
                        Alarm sentItem = realm.where(Alarm.class).equalTo("uuid", uuid).findFirst();
                        // устанавливаем id присвоенное сервером
                        sentItem.set_id(_id);
                        sentItem.setSent(true);
                    }
                    realm.commitTransaction();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Ошибка при отправке журнала.");
            }
        }

        void sendMeasure(Realm realm, long[] array) {
            int count = array.length;
            Long[] data = new Long[count];
            for (int i = 0; i < count; i++) {
                data[i] = array[i];
            }

            RealmResults<Measure> items = realm.where(Measure.class).in("_id", data)
                    .equalTo("sent", false)
                    .findAll();
            // отправляем данные с измерениями
            Call<ResponseBody> call = SManApiFactory.getMeasureService().sendData(realm.copyFromRealm(items));
            try {
                Response response = call.execute();
                ResponseBody result = (ResponseBody) response.body();
                if (response.isSuccessful()) {
                    JSONObject jObj = new JSONObject(result.string());
                    // при сохранении данных на сервере произошли ошибки
                    // данный флаг пока не используем
//                        boolean success = (boolean) jObj.get("success");
                    JSONArray jData = (JSONArray) jObj.get("data");
                    // устанавливаем флаг отправки записям которые подтвердил сервер
                    realm.beginTransaction();
                    for (int idx = 0; idx < jData.length(); idx++) {
                        JSONObject item = (JSONObject) jData.get(idx);
                        Long _id = Long.parseLong(item.get("_id").toString());
                        String uuid = item.get("uuid").toString();
                        Measure sentItem = realm.where(Measure.class).equalTo("uuid", uuid).findFirst();
                        // устанавливаем id присвоенное сервером
                        sentItem.set_id(_id);
                        sentItem.setSent(true);
                    }

                    realm.commitTransaction();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Ошибка при отправке журнала.");
            }
        }

        void sendEquipment(Realm realm, long[] array) {
            int count = array.length;
            Long[] data = new Long[count];
            for (int i = 0; i < count; i++) {
                data[i] = array[i];
            }

            RealmResults<Equipment> items = realm.where(Equipment.class).in("_id", data)
                    .equalTo("sent", false)
                    .findAll();
            // отправляем данные с оборудованием
            Call<ResponseBody> call = SManApiFactory.getEquipmentService().sendData(realm.copyFromRealm(items));
            try {
                Response response = call.execute();
                ResponseBody result = (ResponseBody) response.body();
                if (response.isSuccessful()) {
                    JSONObject jObj = new JSONObject(result.string());
                    // при сохранении данных на сервере произошли ошибки
                    // данный флаг пока не используем
//                        boolean success = (boolean) jObj.get("success");
                    JSONArray jData = (JSONArray) jObj.get("data");
                    // устанавливаем флаг отправки записям которые подтвердил сервер
                    realm.beginTransaction();
                    for (int idx = 0; idx < jData.length(); idx++) {
                        JSONObject item = (JSONObject) jData.get(idx);
                        Long _id = Long.parseLong(item.get("_id").toString());
                        String uuid = item.get("uuid").toString();
                        Equipment sentItem = realm.where(Equipment.class).equalTo("uuid", uuid).findFirst();
                        // устанавливаем id присвоенное сервером
                        sentItem.set_id(_id);
                        sentItem.setSent(true);
                    }

                    realm.commitTransaction();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Ошибка при отправке журнала.");
            }
        }
    };

    @Override
    public void onCreate() {
        isRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return START_STICKY;
        }

        if (!isRunning) {
            Log.d(TAG, "Запускаем поток отправки данных на сервер...");
            isRunning = true;
            gpsIds = intent.getLongArrayExtra(GPS_IDS);
            logIds = intent.getLongArrayExtra(LOG_IDS);
            alarmIds = intent.getLongArrayExtra(ALARM_IDS);
            measureIds = intent.getLongArrayExtra(MEASURE_IDS);
            equipmentIds = intent.getLongArrayExtra(EQUIPMENT_IDS);
            new Thread(task).start();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
