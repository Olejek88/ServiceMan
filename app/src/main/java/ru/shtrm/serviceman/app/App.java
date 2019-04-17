package ru.shtrm.serviceman.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatDelegate;

import io.realm.Realm;
import ru.shtrm.serviceman.R;
//import ru.shtrm.serviceman.data.AuthorizedUser;
import ru.shtrm.serviceman.db.AppRealm;
import ru.shtrm.serviceman.retrofit.Api;
import ru.shtrm.serviceman.rfid.driver.RfidDriverNull;
import ru.shtrm.serviceman.rfid.driver.RfidDriverQRcode;
import ru.shtrm.serviceman.util.SettingsUtil;

public class App extends Application {
    public static double defaultLatitude = 56.06;
    public static double defaultLongitude = 59.58;

    public static boolean isInternetOn(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo niMobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (niMobile != null) {
                if (niMobile.getState() == NetworkInfo.State.CONNECTED ||
                        niMobile.getState() == NetworkInfo.State.CONNECTING) {
                    return true;
                }
            }

            NetworkInfo niWiFi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (niWiFi != null) {
                return niWiFi.getState() == NetworkInfo.State.CONNECTED ||
                        niWiFi.getState() == NetworkInfo.State.CONNECTING;
            }
        }

        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);

        if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).
                getBoolean(SettingsUtil.KEY_NIGHT_MODE, false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Api.API_URL = preferences.getString(getString(R.string.api_url), null);
        if (Api.API_URL == null) {
            Api.API_URL = getString(R.string.api_url_default);
            preferences.edit().putString(getString(R.string.api_url), Api.API_URL).apply();
        }

        String rfidDriver = preferences.getString(getString(R.string.default_rfid_driver_key), null);
        if (rfidDriver == null) {
            preferences.edit().putString(getString(R.string.default_rfid_driver_key), RfidDriverQRcode.class.getCanonicalName()).apply();
        }

        String uhfDriver = preferences.getString(getString(R.string.default_uhf_driver_key), null);
        if (uhfDriver == null) {
            preferences.edit().putString(getString(R.string.default_uhf_driver_key), RfidDriverNull.class.getCanonicalName()).apply();
        }

        // инициализируем синглтон с данными о активном пользователе на уровне приложения
        // AuthorizedUser authorizedUser = AuthorizedUser.getInstance();

        // инициализируем базу данных Realm
        AppRealm.init(this);
    }
}
