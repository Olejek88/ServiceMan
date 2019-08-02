package ru.shtrm.serviceman.data;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.Date;
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import retrofit2.Call;
import retrofit2.Response;
import ru.shtrm.serviceman.retrofit.Api;
import ru.shtrm.serviceman.retrofit.SManApiFactory;

public class Documentation extends RealmObject implements IBaseRecord {
    @PrimaryKey
    private long _id;
    private String uuid;
    private Organization organization;
    private Equipment equipment;
    private EquipmentType equipmentType;
    private DocumentationType documentationType;
    private String title;
    private String path;
    private Date createdAt;
    private Date changedAt;

    public static List<Documentation> getData() {
        String lastUpdate = ReferenceUpdate.lastChangedAsStr(ReferenceUpdate.makeReferenceName(Documentation.class));
        Call<List<Documentation>> call = SManApiFactory.getDocumentationService().getData(lastUpdate);
        try {
            Response<List<Documentation>> response = call.execute();
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

    public EquipmentType getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(EquipmentType equipmentType) {
        this.equipmentType = equipmentType;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public DocumentationType getDocumentationType() {
        return documentationType;
    }

    public void setDocumentationType(DocumentationType documentationType) {
        this.documentationType = documentationType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public File getLocalPath(Context context) {
        File docFile = new File(context.getExternalFilesDir("Documents"), path);
        if (!docFile.getParentFile().exists()) {
            if (!docFile.getParentFile().mkdirs()) {
                Log.e("Documentation", "can`t create \"" + docFile.getAbsolutePath() + "\" path.");
                return null;
            }
        }

        return docFile;
    }

    public String getUrl() {
        String root = "";
        if (equipment != null) {
            root = equipment.getUuid();
        } else if (equipmentType != null) {
            root = equipmentType.getUuid();
        }

        return Api.API_URL + "/storage/" + organization.get_id() + "/files/doc/" + root + "/" + path;
    }
}
