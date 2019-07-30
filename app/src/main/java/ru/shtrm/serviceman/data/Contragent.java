package ru.shtrm.serviceman.data;

import java.util.Date;
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import retrofit2.Call;
import retrofit2.Response;
import ru.shtrm.serviceman.retrofit.SManApiFactory;

public class Contragent extends RealmObject implements IBaseRecord {

    @PrimaryKey
    private long _id;
    private String uuid;
    private Organization organization;
    private String gisId;
    private String title;
    private String address;
    private String phone;
    private String inn;
    private String account;
    private String director;
    private String email;
    private ContragentType contragentType;
    private Date createdAt;
    private Date changedAt;

    public static List<Contragent> getData() {
        String lastUpdate = ReferenceUpdate.lastChangedAsStr(ReferenceUpdate.makeReferenceName(Contragent.class));
        Call<List<Contragent>> call = SManApiFactory.getContragentService().getData(lastUpdate);
        try {
            Response<List<Contragent>> response = call.execute();
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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGisId() {
        return gisId;
    }

    public void setGisId(String gisId) {
        this.gisId = gisId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ContragentType getContragentType() {
        return contragentType;
    }

    public void setContragentType(ContragentType contragentType) {
        this.contragentType = contragentType;
    }

}
