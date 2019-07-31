package ru.shtrm.serviceman.data;

import java.util.Date;
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import retrofit2.Call;
import retrofit2.Response;
import ru.shtrm.serviceman.retrofit.SManApiFactory;

public class RequestStatus extends RealmObject {
    @PrimaryKey
    private long _id;
    private String uuid;
    private String title;
    private Date createdAt;
    private Date changedAt;

    public static List<RequestStatus> getData() {
        String lastUpdate = ReferenceUpdate.lastChangedAsStr(ReferenceUpdate.makeReferenceName(RequestStatus.class));
        Call<List<RequestStatus>> call = SManApiFactory.getRequestStatusService().getData(lastUpdate);
        try {
            Response<List<RequestStatus>> response = call.execute();
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

    public class Status {
        public static final String NEW_REQUEST = "F45775D3-9876-4831-9781-92E00240D44F";
        public static final String IN_WORK = "49085FF9-5223-404A-B98D-7B042BB571A3";
        public static final String COMPLETE = "FB7E8A7C-E228-4226-AAF5-AD3DB472F4ED";
        public static final String UN_COMPLETE = "B17CB2E0-58DF-4CA3-B620-AF8B39D6C229";
        public static final String CANCELED = "8DA302D8-978B-4900-872C-4EB4DE13682A";
    }
}
