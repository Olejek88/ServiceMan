package ru.shtrm.serviceman.data;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import ru.shtrm.serviceman.retrofit.SManApiFactory;

public class User extends RealmObject {
    public static final String SERVICE_USER_UUID = "00000000-9BF0-4542-B127-F4ECEFCE49DA";
    public static final String SERVICE_USER_PIN = "PIN:qwerfvgtbsasljflasjflajsljdsa";
    public static final String SERVICE_USER_NAME = "sUser";

    @PrimaryKey
    private long _id;
    private String uuid;
    private String name;
    private String pin;
    private String image;
    private String contact;
    private Organization organization;
    private int type;

    public static long getLastId() {
        Realm realm = Realm.getDefaultInstance();

        Number lastId = realm.where(User.class).max("_id");
        if (lastId == null) {
            lastId = 0;
        }

        realm.close();
        return lastId.longValue();
    }

    public static List<User> getData() {
        String lastUpdate = ReferenceUpdate.lastChangedAsStr(ReferenceUpdate.makeReferenceName(User.class));
        Call<List<User>> call = SManApiFactory.getUsersService().getData(lastUpdate);
        try {
            Response<List<User>> response = call.execute();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public class Type {
        public static final int ARM = 1;
        public static final int WORKER = 2;
    }
}
