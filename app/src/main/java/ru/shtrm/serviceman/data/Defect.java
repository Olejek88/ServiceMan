package ru.shtrm.serviceman.data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import retrofit2.Call;
import retrofit2.Response;
import ru.shtrm.serviceman.retrofit.SManApiFactory;

public class Defect extends RealmObject implements ISend, IBaseRecord {
    @Index
    private long _id;
    @PrimaryKey
    private String uuid;
    private Organization organization;
    private User user;
    private String title;
    private Date date;
    private Task task;
    private Equipment equipment;
    private DefectType defectType;
    private int status;
    private Date createdAt;
    private Date changedAt;
    private boolean sent;

    public Defect() {
        uuid = UUID.randomUUID().toString().toUpperCase();
        sent = false;
        Date createDate = new Date();
        date = createDate;
        createdAt = createDate;
        changedAt = createDate;
        user = AuthorizedUser.getInstance().getUser();
    }

    public static long getLastId() {
        Realm realm = Realm.getDefaultInstance();

        Number lastId = realm.where(Defect.class).max("_id");
        if (lastId == null) {
            lastId = 0;
        }

        realm.close();
        return lastId.longValue();
    }

    public static List<Defect> getData() {
        String lastUpdate = ReferenceUpdate.lastChangedAsStr(ReferenceUpdate.makeReferenceName(Defect.class));
        Call<List<Defect>> call = SManApiFactory.getDefectService().getData(lastUpdate);
        try {
            Response<List<Defect>> response = call.execute();
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

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public DefectType getDefectType() {
        return defectType;
    }

    public void setDefectType(DefectType defectType) {
        this.defectType = defectType;
    }

    public int getDefectStatus() {
        return status;
    }

    public void setDefectStatus(int status) {
        this.status = status;
    }
}
