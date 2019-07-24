package ru.shtrm.serviceman.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import ru.shtrm.serviceman.data.AlarmStatus;
import ru.shtrm.serviceman.data.AlarmType;
import ru.shtrm.serviceman.data.AuthorizedUser;
import ru.shtrm.serviceman.data.City;
import ru.shtrm.serviceman.data.Defect;
import ru.shtrm.serviceman.data.DefectType;
import ru.shtrm.serviceman.data.Documentation;
import ru.shtrm.serviceman.data.DocumentationType;
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.EquipmentStatus;
import ru.shtrm.serviceman.data.EquipmentSystem;
import ru.shtrm.serviceman.data.EquipmentType;
import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.HouseStatus;
import ru.shtrm.serviceman.data.HouseType;
import ru.shtrm.serviceman.data.Journal;
import ru.shtrm.serviceman.data.MeasureType;
import ru.shtrm.serviceman.data.Message;
import ru.shtrm.serviceman.data.OperationTemplate;
import ru.shtrm.serviceman.data.ReferenceUpdate;
import ru.shtrm.serviceman.data.Street;
import ru.shtrm.serviceman.data.Task;
import ru.shtrm.serviceman.data.TaskTemplate;
import ru.shtrm.serviceman.data.TaskType;
import ru.shtrm.serviceman.data.TaskVerdict;
import ru.shtrm.serviceman.data.UpdateQuery;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.data.WorkStatus;
import ru.shtrm.serviceman.data.ZhObject;
import ru.shtrm.serviceman.data.ZhObjectStatus;
import ru.shtrm.serviceman.data.ZhObjectType;
import ru.shtrm.serviceman.retrofit.SManApiFactory;
import ru.shtrm.serviceman.retrofit.ServiceApiFactory;

public class GetReferenceService extends Service {
    public static final String ACTION = "ru.shtrm.serviceman.service.GET_REFERENCE";
    private static final String TAG = GetReferenceService.class.getSimpleName();
    private boolean isRuning;
    private String userUuid;

