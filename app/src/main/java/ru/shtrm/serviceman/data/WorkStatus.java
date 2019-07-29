package ru.shtrm.serviceman.data;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import ru.shtrm.serviceman.retrofit.SManApiFactory;

public class WorkStatus extends RealmObject {
    @Index
    private long _id;
    @PrimaryKey
    private String uuid;
    private String title;
    private Date createdAt;
    private Date changedAt;

    public static List<WorkStatus> getData() {
        String lastUpdate = ReferenceUpdate.lastChangedAsStr(ReferenceUpdate.makeReferenceName(WorkStatus.class));
        Call<List<WorkStatus>> call = SManApiFactory.getWorkStatusService().getData(lastUpdate);
        try {
            Response<List<WorkStatus>> response = call.execute();
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
        public static final String NEW = "1E9B4D73-044C-471B-A08D-26F32EBB22B0";
        public static final String IN_WORK = "31179027-8416-47E4-832F-2A94D7804A4F";
        public static final String COMPLETE = "F1576F3E-ACB6-4EEB-B8AF-E34E4D345CE9";
        public static final String UN_COMPLETE = "EFDE80D2-D00E-413B-B430-0A011056C6EA";
        public static final String CANCELED = "C2FA4A7B-0D7C-4407-A449-78FA70A11D47";
    }
}
