package ru.shtrm.serviceman.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.NonNull;
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
import io.realm.Sort;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import ru.shtrm.serviceman.data.Alarm;
import ru.shtrm.serviceman.data.Message;
import ru.shtrm.serviceman.data.Photo;
import ru.shtrm.serviceman.data.UpdateQuery;
import ru.shtrm.serviceman.retrofit.SManApiFactory;

public class SendDataService extends Service {
    public static final String ACTION = "ru.shtrm.serviceman.service.SEND_DATA";
    public static final String ALARM_IDS = "alramIds";
    public static final String MESSAGE_IDS = "messageIds";
    private static final String TAG = SendDataService.class.getSimpleName();
    private boolean isRunning;

    private long alarmIds[];
    private long messageIds[];

    /**
     * Метод для выполнения отправки данных на сервер.
     */
    private Runnable task = new Runnable() {
        @Override
        public void run() {
            Realm realm = Realm.getDefaultInstance();

            // отправка аварий
            if (alarmIds != null && alarmIds.length > 0) {
                sendAlarm(realm, alarmIds);
            }

            // отправка сообщений
            if (messageIds != null && messageIds.length > 0) {
                sendMessage(realm, messageIds);
            }

            // отправка очереди изменённых атрибутов
            sendUpdateQuery(realm);

            realm.close();

            // останавливаем сервис
            stopSelf();
        }

        void sendUpdateQuery(Realm realm) {
            RealmResults<UpdateQuery> queryList = realm.where(UpdateQuery.class).findAllSorted("createdAt", Sort.ASCENDING);
            Call<ResponseBody> call;
            Response<ResponseBody> response;
            File photoFile;

            for (UpdateQuery query : queryList) {
                photoFile = null;
                call = null;
                switch (query.getModelClass()) {
                    case "Task":
                        call = SManApiFactory.getTaskService().updateAttribute(realm.copyFromRealm(query));
                        break;
                    case "Equipment":
                        call = SManApiFactory.getEquipmentService().updateAttribute(realm.copyFromRealm(query));
                        break;
                    case "Operation":
                        call = SManApiFactory.getOperationService().updateAttribute(realm.copyFromRealm(query));
                        break;
                    case "Photo":
                        Context context = getApplicationContext();
                        File extDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                        if (extDir != null) {
                            List<MultipartBody.Part> files = new ArrayList<>();
                            try {
                                photoFile = new File(extDir.getAbsolutePath(), query.getModelUuid() + ".jpg");
                                Uri uri = Uri.fromFile(photoFile);
                                String formId = "file";
                                files.add(prepareFilePart(formId, uri));
                            } catch (Exception e) {
                                e.printStackTrace();
                                continue;
                            }

                            RequestBody rb_id = RequestBody.create(MultipartBody.FORM, "" + query.get_id());
                            RequestBody rbModelClass = RequestBody.create(MultipartBody.FORM, query.getModelClass());
                            RequestBody rbModelUuid = RequestBody.create(MultipartBody.FORM, query.getModelUuid());
                            RequestBody rbAttribute = RequestBody.create(MultipartBody.FORM, "");
                            RequestBody rbValue = RequestBody.create(MultipartBody.FORM, query.getValue());
                            RequestBody rbCreatedAt = RequestBody.create(MultipartBody.FORM, query.getCreatedAt().toString());
                            RequestBody rbChangedAt = RequestBody.create(MultipartBody.FORM, query.getChangedAt().toString());
                            call = SManApiFactory.getPhotoService().updateAttribute(
                                    rb_id,
                                    rbModelClass,
                                    rbModelUuid,
                                    rbAttribute,
                                    rbValue,
                                    rbCreatedAt,
                                    rbChangedAt,
                                    files);
                        }

                        break;
                    case "Message":
                        call = SManApiFactory.getMessageService().updateAttribute(realm.copyFromRealm(query));
                        break;
                    case "Measure":
                        call = SManApiFactory.getMeasureService().updateAttribute(realm.copyFromRealm(query));
                        break;
                    case "Journal":
                        call = SManApiFactory.getJournalService().updateAttribute(realm.copyFromRealm(query));
                        break;
                    case "GpsTrack":
                        call = SManApiFactory.getGpsTrackService().updateAttribute(realm.copyFromRealm(query));
                        break;
                    case "Defect":
                        call = SManApiFactory.getDefectService().updateAttribute(realm.copyFromRealm(query));
                        break;
                    case "Alarm":
                        call = SManApiFactory.getAlarmService().updateAttribute(realm.copyFromRealm(query));
                        break;
                    default:
                        call = null;
                        break;
                }

                if (call == null) {
                    return;
                }

                try {
                    response = call.execute();
                    if (response.isSuccessful()) {
                        JSONObject jObj = new JSONObject(response.body().string());
                        boolean success = jObj.getBoolean("success");
                        if (success) {
                            if (query.getModelClass().equals(Photo.class.getSimpleName())) {
                                if (photoFile != null && !photoFile.delete()) {
                                    Log.e(TAG, "Can`t delete " + photoFile.getAbsolutePath());
                                }
                            }

                            long jData = jObj.getLong("data");
                            realm.beginTransaction();
                            realm.where(UpdateQuery.class).equalTo("_id", jData).findAll().deleteAllFromRealm();
                            realm.commitTransaction();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
            alarmIds = intent.getLongArrayExtra(ALARM_IDS);
            messageIds = intent.getLongArrayExtra(MESSAGE_IDS);
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

    @NonNull
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
