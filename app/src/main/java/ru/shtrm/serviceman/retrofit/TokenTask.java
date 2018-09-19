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
import ru.shtrm.serviceman.data.Token;

public class TokenTask extends AsyncTask<String, Void, Token> {

    private WeakReference<Context> context;

    public TokenTask(@NonNull Context context) {
        this.context = new WeakReference<>(context);
    }

    @Override
    protected Token doInBackground(String... strings) {
        Token token = null;
        Call<Token> call = SManApiFactory.getTokenService().getToken(strings[0], strings[1]);
        try {
            Response<Token> response = call.execute();
            // TODO: реализовать проверку на неверные реквизиты или протухший токен
            if (response.isSuccessful()) {
                token = response.body();
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
            SharedPreferences sp = context.get().getSharedPreferences(token.getUsersUuid(), Context.MODE_PRIVATE);
            sp.edit().putString("token", token.getToken()).commit();
            Intent result = new Intent(Token.TOKEN_INTENT);
            result.putExtra("token", token.getToken());
            context.get().sendBroadcast(result);
        }
    }
}