    /**
     * Метод для выполнения приёма данных с сервера.
     */
    private Runnable task = new Runnable() {
        @Override
        public void run() {

            Log.d(TAG, "run() started...");
            AuthorizedUser user = AuthorizedUser.getInstance();
            boolean isValidUser = user.getUser() != null && user.getToken() != null;
            if (!isValidUser) {
                finishService();
                return;
            }

            userUuid = user.getUser().getUuid();

            // обновляем справочники
            Realm realm = Realm.getDefaultInstance();

            Date updateDate = new Date();
            List<? extends RealmModel> list;

            list = AlarmStatus.getData();
            if (list != null) {
                if (list.size() > 0) {
                    saveData(list, updateDate, realm);
                }
            } else {
                Journal.add("AlarmStatus not updated.");
            }

            list = AlarmType.getData();
            if (list != null) {
                if (list.size() > 0) {
                    saveData(list, updateDate, realm);
                }
            } else {
                Journal.add("AlarmType not updated.");
            }

            list = DefectType.getData();
            if (list != null) {
                if (list.size() > 0) {
                    saveData(list, updateDate, realm);
                }
            } else {
                Journal.add("DefectType not updated.");
            }

            list = DocumentationType.getData();
            if (list != null) {
                if (list.size() > 0) {
                    saveData(list, updateDate, realm);
                }
            } else {
                Journal.add("DocumentationType not updated.");
            }

            list = EquipmentStatus.getData();
            if (list != null) {
                if (list.size() > 0) {
                    saveData(list, updateDate, realm);
                }
            } else {
                Journal.add("EquipmentStatus not updated.");
            }

            list = HouseStatus.getData();
            if (list != null) {
                if (list.size() > 0) {
                    saveData(list, updateDate, realm);
                }
            } else {
                Journal.add("HouseStatus not updated.");
            }

            list = MeasureType.getData();
            if (list != null) {
                if (list.size() > 0) {
                    saveData(list, updateDate, realm);
                }
            } else {
                Journal.add("MeasureType not updated.");
            }

            list = ZhObjectStatus.getData();
            if (list != null) {
                if (list.size() > 0) {
                    saveData(list, updateDate, realm);
                }
            } else {
                Journal.add("ObjectStatus not updated.");
            }

            list = ZhObjectType.getData();
            if (list != null) {
                if (list.size() > 0) {
                    saveData(list, updateDate, realm);
                }
            } else {
                Journal.add("ObjectType not updated.");
            }

            list = TaskType.getData();
            if (list != null) {
                if (list.size() > 0) {
                    saveData(list, updateDate, realm);
                }
            } else {
                Journal.add("TaskType not updated.");
            }

            list = TaskVerdict.getData();
            if (list != null) {
                if (list.size() > 0) {
                    saveData(list, updateDate, realm);
                }
            } else {
                Journal.add("TaskVerdict not updated.");
            }

            list = WorkStatus.getData();
            if (list != null) {
                if (list.size() > 0) {
                    saveData(list, updateDate, realm);
                }
            } else {
                Journal.add("WorkStatus not updated.");
            }

            list = EquipmentSystem.getData();
            if (list != null) {
                if (list.size() > 0) {
                    saveData(list, updateDate, realm);
                }
            } else {
                Journal.add("EquipmentSystem not updated.");
            }

            if (AuthorizedUser.getInstance().isValidToken()) {
                list = User.getData();
                if (list != null) {
                    if (list.size() > 0) {
                        saveData(list, updateDate, realm);
                    }
                } else {
                    Journal.add("User not updated.");
                }
            } else {
                if (!updateUserByService(realm)) {
                    Journal.add("User not updated by service user.");
                }
            }

            // --------
            list = City.getData();
            if (list != null) {
                if (list.size() > 0) {
                    saveData(list, updateDate, realm);
                }
            } else {
                Journal.add("City not updated.");
            }

            list = Street.getData();
            if (list != null) {
                if (list.size() > 0) {
                    saveData(list, updateDate, realm);
                }
            } else {
                Journal.add("Street not updated.");
            }

            list = HouseType.getData();
            if (list != null) {
                if (list.size() > 0) {
                    saveData(list, updateDate, realm);
                }
            } else {
                Journal.add("HouseType not updated.");
            }

            list = House.getData();
            if (list != null) {
                if (list.size() > 0) {
                    saveData(list, updateDate, realm);
                }
            } else {
                Journal.add("House not updated.");
            }

            list = ZhObject.getData();
            if (list != null) {
                if (list.size() > 0) {
                    saveData(list, updateDate, realm);
                }
            } else {
                Journal.add("Object not updated.");
            }

            list = EquipmentType.getData();
            if (list != null) {
                if (list.size() > 0) {
                    saveData(list, updateDate, realm);
                }
            } else {
                Journal.add("EquipmentType not updated.");
            }

            list = Equipment.getData();
            if (list != null) {
                if (list.size() > 0) {
                    saveData(list, updateDate, realm);
                }
            } else {
                Journal.add("Equipment not updated.");
            }

            list = Defect.getData();
            if (list != null) {
                if (list.size() > 0) {
                    saveData(list, updateDate, realm);
                }
            } else {
                Journal.add("Defect not updated.");
            }

            list = Documentation.getData();
            if (list != null) {
                if (list.size() > 0) {
                    saveData(list, updateDate, realm);
                }
            } else {
                Journal.add("Documentation not updated.");
            }

            list = Message.getData();
            if (list != null) {
                if (list.size() > 0) {
                    saveData(list, updateDate, realm);
                }
            } else {
                Journal.add("Message not updated.");
            }

            list = OperationTemplate.getData();
            if (list != null) {
                if (list.size() > 0) {
                    saveData(list, updateDate, realm);
                }
            } else {
                Journal.add("OperationTemplate not updated.");
            }

            list = TaskTemplate.getData();
            if (list != null) {
                if (list.size() > 0) {
                    saveData(list, updateDate, realm);
                }
            } else {
                Journal.add("TaskTemplate not updated.");
            }

            if (!getNewTask(realm)) {
                Journal.add("Task not received.");
            }

            realm.close();

            Log.d(TAG, "run() ended...");
            finishService();
        }
    };

