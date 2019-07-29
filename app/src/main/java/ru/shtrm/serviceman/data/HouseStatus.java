package ru.shtrm.serviceman.data;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import ru.shtrm.serviceman.retrofit.SManApiFactory;

public class HouseStatus extends RealmObject {

    @PrimaryKey
    private long _id;
    private String uuid;
    private String title;
    private Date createdAt;
    private Date changedAt;

    public static List<HouseStatus> getData() {
        String lastUpdate = ReferenceUpdate.lastChangedAsStr(ReferenceUpdate.makeReferenceName(HouseStatus.class));
        Call<List<HouseStatus>> call = SManApiFactory.getHouseStatusService().getData(lastUpdate);
        try {
            Response<List<HouseStatus>> response = call.execute();
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

    public void set_id(long _id) { this._id = _id; }

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

    public class Status {
        public static final String HOUSE_STATUS_OK = "9236E1FF-D967-4080-9F42-59B03ADD25E8";
        public static final String HOUSE_STATUS_NO_ENTRANCE = "559FBFE0-9543-4965-AC84-8919237EC317";
        public static final String HOUSE_STATUS_ABSENT = "9B6C8A1D-498E-40EE-B973-AA9ACC6322A0";
        public static final String HOUSE_STATUS_DEFAULT = "9127B1A3-D0C1-4F96-8026-B597600FC9CD";
    }
}
