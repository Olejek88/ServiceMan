package ru.shtrm.serviceman.data;

import java.util.Date;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import ru.shtrm.serviceman.retrofit.SManApiFactory;

public class ZhObject extends RealmObject implements IBaseRecord {

    @PrimaryKey
    private long _id;
    private String uuid;
    private Organization organization;
    private String title;
    private String gisId;
    private double square;
    private ZhObjectStatus objectStatus;
    private House house;
    private ZhObjectType objectType;
    private Date createdAt;
    private Date changedAt;

    public ZhObject() {
        uuid = UUID.randomUUID().toString().toUpperCase();
        Date createDate = new Date();
        createdAt = createDate;
        changedAt = createDate;
    }

    public static long getLastId() {
        Realm realm = Realm.getDefaultInstance();

        Number lastId = realm.where(ZhObject.class).max("_id");
        if (lastId == null) {
            lastId = 0;
        }

        realm.close();
        return lastId.longValue();
    }

    public static List<ZhObject> getData() {
        String lastUpdate = ReferenceUpdate.lastChangedAsStr(ReferenceUpdate.makeReferenceName(ZhObject.class));
        Call<List<ZhObject>> call = SManApiFactory.getObjectService().getData(lastUpdate);
        try {
            Response<List<ZhObject>> response = call.execute();
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

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public String getTitle() {
        return title;
    }

    public String getFullTitle() {
        return this.getHouse().getFullTitle().concat(" ").concat(this.title);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGisId() {
        return gisId;
    }

    public void setGisId(String gisId) {
        this.gisId = gisId;
    }

    public double getSquare() {
        return square;
    }

    public void setSquare(double square) {
        this.square = square;
    }

    public ZhObjectStatus getObjectStatus() {
        return objectStatus;
    }

    public void setObjectStatus(ZhObjectStatus objectStatus) {
        this.objectStatus = objectStatus;
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public ZhObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(ZhObjectType objectType) {
        this.objectType = objectType;
    }
}
