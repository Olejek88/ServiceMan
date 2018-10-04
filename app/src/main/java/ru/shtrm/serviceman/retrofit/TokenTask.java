package ru.shtrm.serviceman.retrofit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Response;
import ru.shtrm.serviceman.data.AuthorizedUser;
import ru.shtrm.serviceman.data.Token;

public class TokenTask extends AsyncTask<Void, Void, Token> {

    private WeakReference<Context> context;
    private int rc;
    private String userUuid;
    private String pinHash;

    public TokenTask(@NonNull Context c, String uuid, String hash) {
        context = new WeakReference<>(c);
        userUuid = uuid;
        pinHash = hash;
    }

    @Override
    protected Token doInBackground(Void... strings) {
        Context c = context.get();
        if (c == null) {
            return null;
        }

        // пытаемся получить ранее выданный токен
        SharedPreferences sp = c.getSharedPreferences(userUuid, Context.MODE_PRIVATE);
        String localToken = sp.getString("token", null);
        Token token;
        if (localToken == null) {
            token = getToken(userUuid, pinHash);
            return token;
        } else {
            AuthorizedUser.getInstance().setToken(localToken);
        }

        boolean isPingOk = false;
        Call<Void> callPing = SManApiFactory.getPingService().ping();
        try {
            isPingOk = callPing.execute().isSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isPingOk) {
            token = new Token();
            token.setToken(localToken);
            return token;
        }

        token = getToken(userUuid, pinHash);

        return token;
    }

    private Token getToken(String userUuid, String pinHash) {
        Token token = null;
        Call<Token> call = SManApiFactory.getTokenService().getToken(userUuid, pinHash);
        try {
            Response<Token> response = call.execute();
            rc = response.code();
            if (response.isSuccessful()) {
                token = response.body();
                AuthorizedUser aUser = AuthorizedUser.getInstance();
                aUser.setToken(token.getToken());
                aUser.setValidToken(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return token;
    }

    @SuppressLint("ApplySharedPref")
    @Override
    protected void onPostExecute(Token token) {
        super.onPostExecute(token);
        if (token != null) {
            Context c = context.get();
            if (c != null) {
                SharedPreferences sp = c.getSharedPreferences(userUuid, Context.MODE_PRIVATE);
                sp.edit().putString("token", token.getToken()).commit();
                Intent result = new Intent(Token.TOKEN_INTENT);
                result.putExtra("token", token.getToken());
                result.putExtra("result", rc);
                c.sendBroadcast(result);
            }
        }
    }
}
