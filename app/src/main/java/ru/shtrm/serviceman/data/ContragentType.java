package ru.shtrm.serviceman.data;

import java.util.Date;
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import retrofit2.Call;
import retrofit2.Response;
import ru.shtrm.serviceman.retrofit.SManApiFactory;

public class ContragentType extends RealmObject {

    @PrimaryKey
    private long _id;
    private String uuid;
    private String title;
    private Date createdAt;
    private Date changedAt;

    public static List<ContragentType> getData() {
        String lastUpdate = ReferenceUpdate.lastChangedAsStr(ReferenceUpdate.makeReferenceName(ContragentType.class));
        Call<List<ContragentType>> call = SManApiFactory.getContragentTypeService().getData(lastUpdate);
        try {
            Response<List<ContragentType>> response = call.execute();
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
        // исполнитель - рабочий ЖЭК
        public static final String WORKER = "D9B906FB-0559-4DD3-A632-BAEE215FA3A7";
        // оператор - сотрудник
        public static final String EMPLOYEE = "57BF4D1C-2345-49CC-8BAC-7CA9D9EA2283";
        // подрядчик
        public static final String CONTRACTOR = "4E39F32F-6E15-4015-BCB8-6E6ED54890B3";
        // организация
        public static final String ORGANIZATION = "340B3291-3F97-4B28-8DC4-A8AD74C52F07";
    }
}
