package ru.shtrm.serviceman.retrofit;

import android.os.AsyncTask;
import java.util.List;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Response;
import ru.shtrm.serviceman.data.AlarmStatus;
import ru.shtrm.serviceman.data.AlarmType;
import ru.shtrm.serviceman.data.City;
import ru.shtrm.serviceman.data.Equipment;
import ru.shtrm.serviceman.data.EquipmentStatus;
import ru.shtrm.serviceman.data.EquipmentType;
import ru.shtrm.serviceman.data.Flat;
import ru.shtrm.serviceman.data.FlatStatus;
import ru.shtrm.serviceman.data.House;
import ru.shtrm.serviceman.data.HouseStatus;
import ru.shtrm.serviceman.data.Journal;
import ru.shtrm.serviceman.data.ReferenceUpdate;
import ru.shtrm.serviceman.data.Resident;
import ru.shtrm.serviceman.data.Street;
import ru.shtrm.serviceman.data.Subject;
import ru.shtrm.serviceman.data.User;

public class ReferenceTask extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... voids) {
        Realm realm = Realm.getDefaultInstance();

        if (!updateAlarmStatus(realm)) {
            Journal.add("AlarmStatus not updated.");
        }

        if (!updateAlarmType(realm)) {
            Journal.add("AlarmStatus not updated.");
        }

        if (!updateCity(realm)) {
            Journal.add("City not updated.");
        }

        if (!updateStreet(realm)) {
            Journal.add("Street not updated.");
        }

        if (!updateHouseStatus(realm)) {
            Journal.add("HouseStatus not updated.");
        }

        if (!updateHouse(realm)) {
            Journal.add("House not updated.");
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
        return null;
    }

    private boolean updateAlarmStatus(Realm realm) {
        String lastUpdate;
        lastUpdate = ReferenceUpdate.lastChangedAsStr("AlarmStatus");
        Call<List<AlarmStatus>> call = SManApiFactory.getAlarmStatusService().getData(lastUpdate);
        try {
            Response<List<AlarmStatus>> response = call.execute();
            if (response.isSuccessful()) {
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(response.body());
                realm.commitTransaction();
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
        lastUpdate = ReferenceUpdate.lastChangedAsStr("AlarmType");
        Call<List<AlarmType>> call = SManApiFactory.getAlarmTypeService().getData(lastUpdate);
        try {
            Response<List<AlarmType>> response = call.execute();
            if (response.isSuccessful()) {
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(response.body());
                realm.commitTransaction();
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
        lastUpdate = ReferenceUpdate.lastChangedAsStr("City");
        Call<List<City>> call = SManApiFactory.getCityService().getData(lastUpdate);
        try {
            Response<List<City>> response = call.execute();
            if (response.isSuccessful()) {
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(response.body());
                realm.commitTransaction();
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
        lastUpdate = ReferenceUpdate.lastChangedAsStr("Street");
        Call<List<Street>> call = SManApiFactory.getStreetService().getData(lastUpdate);
        try {
            Response<List<Street>> response = call.execute();
            if (response.isSuccessful()) {
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(response.body());
                realm.commitTransaction();
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
        lastUpdate = ReferenceUpdate.lastChangedAsStr("HouseStatus");
        Call<List<HouseStatus>> call = SManApiFactory.getHouseStatusService().getData(lastUpdate);
        try {
            Response<List<HouseStatus>> response = call.execute();
            if (response.isSuccessful()) {
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(response.body());
                realm.commitTransaction();
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
        lastUpdate = ReferenceUpdate.lastChangedAsStr("House");
        Call<List<House>> call = SManApiFactory.getHouseService().getData(lastUpdate);
        try {
            Response<List<House>> response = call.execute();
            if (response.isSuccessful()) {
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(response.body());
                realm.commitTransaction();
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
        lastUpdate = ReferenceUpdate.lastChangedAsStr(FlatStatus.class.getSimpleName());
        Call<List<FlatStatus>> call = SManApiFactory.getFlatStatusService().getData(lastUpdate);
        try {
            Response<List<FlatStatus>> response = call.execute();
            if (response.isSuccessful()) {
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(response.body());
                realm.commitTransaction();
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
        lastUpdate = ReferenceUpdate.lastChangedAsStr(Flat.class.getSimpleName());
        Call<List<Flat>> call = SManApiFactory.getFlatService().getData(lastUpdate);
        try {
            Response<List<Flat>> response = call.execute();
            if (response.isSuccessful()) {
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(response.body());
                realm.commitTransaction();
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
        lastUpdate = ReferenceUpdate.lastChangedAsStr(EquipmentStatus.class.getSimpleName());
        Call<List<EquipmentStatus>> call = SManApiFactory.getEquipmentStatusService().getData(lastUpdate);
        try {
            Response<List<EquipmentStatus>> response = call.execute();
            if (response.isSuccessful()) {
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(response.body());
                realm.commitTransaction();
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
        lastUpdate = ReferenceUpdate.lastChangedAsStr(EquipmentType.class.getSimpleName());
        Call<List<EquipmentType>> call = SManApiFactory.getEquipmentTypeService().getData(lastUpdate);
        try {
            Response<List<EquipmentType>> response = call.execute();
            if (response.isSuccessful()) {
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(response.body());
                realm.commitTransaction();
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
        lastUpdate = ReferenceUpdate.lastChangedAsStr(Equipment.class.getSimpleName());
        Call<List<Equipment>> call = SManApiFactory.getEquipmentService().getData(lastUpdate);
        try {
            Response<List<Equipment>> response = call.execute();
            if (response.isSuccessful()) {
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(response.body());
                realm.commitTransaction();
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
        lastUpdate = ReferenceUpdate.lastChangedAsStr(Subject.class.getSimpleName());
        Call<List<Subject>> call = SManApiFactory.getSubjectService().getData(lastUpdate);
        try {
            Response<List<Subject>> response = call.execute();
            if (response.isSuccessful()) {
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(response.body());
                realm.commitTransaction();
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
        lastUpdate = ReferenceUpdate.lastChangedAsStr(Resident.class.getSimpleName());
        Call<List<Resident>> call = SManApiFactory.getResidentService().getData(lastUpdate);
        try {
            Response<List<Resident>> response = call.execute();
            if (response.isSuccessful()) {
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(response.body());
                realm.commitTransaction();
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
        lastUpdate = ReferenceUpdate.lastChangedAsStr(User.class.getSimpleName());
        Call<List<User>> call = SManApiFactory.getUsersService().getData(lastUpdate);
        try {
            Response<List<User>> response = call.execute();
            if (response.isSuccessful()) {
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(response.body());
                realm.commitTransaction();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
