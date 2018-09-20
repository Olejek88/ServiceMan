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
import ru.shtrm.serviceman.data.GpsTrack;
import ru.shtrm.serviceman.data.Journal;
import ru.shtrm.serviceman.retrofit.SManApiFactory;

public class SendDataService extends Service {
    public static final String ACTION = "ru.shtrm.serviceman.service.SEND_DATA";
    public static final String GPS_IDS = "gpsIds";
    public static final String LOG_IDS = "logIds";
    private static final String TAG = GetReferenceService.class.getSimpleName();
    private boolean isRuning;

    private long gpsIds[];
    private long logIds[];

    /**
     * Метод для выполнения отправки данных на сервер.
     */
    private Runnable task = new Runnable() {
        @Override
        public void run() {
            Realm realm = Realm.getDefaultInstance();
            Call<ResponseBody> call;

            // отправка координат
            if (gpsIds != null && gpsIds.length > 0) {
                int count = gpsIds.length;
                Long[] data = new Long[count];
                for (int i = 0; i < count; i++) {
                    data[i] = gpsIds[i];
                }

                RealmResults<GpsTrack> items = realm.where(GpsTrack.class)
                        .in("_id", data)
                        .findAll();
                // отправляем данные с координатами
                call = SManApiFactory.getGpsTrackService().sendData(realm.copyFromRealm(items));
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

            // отправка журнала
            if (logIds != null && logIds.length > 0) {
                int count = logIds.length;
                Long[] data = new Long[count];
                for (int i = 0; i < count; i++) {
                    data[i] = logIds[i];
                }

                RealmResults<Journal> items = realm.where(Journal.class).in("_id", data)
                        .findAll();
                // отправляем данные с логами
                call = SManApiFactory.getJournalService().sendData(realm.copyFromRealm(items));
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

            stopSelf();
        }
    };

    @Override
    public void onCreate() {
        isRuning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return START_STICKY;
        }

        if (!isRuning) {
            Log.d(TAG, "Запускаем поток отправки данных на сервер...");
            isRuning = true;
            gpsIds = intent.getLongArrayExtra(GPS_IDS);
            logIds = intent.getLongArrayExtra(LOG_IDS);
            new Thread(task).start();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRuning = false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
