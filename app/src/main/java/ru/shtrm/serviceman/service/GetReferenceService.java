package ru.shtrm.serviceman.service;

import android.app.Service;
import android.content.Intent;
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
import ru.shtrm.serviceman.data.Flat;
import ru.shtrm.serviceman.data.FlatStatus;
import ru.shtrm.serviceman.data.FlatType;
import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.HouseStatus;
import ru.shtrm.serviceman.data.HouseType;
import ru.shtrm.serviceman.data.Journal;
import ru.shtrm.serviceman.data.ReferenceUpdate;
import ru.shtrm.serviceman.data.Resident;
import ru.shtrm.serviceman.data.Street;
import ru.shtrm.serviceman.data.Subject;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.retrofit.SManApiFactory;

public class GetReferenceService extends Service {
    public static final String ACTION = "ru.shtrm.serviceman.service.GET_REFERENCE";
    private static final String TAG = GetReferenceService.class.getSimpleName();
    private boolean isRuning;

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

            // обновляем справочники
            Realm realm = Realm.getDefaultInstance();

            if (!updateAlarmStatus(realm)) {
                Journal.add("AlarmStatus not updated.");
            }

            if (!updateAlarmType(realm)) {
                Journal.add("AlarmType not updated.");
            }

            if (!updateCity(realm)) {
                Journal.add("City not updated.");
            }

            if (!updateStreet(realm)) {
                Journal.add("Street not updated.");
            }

            if (!updateHouseType(realm)) {
                Journal.add("HouseType not updated.");
            }

            if (!updateHouseStatus(realm)) {
                Journal.add("HouseStatus not updated.");
            }

            if (!updateHouse(realm)) {
                Journal.add("House not updated.");
            }

            if (!updateFlatType(realm)) {
                Journal.add("FlatType not updated.");
            }

            if (!updateFlatStatus(realm)) {
                Journal.add("FlatStatus not updated.");
            }

            if (!updateFlat(realm)) {
                Journal.add("Flat not updated.");
            }

            if (!updateEquipmentStatus(realm)) {
                Journal.add("EquipmentStatus not updated.");
            }

            if (!updateEquipmentType(realm)) {
                Journal.add("EquipmentType not updated.");
            }

            if (!updateEquipment(realm)) {
                Journal.add("Equipment not updated.");
            }

            if (!updateSubject(realm)) {
                Journal.add("Subject not updated.");
            }

            if (!updateResident(realm)) {
                Journal.add("Resident not updated.");
            }

            if (!updateUser(realm)) {
                Journal.add("User not updated.");
            }

            realm.close();

