package ru.shtrm.serviceman.retrofit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.List;

import io.realm.Realm;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import ru.shtrm.serviceman.data.AuthorizedUser;
import ru.shtrm.serviceman.data.Token;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.data.source.UsersRepository;

public class UsersTask extends AsyncTask<String, Void, List<User>> {

    private WeakReference<Context> context;

    public UsersTask(@NonNull Context context) {
        this.context = new WeakReference<>(context);
    }

    @Override
    protected List<User> doInBackground(String... strings) {
//        Token token = null;
        List<User> users = null;
//        Call<Token> call = SManApiFactory.getTokenService().getToken(strings[0], strings[1]);
//        try {
//            Response<Token> response = call.execute();
//            if (response.isSuccessful()) {
//                token = response.body();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        String token = "qxWUrcY6VA6GMG0y5rotx95uWZM08Zfm";
        if (token != null) {
            User user = AuthorizedUser.getInstance().getUser();
            AuthorizedUser.getInstance().setToken(token);
            Call<List<User>> res = SManApiFactory.getUsersService().getUsers("0000-00-00");
            try {
//                Response<ResponseBody> response = res.execute();
                Response<List<User>> response = res.execute();
                if (response.isSuccessful()) {
                    users = response.body();
//                    ResponseBody body = response.body();
//                    String json = body.string();
//                    try {
//                        JSONArray jObj = new JSONArray(json);
//                        for (int idx = 0; idx < jObj.length(); idx++) {
//                            Object o = jObj.get(idx);
//                            User u = new User();
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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
//        if (token != null) {
//            SharedPreferences sp = context.get().getSharedPreferences(token.getUsersUuid(), Context.MODE_PRIVATE);
//            sp.edit().putString("token", token.getToken()).commit();
//            AuthorizedUser.getInstance().setToken(token.getToken());
//        }
    }
}
