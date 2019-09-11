package ru.shtrm.serviceman.retrofit;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

import ru.shtrm.serviceman.service.GetServiceToken;

public class UsersTask extends AsyncTask<Void, Void, Void> {

    public static final String USERS_TASK_INTENT = "ru.shtrm.serviceman.retrofit.USERS_TASK_INTENT";
    private WeakReference<Context> context;
    private int rc = -1;

    public UsersTask(@NonNull Context c) {
        context = new WeakReference<>(c);
    }

    @Override
    protected Void doInBackground(Void... strings) {
        // на самом деле здесь запрашивается токен и список пользователей (нужно разделить)
        GetServiceToken getServiceToken = new GetServiceToken(context.get());
        getServiceToken.run();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Context c = context.get();
        if (c != null) {
            Intent result = new Intent(USERS_TASK_INTENT);
            result.putExtra("result", rc);
            c.sendBroadcast(result);
        }
    }
}