    private void saveData(List<? extends RealmModel> list, Date updateDate, Realm realm) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(list);
        realm.commitTransaction();
        ReferenceUpdate.saveReferenceData(list.get(0).getClass().getSimpleName() + ":" + userUuid, updateDate, realm);
    }

    private boolean updateUserByService(Realm realm) {
        String lastUpdate;
        String rName = User.class.getSimpleName();
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);
        SharedPreferences sp = getSharedPreferences(User.SERVICE_USER_UUID, Context.MODE_PRIVATE);
        String token = sp.getString("token", null);
        if (token == null) {
            return false;
        } else {
            ServiceApiFactory.setToken(token);
        }

        Call<List<User>> call = ServiceApiFactory.getUsersService().getData(lastUpdate);
        try {
            Response<List<User>> response = call.execute();
            if (response.isSuccessful()) {
                List<User> list = response.body();
                if (list.size() > 0) {
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(list);
                    realm.commitTransaction();
                    ReferenceUpdate.saveReferenceData(rName, updateDate, realm);
                }

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean getNewTask(Realm realm) {
        List<Task> list;
        List<UpdateQuery> changes = new ArrayList<>();

        Call<List<Task>> call = SManApiFactory.getTaskService().getByStatus(WorkStatus.Status.NEW);
        try {
            Response<List<Task>> response = call.execute();
            if (response.isSuccessful()) {
                list = response.body();
                if (list.size() > 0) {
                    WorkStatus inWorkStatus = realm.where(WorkStatus.class)
                            .equalTo("uuid", WorkStatus.Status.IN_WORK).findFirst();
                    Date date = new Date();

                    // проставляем дату получения задач
                    for (Task item : list) {
                        item.setStartDate(date);
                        if (item.getWorkStatus().getUuid().equals(WorkStatus.Status.NEW)) {
                            // устанавливаем статус "В работе"
                            item.setWorkStatus(inWorkStatus);

                            UpdateQuery changedAttr = new UpdateQuery(item.getClass().getSimpleName(),
                                    item.getUuid(), "workStatusUuid", inWorkStatus.getUuid(),
                                    item.getChangedAt());
                            changedAttr.set_id(UpdateQuery.getLastId() + 1);
                            changes.add(changedAttr);
                        }
                    }

                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(list);
                    realm.commitTransaction();

                    // если есть новые задачи, отправляем подтверждение о получении
                    for (UpdateQuery item : changes) {
                        boolean success;
                        Call<ResponseBody> callUpdAttr = SManApiFactory.getTaskService().updateAttribute(item);
                        try {
                            Response<ResponseBody> responseUpdAttr = callUpdAttr.execute();
                            if (responseUpdAttr.isSuccessful()) {
                                JSONObject jObj = new JSONObject(responseUpdAttr.body().string());
                                success = (boolean) jObj.get("success");
                            } else {
                                success = false;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            success = false;
                        }

                        // что-то пошло не так, сохраняем данные в очередь на отправку
                        if (!success) {
                            realm.beginTransaction();
                            realm.copyToRealmOrUpdate(item);
                            realm.commitTransaction();
                        }
                    }
                }

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

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
            Log.d(TAG, "Запускаем поток получения справочников с сервера...");
            isRuning = true;
            new Thread(task).start();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
        isRuning = false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void finishService() {
        Log.d(TAG, "finishService()");
        stopSelf();
    }
}
