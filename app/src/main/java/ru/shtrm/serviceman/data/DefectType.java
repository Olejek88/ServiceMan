package ru.shtrm.serviceman.data;

import java.util.Date;
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import retrofit2.Call;
import retrofit2.Response;
import ru.shtrm.serviceman.retrofit.SManApiFactory;

public class DefectType extends RealmObject {

    @PrimaryKey
    private long _id;
    private String uuid;
    private String title;
    private Date createdAt;
    private Date changedAt;

    public static List<DefectType> getData() {
        String lastUpdate = ReferenceUpdate.lastChangedAsStr(ReferenceUpdate.makeReferenceName(DefectType.class));
        Call<List<DefectType>> call = SManApiFactory.getDefectTypeService().getData(lastUpdate);
        try {
            Response<List<DefectType>> response = call.execute();
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
        public static final String DEFECT_DEFAULT = "A0DB029F-BD49-4CE8-B25B-DA8066114931";
    }
}
