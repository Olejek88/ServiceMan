package ru.shtrm.serviceman.retrofit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Response;
import ru.shtrm.serviceman.data.ReferenceUpdate;
import ru.shtrm.serviceman.data.Token;
import ru.shtrm.serviceman.data.User;

public class UsersTask extends AsyncTask<String, Void, List<User>> {

    private WeakReference<Context> context;
    private String token;

    private class Reason {
        static final int UNK_ERROR = -1;
        static final int OK = 0;
        static final int NOT_GET_TOKEN = 1;
        static final int NO_NETWORK = 2;
        static final int UNAUTHORIZED = 3;
    }

    private int rc = Reason.OK;

    public UsersTask(@NonNull Context c) {
        context = new WeakReference<>(c);
        SharedPreferences sp = context.get().getSharedPreferences(User.SERVICE_USER_UUID, Context.MODE_PRIVATE);
        token = sp.getString("token", null);
    }

    @Override
    protected List<User> doInBackground(String... strings) {
        List<User> users;

        // видимо первый запуск или обнулили данные приложения
        if (token == null) {
            String t = getToken(strings[0], strings[1]);
            if (t == null) {
                this.rc = Reason.NOT_GET_TOKEN;
                return null;
            } else {
                token = t;
            }
        }

        // проверяем токен
        if (!checkToken(token)) {
            return null;
        }

        ServiceApiFactory.setToken(token);

        String rName = User.class.getSimpleName();
        Date date = ReferenceUpdate.lastChanged(rName);
        Date updateDate = new Date();
        users = getUsersList(date);
        ReferenceUpdate.saveReferenceData(rName, updateDate);

        return users;
    }

    @SuppressLint("ApplySharedPref")
    @Override
    protected void onPostExecute(final List<User> users) {
        super.onPostExecute(users);
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(users);
            }
        });
        realm.close();

        if (rc == Reason.OK) {
            SharedPreferences sp = context.get().getSharedPreferences(User.SERVICE_USER_UUID, Context.MODE_PRIVATE);
            sp.edit().putString("token", this.token).commit();
        } else {
            switch (rc) {
                case Reason.UNAUTHORIZED:
                    Toast.makeText(context.get(), "Неверный пин код!", Toast.LENGTH_LONG).show();
                    break;
                case Reason.NO_NETWORK:
                    Toast.makeText(context.get(), "Нет сети!", Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(context.get(), "Неизвестная ошибка!", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    private boolean checkToken(String token) {
        ServiceApiFactory.setToken(token);
        getUsersList(new Date());
        return this.rc == Reason.OK;
    }

    private List<User> getUsersList(Date date) {
        List<User> users = null;
        String dateParam = new SimpleDateFormat("yyyy-MM-dd H:m:s", Locale.US).format(date);
        Call<List<User>> res = ServiceApiFactory.getUsersService().getData(dateParam);
        this.rc = Reason.OK;
        try {
            Response<List<User>> response = res.execute();
            if (response.isSuccessful()) {
                users = response.body();
            } else {
                switch (response.code()) {
                    case 401:
                        rc = Reason.UNAUTHORIZED;
                        break;
                    default:
                        rc = Reason.UNK_ERROR;
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            rc = Reason.NO_NETWORK;
        }

        return users;
    }

    private String getToken(String uuid, String pinHash) {
        Token token = null;
        Call<Token> call = SManApiFactory.getTokenService().getToken(uuid, pinHash);
        try {
            Response<Token> response = call.execute();
            if (response.isSuccessful()) {
                token = response.body();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (token != null) {
            return token.getToken();
        } else {
            return null;
        }
    }
}