            Log.d(TAG, "run() ended...");
            finishService();
        }
    };

    private boolean updateAlarmStatus(Realm realm) {
        String lastUpdate;
        String rName = AlarmStatus.class.getSimpleName();
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);
        Call<List<AlarmStatus>> call = SManApiFactory.getAlarmStatusService().getData(lastUpdate);
        try {
            Response<List<AlarmStatus>> response = call.execute();
            if (response.isSuccessful()) {
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(response.body());
                realm.commitTransaction();
                ReferenceUpdate.saveReferenceData(rName, updateDate, realm);
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
        String rName = AlarmType.class.getSimpleName();
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);
        Call<List<AlarmType>> call = SManApiFactory.getAlarmTypeService().getData(lastUpdate);
        try {
            Response<List<AlarmType>> response = call.execute();
            if (response.isSuccessful()) {
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(response.body());
                realm.commitTransaction();
                ReferenceUpdate.saveReferenceData(rName, updateDate, realm);
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
        String rName = City.class.getSimpleName();
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);
        Call<List<City>> call = SManApiFactory.getCityService().getData(lastUpdate);
        try {
            Response<List<City>> response = call.execute();
            if (response.isSuccessful()) {
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(response.body());
                realm.commitTransaction();
                ReferenceUpdate.saveReferenceData(rName, updateDate, realm);
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
        String rName = Street.class.getSimpleName();
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);
        Call<List<Street>> call = SManApiFactory.getStreetService().getData(lastUpdate);
        try {
            Response<List<Street>> response = call.execute();
            if (response.isSuccessful()) {
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(response.body());
                realm.commitTransaction();
                ReferenceUpdate.saveReferenceData(rName, updateDate, realm);
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
        String rName = HouseStatus.class.getSimpleName();
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);
        Call<List<HouseStatus>> call = SManApiFactory.getHouseStatusService().getData(lastUpdate);
        try {
            Response<List<HouseStatus>> response = call.execute();
            if (response.isSuccessful()) {
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(response.body());
                realm.commitTransaction();
                ReferenceUpdate.saveReferenceData(rName, updateDate, realm);
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
        String rName = House.class.getSimpleName();
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);
        Call<List<House>> call = SManApiFactory.getHouseService().getData(lastUpdate);
        try {
            Response<List<House>> response = call.execute();
            if (response.isSuccessful()) {
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(response.body());
                realm.commitTransaction();
                ReferenceUpdate.saveReferenceData(rName, updateDate, realm);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean updateFlatStatus(Realm realm) {
        String lastUpdate;
        String rName = FlatStatus.class.getSimpleName();
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);
        Call<List<FlatStatus>> call = SManApiFactory.getFlatStatusService().getData(lastUpdate);
        try {
            Response<List<FlatStatus>> response = call.execute();
            if (response.isSuccessful()) {
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(response.body());
                realm.commitTransaction();
                ReferenceUpdate.saveReferenceData(rName, updateDate, realm);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean updateFlat(Realm realm) {
        String lastUpdate;
        String rName = Flat.class.getSimpleName();
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);
        Call<List<Flat>> call = SManApiFactory.getFlatService().getData(lastUpdate);
        try {
            Response<List<Flat>> response = call.execute();
            if (response.isSuccessful()) {
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(response.body());
                realm.commitTransaction();
                ReferenceUpdate.saveReferenceData(rName, updateDate, realm);
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
        String rName = EquipmentStatus.class.getSimpleName();
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);
        Call<List<EquipmentStatus>> call = SManApiFactory.getEquipmentStatusService().getData(lastUpdate);
        try {
            Response<List<EquipmentStatus>> response = call.execute();
            if (response.isSuccessful()) {
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(response.body());
                realm.commitTransaction();
                ReferenceUpdate.saveReferenceData(rName, updateDate, realm);
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
        String rName = EquipmentType.class.getSimpleName();
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);
        Call<List<EquipmentType>> call = SManApiFactory.getEquipmentTypeService().getData(lastUpdate);
        try {
            Response<List<EquipmentType>> response = call.execute();
            if (response.isSuccessful()) {
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(response.body());
                realm.commitTransaction();
                ReferenceUpdate.saveReferenceData(rName, updateDate, realm);
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
        String rName = Equipment.class.getSimpleName();
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);
        Call<List<Equipment>> call = SManApiFactory.getEquipmentService().getData(lastUpdate);
        try {
            Response<List<Equipment>> response = call.execute();
            if (response.isSuccessful()) {
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(response.body());
                realm.commitTransaction();
                ReferenceUpdate.saveReferenceData(rName, updateDate, realm);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean updateSubject(Realm realm) {
        String lastUpdate;
        String rName = Subject.class.getSimpleName();
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);
        Call<List<Subject>> call = SManApiFactory.getSubjectService().getData(lastUpdate);
        try {
            Response<List<Subject>> response = call.execute();
            if (response.isSuccessful()) {
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(response.body());
                realm.commitTransaction();
                ReferenceUpdate.saveReferenceData(rName, updateDate, realm);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean updateResident(Realm realm) {
        String lastUpdate;
        String rName = Resident.class.getSimpleName();
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);
        Call<List<Resident>> call = SManApiFactory.getResidentService().getData(lastUpdate);
        try {
            Response<List<Resident>> response = call.execute();
            if (response.isSuccessful()) {
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(response.body());
                realm.commitTransaction();
                ReferenceUpdate.saveReferenceData(rName, updateDate, realm);
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
        String rName = User.class.getSimpleName();
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);
        Call<List<User>> call = SManApiFactory.getUsersService().getData(lastUpdate);
        try {
            Response<List<User>> response = call.execute();
            if (response.isSuccessful()) {
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(response.body());
                realm.commitTransaction();
                ReferenceUpdate.saveReferenceData(rName, updateDate, realm);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean updateFlatType(Realm realm) {
        String lastUpdate;
        String rName = FlatType.class.getSimpleName();
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);
        Call<List<FlatType>> call = SManApiFactory.getFlatTypeService().getData(lastUpdate);
        try {
            Response<List<FlatType>> response = call.execute();
            if (response.isSuccessful()) {
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(response.body());
                realm.commitTransaction();
                ReferenceUpdate.saveReferenceData(rName, updateDate, realm);
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
        String rName = HouseType.class.getSimpleName();
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);
        Call<List<HouseType>> call = SManApiFactory.getHouseTypeService().getData(lastUpdate);
        try {
            Response<List<HouseType>> response = call.execute();
            if (response.isSuccessful()) {
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(response.body());
                realm.commitTransaction();
                ReferenceUpdate.saveReferenceData(rName, updateDate, realm);
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
