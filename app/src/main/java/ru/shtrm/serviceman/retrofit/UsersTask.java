package ru.shtrm.serviceman.retrofit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Response;
import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.ReferenceUpdate;
import ru.shtrm.serviceman.data.User;

public class UsersTask extends AsyncTask<Void, Void, List<User>> {

    public static final String USERS_TASK_INTENT = "ru.shtrm.serviceman.retrofit.USERS_TASK_INTENT";
    private WeakReference<Context> context;
    private String token;
    private String oid;
    private String secret;
    private String lastUpdateDate;
    private Date startUpdateDate;
    private int rc = -1;

    public UsersTask(@NonNull Context c, @NonNull String t, String d) {
        context = new WeakReference<>(c);
        token = t;
        SharedPreferences sp = c.getSharedPreferences(User.SERVICE_USER_UUID, Context.MODE_PRIVATE);
        SharedPreferences spd = PreferenceManager.getDefaultSharedPreferences(c);
        oid = spd.getString(c.getString(R.string.api_oid_key), null);
        secret = spd.getString(c.getString(R.string.api_organization_secret_key), null);
        lastUpdateDate = d;
        startUpdateDate = new Date();
    }

    @Override
    protected List<User> doInBackground(Void... strings) {
        List<User> users = null;

        ServiceApiFactory.setToken(token);
        ServiceApiFactory.setOid(oid);
        ServiceApiFactory.setSecret(secret);

        Call<List<User>> res = ServiceApiFactory.getUsersService().getData(lastUpdateDate);
        try {
            Response<List<User>> response = res.execute();
            rc = response.code();
            if (response.isSuccessful()) {
                users = response.body();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    protected void onPostExecute(final List<User> users) {
        super.onPostExecute(users);

        if (users != null && users.size() > 0) {
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(users);
                }
            });
            ReferenceUpdate.saveReferenceData(User.class.getSimpleName(), startUpdateDate, realm);
            realm.close();
        }

        Context c = context.get();
        if (c != null) {
            Intent result = new Intent(USERS_TASK_INTENT);
            result.putExtra("result", rc);
            c.sendBroadcast(result);
        }
    }
}
