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
import ru.shtrm.serviceman.data.TaskTemplate;
import ru.shtrm.serviceman.data.TaskType;
import ru.shtrm.serviceman.data.TaskVerdict;
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

            if (!updateAlarmStatus(realm)) {
                Journal.add("AlarmStatus not updated.");
            }

            if (!updateAlarmType(realm)) {
                Journal.add("AlarmType not updated.");
            }

            if (!updateDefectType(realm)) {
                Journal.add("DefectType not updated.");
            }

            if (!updateDocumentationType(realm)) {
                Journal.add("DocumentationType not updated.");
            }

            if (!updateEquipmentStatus(realm)) {
                Journal.add("EquipmentStatus not updated.");
            }

            if (!updateHouseStatus(realm)) {
                Journal.add("HouseStatus not updated.");
            }

            if (!updateMeasureType(realm)) {
                Journal.add("MeasureType not updated.");
            }

            if (!updateObjectStatus(realm)) {
                Journal.add("ObjectStatus not updated.");
            }

            if (!updateObjectType(realm)) {
                Journal.add("ObjectType not updated.");
            }

            if (!updateTaskType(realm)) {
                Journal.add("TaskType not updated.");
            }

            if (!updateTaskVerdict(realm)) {
                Journal.add("TaskVerdict not updated.");
            }

            if (!updateWorkStatus(realm)) {
                Journal.add("WorkStatus not updated.");
            }

            if (!updateEquipmentSystem(realm)) {
                Journal.add("EquipmentSystem not updated.");
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

            // --------
            if (!updateCity(realm)) {
                Journal.add("City not updated.");
            }

            if (!updateStreet(realm)) {
                Journal.add("Street not updated.");
            }

            if (!updateHouseType(realm)) {
                Journal.add("HouseType not updated.");
            }

            if (!updateHouse(realm)) {
                Journal.add("House not updated.");
            }

            if (!updateObject(realm)) {
                Journal.add("Object not updated.");
            }

            if (!updateEquipmentType(realm)) {
                Journal.add("EquipmentType not updated.");
            }

            if (!updateEquipment(realm)) {
                Journal.add("Equipment not updated.");
            }

            if (!updateDefect(realm)) {
                Journal.add("Defect not updated.");
            }

            if (!updateDocumentation(realm)) {
                Journal.add("Documentation not updated.");
            }

            if (!updateMessage(realm)) {
                Journal.add("Message not updated.");
            }

            if (!updateOperationTemplate(realm)) {
                Journal.add("OperationTemplate not updated.");
            }

            if (!updateTaskTemplate(realm)) {
                Journal.add("TaskTemplate not updated.");
            }

//            if (!updateUserHouse(realm)) {
//                Journal.add("UserHouse not updated.");
//            }

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

    private boolean updateObject(Realm realm) {
        String lastUpdate;
        String rName = ZhObject.class.getSimpleName() + ":" + userUuid;
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);
        Call<List<ZhObject>> call = SManApiFactory.getObjectService().getData(lastUpdate);
        try {
            Response<List<ZhObject>> response = call.execute();
            if (response.isSuccessful()) {
                List<ZhObject> list = response.body();
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

//    private boolean updateUserHouse(Realm realm) {
//        String lastUpdate;
//        String rName = UserHouse.class.getSimpleName() + ":" + userUuid;
//        Date updateDate = new Date();
//        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);
//        Call<List<UserHouse>> call = SManApiFactory.getUserHouseService().getData(lastUpdate);
//        try {
//            Response<List<UserHouse>> response = call.execute();
//            if (response.isSuccessful()) {
//                List<UserHouse> list = response.body();
//                if (list.size() > 0) {
//                    realm.beginTransaction();
//                    realm.copyToRealmOrUpdate(list);
//                    realm.commitTransaction();
//                    ReferenceUpdate.saveReferenceData(rName, updateDate, realm);
//                }
//
//                return true;
//            } else {
//                return false;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    private boolean updateDefectType(Realm realm) {
        String lastUpdate;
        String rName = DefectType.class.getSimpleName() + ":" + userUuid;
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);
        Call<List<DefectType>> call = SManApiFactory.getDefectTypeService().getData(lastUpdate);
        try {
            Response<List<DefectType>> response = call.execute();
            if (response.isSuccessful()) {
                List<DefectType> list = response.body();
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

    private boolean updateDocumentationType(Realm realm) {
        String lastUpdate;
        String rName = DocumentationType.class.getSimpleName() + ":" + userUuid;
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);
        Call<List<DocumentationType>> call = SManApiFactory.getDocumentationTypeService().getData(lastUpdate);
        try {
            Response<List<DocumentationType>> response = call.execute();
            if (response.isSuccessful()) {
                List<DocumentationType> list = response.body();
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

    private boolean updateMeasureType(Realm realm) {
        String lastUpdate;
        String rName = MeasureType.class.getSimpleName() + ":" + userUuid;
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);
        Call<List<MeasureType>> call = SManApiFactory.getMeasureTypeService().getData(lastUpdate);
        try {
            Response<List<MeasureType>> response = call.execute();
            if (response.isSuccessful()) {
                List<MeasureType> list = response.body();
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

    private boolean updateObjectStatus(Realm realm) {
        String lastUpdate;
        String rName = ZhObjectStatus.class.getSimpleName() + ":" + userUuid;
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);
        Call<List<ZhObjectStatus>> call = SManApiFactory.getZhObjectStatusService().getData(lastUpdate);
        try {
            Response<List<ZhObjectStatus>> response = call.execute();
            if (response.isSuccessful()) {
                List<ZhObjectStatus> list = response.body();
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

    private boolean updateObjectType(Realm realm) {
        String lastUpdate;
        String rName = ZhObjectType.class.getSimpleName() + ":" + userUuid;
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);
        Call<List<ZhObjectType>> call = SManApiFactory.getZhObjectTypeService().getData(lastUpdate);
        try {
            Response<List<ZhObjectType>> response = call.execute();
            if (response.isSuccessful()) {
                List<ZhObjectType> list = response.body();
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

    private boolean updateTaskType(Realm realm) {
        String lastUpdate;
        String rName = TaskType.class.getSimpleName() + ":" + userUuid;
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);
        Call<List<TaskType>> call = SManApiFactory.getTaskTypeService().getData(lastUpdate);
        try {
            Response<List<TaskType>> response = call.execute();
            if (response.isSuccessful()) {
                List<TaskType> list = response.body();
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

    private boolean updateTaskVerdict(Realm realm) {
        String lastUpdate;
        String rName = TaskVerdict.class.getSimpleName() + ":" + userUuid;
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);
        Call<List<TaskVerdict>> call = SManApiFactory.getTaskVerdictService().getData(lastUpdate);
        try {
            Response<List<TaskVerdict>> response = call.execute();
            if (response.isSuccessful()) {
                List<TaskVerdict> list = response.body();
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

    private boolean updateWorkStatus(Realm realm) {
        String lastUpdate;
        String rName = WorkStatus.class.getSimpleName() + ":" + userUuid;
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);
        Call<List<WorkStatus>> call = SManApiFactory.getWorkStatusService().getData(lastUpdate);
        try {
            Response<List<WorkStatus>> response = call.execute();
            if (response.isSuccessful()) {
                List<WorkStatus> list = response.body();
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

    private boolean updateEquipmentSystem(Realm realm) {
        String lastUpdate;
        String rName = EquipmentSystem.class.getSimpleName() + ":" + userUuid;
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);
        Call<List<EquipmentSystem>> call = SManApiFactory.getEquipmentSystemService().getData(lastUpdate);
        try {
            Response<List<EquipmentSystem>> response = call.execute();
            if (response.isSuccessful()) {
                List<EquipmentSystem> list = response.body();
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

    private boolean updateDefect(Realm realm) {
        String lastUpdate;
        String rName = Defect.class.getSimpleName() + ":" + userUuid;
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);

        Call<List<Defect>> call = SManApiFactory.getDefectService().getData(lastUpdate);
        try {
            Response<List<Defect>> response = call.execute();
            if (response.isSuccessful()) {
                List<Defect> list = response.body();
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

    private boolean updateDocumentation(Realm realm) {
        String lastUpdate;
        String rName = Documentation.class.getSimpleName() + ":" + userUuid;
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);

        Call<List<Documentation>> call = SManApiFactory.getDocumentationService().getData(lastUpdate);
        try {
            Response<List<Documentation>> response = call.execute();
            if (response.isSuccessful()) {
                List<Documentation> list = response.body();
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

    private boolean updateMessage(Realm realm) {
        String lastUpdate;
        String rName = Message.class.getSimpleName() + ":" + userUuid;
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);
        Call<List<Message>> call = SManApiFactory.getMessageService().getData(lastUpdate);
        try {
            Response<List<Message>> response = call.execute();
            if (response.isSuccessful()) {
                List<Message> list = response.body();
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

    private boolean updateOperationTemplate(Realm realm) {
        String lastUpdate;
        String rName = OperationTemplate.class.getSimpleName() + ":" + userUuid;
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);
        Call<List<OperationTemplate>> call = SManApiFactory.getOperationTemplateService().getData(lastUpdate);
        try {
            Response<List<OperationTemplate>> response = call.execute();
            if (response.isSuccessful()) {
                List<OperationTemplate> list = response.body();
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

    private boolean updateTaskTemplate(Realm realm) {
        String lastUpdate;
        String rName = TaskTemplate.class.getSimpleName() + ":" + userUuid;
        Date updateDate = new Date();
        lastUpdate = ReferenceUpdate.lastChangedAsStr(rName);
        Call<List<TaskTemplate>> call = SManApiFactory.getTaskTemplateService().getData(lastUpdate);
        try {
            Response<List<TaskTemplate>> response = call.execute();
            if (response.isSuccessful()) {
                List<TaskTemplate> list = response.body();
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
