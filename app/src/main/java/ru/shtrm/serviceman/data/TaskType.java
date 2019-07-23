package ru.shtrm.serviceman.data;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import ru.shtrm.serviceman.retrofit.SManApiFactory;

public class TaskType extends RealmObject {

    @PrimaryKey
    private long _id;
    private String uuid;
    private String title;
    private Date createdAt;
    private Date changedAt;

    public static List<TaskType> getData() {
        String lastUpdate = ReferenceUpdate.lastChangedAsStr(ReferenceUpdate.makeReferenceName(TaskType.class));
        Call<List<TaskType>> call = SManApiFactory.getTaskTypeService().getData(lastUpdate);
        try {
            Response<List<TaskType>> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(Date changedAt) {
        this.changedAt = changedAt;
    }

    public class Type {
        public static final String TASK_TYPE_CURRENT_REPAIR = "E39AE9CC-E98B-485C-930D-62315076DBBE";
        public static final String TASK_TYPE_PLAN_REPAIR = "E624A870-2064-40CF-80A1-ADD414EBA4FB";
        public static final String TASK_TYPE_CURRENT_CHECK = "13FC82D2-AE8C-42AF-A406-33E5036C33E1";
        public static final String TASK_TYPE_NOT_PLANNED_CHECK = "B66FB299-C099-496F-95D2-66BAEB73A8D2";
        public static final String TASK_TYPE_SEASON_CHECK = "407A4C28-B1A4-485A-8F5C-4FA9C9CBE336";
        public static final String TASK_TYPE_CONTROL = "444CBA6D-2082-4DD7-9CC2-6EB5EBF1C5E0";
        public static final String TASK_TYPE_PLAN_TO = "3EE2B734-B957-4191-82A7-60119C2C8556";
        public static final String TASK_TYPE_NOT_PLAN_TO = "053D2E52-BD3D-4549-9207-E96D4C053E89";
        public static final String TASK_TYPE_REPAIR = "2E1FC274-D9D6-4C74-BADE-A68080016F88";
        public static final String TASK_TYPE_MEASURE = "47202738-6A35-447D-87CC-9274FED4CCAA";
        public static final String TASK_TYPE_POVERKA = "87DB7F49-A98F-4CD3-B670-F005D89920AE";
        public static final String TASK_TYPE_INSTALL = "831D61CC-147F-4AE3-AE55-C99901C6AA4C";

        public static final String TASK_TYPE_VIEW = "7E2DB5D5-13CB-4BEB-A3F7-4B3A090922BB";
        public static final String TASK_TYPE_REPLACE = "F5856C79-BAA5-4833-B641-A4EF9223FD67";
        public static final String TASK_TYPE_UNINSTALL = "0A7AB474-357B-4BFD-923D-7E7BE17D1B74";

        public static final String TASK_TYPE_TO = "3EE2B734-B957-4191-82A7-60119C2C8556";
        public static final String TASK_TYPE_OVERHAUL = "6345EA99-CD30-41B5-98E9-43D4E8E3FF96";
    }
}
