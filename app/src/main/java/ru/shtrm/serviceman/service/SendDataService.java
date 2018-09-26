package ru.shtrm.serviceman.service;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.webkit.MimeTypeMap;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import ru.shtrm.serviceman.data.Alarm;
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.Flat;
import ru.shtrm.serviceman.data.GpsTrack;
import ru.shtrm.serviceman.data.Journal;
import ru.shtrm.serviceman.data.Measure;
import ru.shtrm.serviceman.data.Message;
import ru.shtrm.serviceman.data.PhotoAlarm;
import ru.shtrm.serviceman.data.PhotoEquipment;
import ru.shtrm.serviceman.data.PhotoFlat;
import ru.shtrm.serviceman.data.PhotoHouse;
import ru.shtrm.serviceman.data.PhotoMessage;
import ru.shtrm.serviceman.retrofit.SManApiFactory;

public class SendDataService extends Service {
    public static final String ACTION = "ru.shtrm.serviceman.service.SEND_DATA";
    public static final String GPS_IDS = "gpsIds";
    public static final String LOG_IDS = "logIds";
    public static final String ALARM_IDS = "alramIds";
    public static final String MEASURE_IDS = "measureIds";
    public static final String EQUIPMENT_IDS = "equipmentIds";
    public static final String PHOTO_ALARM_IDS = "photoAlarmIds";
    public static final String PHOTO_HOUSE_IDS = "photoHouseIds";
    public static final String PHOTO_FLAT_IDS = "photoFlatIds";
    public static final String PHOTO_EQUIPMENT_IDS = "photoEquipmentIds";
    public static final String FLAT_IDS = "flatIds";
    public static final String MESSAGE_IDS = "messageIds";
    public static final String PHOTO_MESSAGE_IDS = "photoMessageIds";
    private static final String TAG = SendDataService.class.getSimpleName();
    private boolean isRunning;

    private long gpsIds[];
    private long logIds[];
    private long alarmIds[];
    private long measureIds[];
    private long equipmentIds[];
    private long photoAlarmIds[];
    private long photoHouseIds[];
    private long photoFlatIds[];
    private long photoEquipmentIds[];
    private long flatIds[];
    private long messageIds[];
    private long photoMessageIds[];


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

            // отправка фотографий аварий
            if (photoAlarmIds != null && photoAlarmIds.length > 0) {
                sendPhotoAlarm(realm, photoAlarmIds);
            }

            // отправка фотографий домов
            if (photoHouseIds != null && photoHouseIds.length > 0) {
                sendPhotoHouse(realm, photoHouseIds);
            }

            // отправка фотографий квартир
            if (photoFlatIds != null && photoFlatIds.length > 0) {
                sendPhotoFlat(realm, photoFlatIds);
            }

            // отправка фотографий оборудования
            if (photoEquipmentIds != null && photoEquipmentIds.length > 0) {
                sendPhotoEquipment(realm, photoEquipmentIds);
            }

            // отправка квартир
            if (flatIds != null && flatIds.length > 0) {
                sendFlat(realm, flatIds);
            }

            // отправка сообщений
            if (messageIds != null && messageIds.length > 0) {
                sendMessage(realm, messageIds);
            }

