package ru.shtrm.serviceman.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Response;
import ru.shtrm.serviceman.data.AlarmStatus;
import ru.shtrm.serviceman.data.AlarmType;
import ru.shtrm.serviceman.data.AuthorizedUser;
import ru.shtrm.serviceman.data.City;
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.EquipmentStatus;
import ru.shtrm.serviceman.data.EquipmentType;
import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.HouseStatus;
import ru.shtrm.serviceman.data.HouseType;
import ru.shtrm.serviceman.data.Journal;
import ru.shtrm.serviceman.data.ReferenceUpdate;
import ru.shtrm.serviceman.data.Street;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.data.UserHouse;
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

            if (!updateCity(realm)) {
                Journal.add("City not updated.");
            }

            if (!updateAlarmType(realm)) {
                Journal.add("AlarmType not updated.");
            }

            if (!updateAlarmStatus(realm)) {
                Journal.add("AlarmStatus not updated.");
            }

            if (!updateHouseType(realm)) {
                Journal.add("HouseType not updated.");
            }

            if (!updateHouseStatus(realm)) {
                Journal.add("HouseStatus not updated.");
            }

            if (!updateEquipmentType(realm)) {
                Journal.add("EquipmentType not updated.");
            }

            if (!updateEquipmentStatus(realm)) {
                Journal.add("EquipmentStatus not updated.");
            }

            if (AuthorizedUser.getInstance().isValidToken()) {
                if (!updateUser(realm)) {
                    Journal.add("User not updated.");
                }
            } else {
                if (!updateUserByService(realm)) {
                    Journal.add("User not updated by service user.");
                }
            }

            if (!updateStreet(realm)) {
                Journal.add("Street not updated.");
            }

            if (!updateHouse(realm)) {
                Journal.add("House not updated.");
            }

            if (!updateEquipment(realm)) {
                Journal.add("Equipment not updated.");
            }

            if (!updateUserHouse(realm)) {
                Journal.add("UserHouse not updated.");
            }

            realm.close();

            Log.d(TAG, "run() ended...");
            finishService();
        }
    };

    private boolean updateAlarmStatus(Realm realm) {
        String lastUpdate;
        String rName = AlarmStatus.class.getSimpleName() + ":" + userUuid;
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);
        Call<List<AlarmStatus>> call = SManApiFactory.getAlarmStatusService().getData(lastUpdate);
        try {
            Response<List<AlarmStatus>> response = call.execute();
            if (response.isSuccessful()) {
                List<AlarmStatus> list = response.body();
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

    private boolean updateAlarmType(Realm realm) {
        String lastUpdate;
        String rName = AlarmType.class.getSimpleName() + ":" + userUuid;
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);
        Call<List<AlarmType>> call = SManApiFactory.getAlarmTypeService().getData(lastUpdate);
        try {
            Response<List<AlarmType>> response = call.execute();
            if (response.isSuccessful()) {
                List<AlarmType> list = response.body();
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

    private boolean updateCity(Realm realm) {
        String lastUpdate;
        String rName = City.class.getSimpleName() + ":" + userUuid;
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);
        Call<List<City>> call = SManApiFactory.getCityService().getData(lastUpdate);
        try {
            Response<List<City>> response = call.execute();
            if (response.isSuccessful()) {
                List<City> list = response.body();
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

    private boolean updateStreet(Realm realm) {
        String lastUpdate;
        String rName = Street.class.getSimpleName() + ":" + userUuid;
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);
        Call<List<Street>> call = SManApiFactory.getStreetService().getData(lastUpdate);
        try {
            Response<List<Street>> response = call.execute();
            if (response.isSuccessful()) {
                List<Street> list = response.body();
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

    private boolean updateHouseStatus(Realm realm) {
        String lastUpdate;
        String rName = HouseStatus.class.getSimpleName() + ":" + userUuid;
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);
        Call<List<HouseStatus>> call = SManApiFactory.getHouseStatusService().getData(lastUpdate);
        try {
            Response<List<HouseStatus>> response = call.execute();
            if (response.isSuccessful()) {
                List<HouseStatus> list = response.body();
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

    private boolean updateHouse(Realm realm) {
        String lastUpdate;
        String rName = House.class.getSimpleName() + ":" + userUuid;
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);
        Call<List<House>> call = SManApiFactory.getHouseService().getData(lastUpdate);
        try {
            Response<List<House>> response = call.execute();
            if (response.isSuccessful()) {
                List<House> list = response.body();
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

    private boolean updateEquipmentStatus(Realm realm) {
        String lastUpdate;
        String rName = EquipmentStatus.class.getSimpleName() + ":" + userUuid;
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);
        Call<List<EquipmentStatus>> call = SManApiFactory.getEquipmentStatusService().getData(lastUpdate);
        try {
            Response<List<EquipmentStatus>> response = call.execute();
            if (response.isSuccessful()) {
                List<EquipmentStatus> list = response.body();
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

    private boolean updateEquipmentType(Realm realm) {
        String lastUpdate;
        String rName = EquipmentType.class.getSimpleName() + ":" + userUuid;
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);
        Call<List<EquipmentType>> call = SManApiFactory.getEquipmentTypeService().getData(lastUpdate);
        try {
            Response<List<EquipmentType>> response = call.execute();
            if (response.isSuccessful()) {
                List<EquipmentType> list = response.body();
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

    private boolean updateEquipment(Realm realm) {
        String lastUpdate;
        String rName = Equipment.class.getSimpleName() + ":" + userUuid;
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);
        Call<List<Equipment>> call = SManApiFactory.getEquipmentService().getData(lastUpdate);
        try {
            Response<List<Equipment>> response = call.execute();
            if (response.isSuccessful()) {
                List<Equipment> list = response.body();
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

    private boolean updateUser(Realm realm) {
        String lastUpdate;
        String rName = User.class.getSimpleName() + ":" + userUuid;
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);
        Call<List<User>> call = SManApiFactory.getUsersService().getData(lastUpdate);
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

    private boolean updateHouseType(Realm realm) {
        String lastUpdate;
        String rName = HouseType.class.getSimpleName() + ":" + userUuid;
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);
        Call<List<HouseType>> call = SManApiFactory.getHouseTypeService().getData(lastUpdate);
        try {
            Response<List<HouseType>> response = call.execute();
            if (response.isSuccessful()) {
                List<HouseType> list = response.body();
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

    private boolean updateUserHouse(Realm realm) {
        String lastUpdate;
        String rName = UserHouse.class.getSimpleName() + ":" + userUuid;
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);
        Call<List<UserHouse>> call = SManApiFactory.getUserHouseService().getData(lastUpdate);
        try {
            Response<List<UserHouse>> response = call.execute();
            if (response.isSuccessful()) {
                List<UserHouse> list = response.body();
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
