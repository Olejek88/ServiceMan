package ru.shtrm.serviceman.data;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import ru.shtrm.serviceman.retrofit.SManApiFactory;

public class TaskVerdict extends RealmObject {

    @PrimaryKey
    private long _id;
    private String uuid;
    private String title;
    private Date createdAt;
    private Date changedAt;

    public static List<TaskVerdict> getData() {
        String lastUpdate = ReferenceUpdate.lastChangedAsStr(ReferenceUpdate.makeReferenceName(TaskVerdict.class));
        Call<List<TaskVerdict>> call = SManApiFactory.getTaskVerdictService().getData(lastUpdate);
        try {
            Response<List<TaskVerdict>> response = call.execute();
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

    public class Verdict {
        public static final String NOT_DEFINED = "0916D468-A631-4FC9-898C-04B7C9415284";
        public static final String INSPECTED = "DFD29CF9-A817-41CD-B78C-3AC44C8A4747";
    }
}