            // отправка фотографий сообщений
            if (photoMessageIds != null && photoMessageIds.length > 0) {
                sendPhotoMessage(realm, photoMessageIds);
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
                Log.e(TAG, "Ошибка при отправке аварий.");
            }
        }

        void sendEquipment(Realm realm, long[] array) {
            int count = array.length;
            Long[] data = new Long[count];
            for (int i = 0; i < count; i++) {
                data[i] = array[i];
            }

            RealmResults<Equipment> items = realm.where(Equipment.class).in("_id", data)
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
                Log.e(TAG, "Ошибка при отправке оборудования.");
            }
        }

        void sendMeasure(Realm realm, long[] array) {
            int count = array.length;
            Long[] data = new Long[count];
            for (int i = 0; i < count; i++) {
                data[i] = array[i];
            }

            RealmResults<Measure> items = realm.where(Measure.class).in("_id", data)
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
                Log.e(TAG, "Ошибка при отправке измерений.");
            }
        }

        void sendPhotoAlarm(Realm realm, long[] array) {
            int count = array.length;
            Long[] data = new Long[count];
            for (int i = 0; i < count; i++) {
                data[i] = array[i];
            }

            RealmResults<PhotoAlarm> items = realm.where(PhotoAlarm.class).in("_id", data)
                    .findAll();
            // добавляем в список файлы
            RequestBody descr = RequestBody.create(MultipartBody.FORM, "Photos due execution operation.");

            for (PhotoAlarm file : items) {
                List<MultipartBody.Part> list = new ArrayList<>();
                try {
                    String formId = "file";
                    String fileUuid = file.getUuid();
                    String fileName = fileUuid + ".jpg";
                    File path = new File(getExternalFilesDir("") + "/" + fileName);

                    Uri uri = Uri.fromFile(path);
                    list.add(prepareFilePart(formId, uri));
                    formId = "photo";
                    list.add(MultipartBody.Part.createFormData(formId + "[_id]", String.valueOf(file.get_id())));
                    list.add(MultipartBody.Part.createFormData(formId + "[uuid]", fileUuid));
                    list.add(MultipartBody.Part.createFormData(formId + "[userUuid]", file.getUser().getUuid()));
                    list.add(MultipartBody.Part.createFormData(formId + "[longitude]", String.valueOf(file.getLongitude())));
                    list.add(MultipartBody.Part.createFormData(formId + "[latitude]", String.valueOf(file.getLattitude())));
                    list.add(MultipartBody.Part.createFormData(formId + "[createdAt]", String.valueOf(file.getCreatedAt())));
                    list.add(MultipartBody.Part.createFormData(formId + "[changedAt]", String.valueOf(file.getChangedAt())));
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }

                // запросы делаем по одному, т.к. может сложиться ситуация когда будет попытка отправить
                // объём данных превышающий ограничения на отправку POST запросом на сервере
                Call<ResponseBody> call = SManApiFactory.getPhotoAlarmService().sendData(descr, list);
                try {
                    Response response = call.execute();
                    ResponseBody result = (ResponseBody) response.body();
                    if (response.isSuccessful()) {
                        Log.d(TAG, "successful");
                        JSONObject jObj = new JSONObject(result.string());
                        // при сохранении данных на сервере произошли ошибки
                        // данный флаг пока не используем
//                            boolean success = (boolean) jObj.get("success");
                        JSONArray jData = (JSONArray) jObj.get("data");
                        // устанавливаем флаг отправки записям которые подтвердил сервер
                        realm.beginTransaction();
                        for (int idx = 0; idx < jData.length(); idx++) {
                            JSONObject item = (JSONObject) jData.get(idx);
                            Long _id = Long.parseLong(item.get("_id").toString());
                            String uuid = item.get("uuid").toString();
                            PhotoAlarm sentItem = realm.where(PhotoAlarm.class)
                                    .equalTo("uuid", uuid)
                                    .findFirst();
                            // устанавливаем id присвоенное сервером
                            sentItem.set_id(_id);
                            sentItem.setSent(true);
                        }

                        realm.commitTransaction();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "Ошибка при отправке фотографий аварий.");
                }
            }
        }

        void sendPhotoHouse(Realm realm, long[] array) {
            int count = array.length;
            Long[] data = new Long[count];
            for (int i = 0; i < count; i++) {
                data[i] = array[i];
            }

            RealmResults<PhotoHouse> items = realm.where(PhotoHouse.class).in("_id", data)
                    .findAll();
            // добавляем в список файлы
            RequestBody descr = RequestBody.create(MultipartBody.FORM, "Photos due execution operation.");

            for (PhotoHouse file : items) {
                List<MultipartBody.Part> list = new ArrayList<>();
                try {
                    String formId = "file";
                    String fileUuid = file.getUuid();
                    String fileName = fileUuid + ".jpg";
                    File path = new File(getExternalFilesDir("") + "/" + fileName);

                    Uri uri = Uri.fromFile(path);
                    list.add(prepareFilePart(formId, uri));
                    formId = "photo";
                    list.add(MultipartBody.Part.createFormData(formId + "[_id]", String.valueOf(file.get_id())));
                    list.add(MultipartBody.Part.createFormData(formId + "[uuid]", fileUuid));
                    list.add(MultipartBody.Part.createFormData(formId + "[houseUuid]", file.getHouse().getUuid()));
                    list.add(MultipartBody.Part.createFormData(formId + "[userUuid]", file.getUser().getUuid()));
                    list.add(MultipartBody.Part.createFormData(formId + "[longitude]", String.valueOf(file.getLongitude())));
                    list.add(MultipartBody.Part.createFormData(formId + "[latitude]", String.valueOf(file.getLattitude())));
                    list.add(MultipartBody.Part.createFormData(formId + "[createdAt]", String.valueOf(file.getCreatedAt())));
                    list.add(MultipartBody.Part.createFormData(formId + "[changedAt]", String.valueOf(file.getChangedAt())));
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }

                // запросы делаем по одному, т.к. может сложиться ситуация когда будет попытка отправить
                // объём данных превышающий ограничения на отправку POST запросом на сервере
                Call<ResponseBody> call = SManApiFactory.getPhotoHouseService().sendData(descr, list);
                try {
                    Response response = call.execute();
                    ResponseBody result = (ResponseBody) response.body();
                    if (response.isSuccessful()) {
                        Log.d(TAG, "successful");
                        JSONObject jObj = new JSONObject(result.string());
                        // при сохранении данных на сервере произошли ошибки
                        // данный флаг пока не используем
//                            boolean success = (boolean) jObj.get("success");
                        JSONArray jData = (JSONArray) jObj.get("data");
                        // устанавливаем флаг отправки записям которые подтвердил сервер
                        realm.beginTransaction();
                        for (int idx = 0; idx < jData.length(); idx++) {
                            JSONObject item = (JSONObject) jData.get(idx);
                            Long _id = Long.parseLong(item.get("_id").toString());
                            String uuid = item.get("uuid").toString();
                            PhotoHouse sentItem = realm.where(PhotoHouse.class)
                                    .equalTo("uuid", uuid)
                                    .findFirst();
                            // устанавливаем id присвоенное сервером
                            sentItem.set_id(_id);
                            sentItem.setSent(true);
                        }

                        realm.commitTransaction();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "Ошибка при отправке фотографий домов.");
                }
            }
        }

        void sendPhotoFlat(Realm realm, long[] array) {
            int count = array.length;
            Long[] data = new Long[count];
            for (int i = 0; i < count; i++) {
                data[i] = array[i];
            }

            RealmResults<PhotoFlat> items = realm.where(PhotoFlat.class).in("_id", data)
                    .findAll();
            // добавляем в список файлы
            RequestBody descr = RequestBody.create(MultipartBody.FORM, "Photos due execution operation.");

            for (PhotoFlat file : items) {
                List<MultipartBody.Part> list = new ArrayList<>();
                try {
                    String formId = "file";
                    String fileUuid = file.getUuid();
                    String fileName = fileUuid + ".jpg";
                    File path = new File(getExternalFilesDir("") + "/" + fileName);

                    Uri uri = Uri.fromFile(path);
                    list.add(prepareFilePart(formId, uri));
                    formId = "photo";
                    list.add(MultipartBody.Part.createFormData(formId + "[_id]", String.valueOf(file.get_id())));
                    list.add(MultipartBody.Part.createFormData(formId + "[uuid]", fileUuid));
                    list.add(MultipartBody.Part.createFormData(formId + "[flatUuid]", file.getFlat().getUuid()));
                    list.add(MultipartBody.Part.createFormData(formId + "[userUuid]", file.getUser().getUuid()));
                    list.add(MultipartBody.Part.createFormData(formId + "[longitude]", String.valueOf(file.getLongitude())));
                    list.add(MultipartBody.Part.createFormData(formId + "[latitude]", String.valueOf(file.getLattitude())));
                    list.add(MultipartBody.Part.createFormData(formId + "[createdAt]", String.valueOf(file.getCreatedAt())));
                    list.add(MultipartBody.Part.createFormData(formId + "[changedAt]", String.valueOf(file.getChangedAt())));
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }

                // запросы делаем по одному, т.к. может сложиться ситуация когда будет попытка отправить
                // объём данных превышающий ограничения на отправку POST запросом на сервере
                Call<ResponseBody> call = SManApiFactory.getPhotoFlatService().sendData(descr, list);
                try {
                    Response response = call.execute();
                    ResponseBody result = (ResponseBody) response.body();
                    if (response.isSuccessful()) {
                        Log.d(TAG, "successful");
                        JSONObject jObj = new JSONObject(result.string());
                        // при сохранении данных на сервере произошли ошибки
                        // данный флаг пока не используем
//                            boolean success = (boolean) jObj.get("success");
                        JSONArray jData = (JSONArray) jObj.get("data");
                        // устанавливаем флаг отправки записям которые подтвердил сервер
                        realm.beginTransaction();
                        for (int idx = 0; idx < jData.length(); idx++) {
                            JSONObject item = (JSONObject) jData.get(idx);
                            Long _id = Long.parseLong(item.get("_id").toString());
                            String uuid = item.get("uuid").toString();
                            PhotoFlat sentItem = realm.where(PhotoFlat.class)
                                    .equalTo("uuid", uuid)
                                    .findFirst();
                            // устанавливаем id присвоенное сервером
                            sentItem.set_id(_id);
                            sentItem.setSent(true);
                        }

                        realm.commitTransaction();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "Ошибка при отправке фотографий квартир.");
                }
            }
        }

        void sendPhotoEquipment(Realm realm, long[] array) {
            int count = array.length;
            Long[] data = new Long[count];
            for (int i = 0; i < count; i++) {
                data[i] = array[i];
            }

            RealmResults<PhotoEquipment> items = realm.where(PhotoEquipment.class).in("_id", data)
                    .findAll();
            // добавляем в список файлы
            RequestBody descr = RequestBody.create(MultipartBody.FORM, "Photos due execution operation.");

            for (PhotoEquipment file : items) {
                List<MultipartBody.Part> list = new ArrayList<>();
                try {
                    String formId = "file";
                    String fileUuid = file.getUuid();
                    String fileName = fileUuid + ".jpg";
                    File path = new File(getExternalFilesDir("") + "/" + fileName);

                    Uri uri = Uri.fromFile(path);
                    list.add(prepareFilePart(formId, uri));
                    formId = "photo";
                    list.add(MultipartBody.Part.createFormData(formId + "[_id]", String.valueOf(file.get_id())));
                    list.add(MultipartBody.Part.createFormData(formId + "[uuid]", fileUuid));
                    list.add(MultipartBody.Part.createFormData(formId + "[equipmentUuid]", file.getEquipment().getUuid()));
                    list.add(MultipartBody.Part.createFormData(formId + "[userUuid]", file.getUser().getUuid()));
                    list.add(MultipartBody.Part.createFormData(formId + "[longitude]", String.valueOf(file.getLongitude())));
                    list.add(MultipartBody.Part.createFormData(formId + "[latitude]", String.valueOf(file.getLattitude())));
                    list.add(MultipartBody.Part.createFormData(formId + "[createdAt]", String.valueOf(file.getCreatedAt())));
                    list.add(MultipartBody.Part.createFormData(formId + "[changedAt]", String.valueOf(file.getChangedAt())));
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }

                // запросы делаем по одному, т.к. может сложиться ситуация когда будет попытка отправить
                // объём данных превышающий ограничения на отправку POST запросом на сервере
                Call<ResponseBody> call = SManApiFactory.getPhotoEquipmentService().sendData(descr, list);
                try {
                    Response response = call.execute();
                    ResponseBody result = (ResponseBody) response.body();
                    if (response.isSuccessful()) {
                        Log.d(TAG, "successful");
                        JSONObject jObj = new JSONObject(result.string());
                        // при сохранении данных на сервере произошли ошибки
                        // данный флаг пока не используем
//                            boolean success = (boolean) jObj.get("success");
                        JSONArray jData = (JSONArray) jObj.get("data");
                        // устанавливаем флаг отправки записям которые подтвердил сервер
                        realm.beginTransaction();
                        for (int idx = 0; idx < jData.length(); idx++) {
                            JSONObject item = (JSONObject) jData.get(idx);
                            Long _id = Long.parseLong(item.get("_id").toString());
                            String uuid = item.get("uuid").toString();
                            PhotoEquipment sentItem = realm.where(PhotoEquipment.class)
                                    .equalTo("uuid", uuid)
                                    .findFirst();
                            // устанавливаем id присвоенное сервером
                            sentItem.set_id(_id);
                            sentItem.setSent(true);
                        }

                        realm.commitTransaction();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "Ошибка при отправке фотографий оборудования.");
                }
            }
        }

        void sendFlat(Realm realm, long[] array) {
            int count = array.length;
            Long[] data = new Long[count];
            for (int i = 0; i < count; i++) {
                data[i] = array[i];
            }

            RealmResults<Flat> items = realm.where(Flat.class).in("_id", data)
                    .findAll();
            // отправляем данные с оборудованием
            Call<ResponseBody> call = SManApiFactory.getFlatService().sendData(realm.copyFromRealm(items));
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
                        String uuid = item.get("uuid").toString();
                        Flat sentItem = realm.where(Flat.class).equalTo("uuid", uuid).findFirst();
                        sentItem.setSent(true);
                    }

                    realm.commitTransaction();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Ошибка при отправке квартир.");
            }
        }

        void sendMessage(Realm realm, long[] array) {
            int count = array.length;
            Long[] data = new Long[count];
            for (int i = 0; i < count; i++) {
                data[i] = array[i];
            }

            RealmResults<Message> items = realm.where(Message.class).in("_id", data)
                    .findAll();
            // отправляем данные с сообщениями
            Call<ResponseBody> call = SManApiFactory.getMessageService().sendData(realm.copyFromRealm(items));
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
                        Message sentItem = realm.where(Message.class).equalTo("uuid", uuid).findFirst();
                        sentItem.set_id(_id);
                        sentItem.setSent(true);
                    }

                    realm.commitTransaction();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Ошибка при отправке сообщений.");
            }
        }

        void sendPhotoMessage(Realm realm, long[] array) {
            int count = array.length;
            Long[] data = new Long[count];
            for (int i = 0; i < count; i++) {
                data[i] = array[i];
            }

            RealmResults<PhotoMessage> items = realm.where(PhotoMessage.class).in("_id", data)
                    .findAll();
            // добавляем в список файлы
            RequestBody descr = RequestBody.create(MultipartBody.FORM, "Photos due execution operation.");

            for (PhotoMessage file : items) {
                List<MultipartBody.Part> list = new ArrayList<>();
                try {
                    String formId = "file";
                    String fileUuid = file.getUuid();
                    String fileName = fileUuid + ".jpg";
                    File path = new File(getExternalFilesDir("") + "/" + fileName);

                    Uri uri = Uri.fromFile(path);
                    list.add(prepareFilePart(formId, uri));
                    formId = "photo";
                    list.add(MultipartBody.Part.createFormData(formId + "[_id]", String.valueOf(file.get_id())));
                    list.add(MultipartBody.Part.createFormData(formId + "[uuid]", fileUuid));
                    list.add(MultipartBody.Part.createFormData(formId + "[messageUuid]", file.getMessage().getUuid()));
                    list.add(MultipartBody.Part.createFormData(formId + "[longitude]", String.valueOf(file.getLongitude())));
                    list.add(MultipartBody.Part.createFormData(formId + "[latitude]", String.valueOf(file.getLattitude())));
                    list.add(MultipartBody.Part.createFormData(formId + "[createdAt]", String.valueOf(file.getCreatedAt())));
                    list.add(MultipartBody.Part.createFormData(formId + "[changedAt]", String.valueOf(file.getChangedAt())));
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }

                // запросы делаем по одному, т.к. может сложиться ситуация когда будет попытка отправить
                // объём данных превышающий ограничения на отправку POST запросом на сервере
                Call<ResponseBody> call = SManApiFactory.getPhotoMessageService().sendData(descr, list);
                try {
                    Response response = call.execute();
                    ResponseBody result = (ResponseBody) response.body();
                    if (response.isSuccessful()) {
                        Log.d(TAG, "successful");
                        JSONObject jObj = new JSONObject(result.string());
                        // при сохранении данных на сервере произошли ошибки
                        // данный флаг пока не используем
//                            boolean success = (boolean) jObj.get("success");
                        JSONArray jData = (JSONArray) jObj.get("data");
                        // устанавливаем флаг отправки записям которые подтвердил сервер
                        realm.beginTransaction();
                        for (int idx = 0; idx < jData.length(); idx++) {
                            JSONObject item = (JSONObject) jData.get(idx);
                            Long _id = Long.parseLong(item.get("_id").toString());
                            String uuid = item.get("uuid").toString();
                            PhotoMessage sentItem = realm.where(PhotoMessage.class)
                                    .equalTo("uuid", uuid)
                                    .findFirst();
                            // устанавливаем id присвоенное сервером
                            sentItem.set_id(_id);
                            sentItem.setSent(true);
                        }

                        realm.commitTransaction();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "Ошибка при отправке фотографий сообщений.");
                }
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
            photoAlarmIds = intent.getLongArrayExtra(PHOTO_ALARM_IDS);
            photoHouseIds = intent.getLongArrayExtra(PHOTO_HOUSE_IDS);
            photoFlatIds = intent.getLongArrayExtra(PHOTO_FLAT_IDS);
            photoEquipmentIds = intent.getLongArrayExtra(PHOTO_EQUIPMENT_IDS);
            flatIds = intent.getLongArrayExtra(FLAT_IDS);
            messageIds = intent.getLongArrayExtra(MESSAGE_IDS);
            photoMessageIds = intent.getLongArrayExtra(PHOTO_MESSAGE_IDS);
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

    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        File file = new File(fileUri.getPath());
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(fileUri.getPath());
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }

        MediaType mediaType = MediaType.parse(type);
        RequestBody requestFile = RequestBody.create(mediaType, file);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }
}
