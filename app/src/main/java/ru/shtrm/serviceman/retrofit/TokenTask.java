package ru.shtrm.serviceman.retrofit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Response;
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
        Token token = null;
        Call<Token> call = SManApiFactory.getTokenService().getToken(userUuid, pinHash);
        try {
            Response<Token> response = call.execute();
            rc = response.code();
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
            Context c = context.get();
            if (c != null) {
                Intent result = new Intent(Token.TOKEN_INTENT);
                result.putExtra("token", token.getToken());
                result.putExtra("result", rc);
                c.sendBroadcast(result);
            }
        }
    }
}
