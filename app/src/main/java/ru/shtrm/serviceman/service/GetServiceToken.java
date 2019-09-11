package ru.shtrm.serviceman.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Response;
import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.AuthorizedUser;
import ru.shtrm.serviceman.data.ReferenceUpdate;
import ru.shtrm.serviceman.data.Token;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.retrofit.SManApiFactory;
import ru.shtrm.serviceman.retrofit.ServiceApiFactory;

public class GetServiceToken implements Runnable {
    private Context context;

    public GetServiceToken(Context c) {
        context = c;
    }

    private String getToken(String uuid, String hash) {
        String token = null;
        Call<Token> call = SManApiFactory.getTokenService()
                .getToken(uuid, hash);
        try {
            Response<Token> response = call.execute();
            if (response.isSuccessful()) {
                token = response.body().getToken();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return token;
    }

    private void getUsers() {
        if (!AuthorizedUser.getInstance().isValidToken()) {
            String lastUpdateDate = ReferenceUpdate.lastChangedAsStr(User.class.getSimpleName());
            Date updateDate = new Date();
            Call<List<User>> res = ServiceApiFactory.getUsersService().getData(lastUpdateDate);
            try {
                Response<List<User>> response = res.execute();
                if (response.isSuccessful()) {
                    List<User> users = response.body();
                    Realm realm = Realm.getDefaultInstance();
                    if (users.size() > 0) {
                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(users);
                        realm.commitTransaction();
                        realm.close();
                        ReferenceUpdate.saveReferenceData(User.class.getSimpleName(), updateDate);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        SharedPreferences sp = context.getSharedPreferences(User.SERVICE_USER_UUID, Context.MODE_PRIVATE);
        String token = sp.getString("token", null);
        SharedPreferences spd = PreferenceManager.getDefaultSharedPreferences(context);
        String name;
        name = context.getString(R.string.api_oid_key);
        String oid = spd.getString(name, null);
        if (oid == null || oid.equals("0")) {
            return;
        }

        name = context.getString(R.string.api_organization_secret_key);
        String secret = spd.getString(name, null);
        ServiceApiFactory.setOid(oid);
        ServiceApiFactory.setSecret(secret);

        String pinHash;
        Realm realm = Realm.getDefaultInstance();
        User sUser = realm.where(User.class)
                .equalTo("uuid", User.SERVICE_USER_UUID).findFirst();
        if (sUser != null) {
            pinHash = sUser.getPin();
            realm.close();
        } else {
            realm.close();
            return;
        }

        if (token == null) {
            token = getToken(User.SERVICE_USER_UUID, pinHash);
            if (token != null) {
                sp.edit().putString("token", token).commit();
            }
        }

        if (token != null) {
            boolean isPingOk = false;
            ServiceApiFactory.setToken(token);
            Call<Void> call = ServiceApiFactory.getPingService().ping();
            try {
                isPingOk = call.execute().isSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (isPingOk) {
                getUsers();
            } else {
                token = getToken(User.SERVICE_USER_UUID, pinHash);
                if (token != null) {
                    sp.edit().putString("token", token).commit();
                    ServiceApiFactory.setToken(token);
                    getUsers();
                }
            }
        }
    }
}
