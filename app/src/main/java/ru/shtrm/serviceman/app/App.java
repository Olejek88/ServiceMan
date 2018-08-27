package ru.shtrm.serviceman.app;

import android.app.Application;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatDelegate;

import io.realm.Realm;
import ru.shtrm.serviceman.data.AuthorizedUser;
import ru.shtrm.serviceman.db.AppRealm;
import ru.shtrm.serviceman.util.SettingsUtil;

public class App extends Application {

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

        /*PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).
                getString(SettingsUtil.SERVER_URL, "http://shtrm.ru/api")*/

        // инициализируем синглтон с данными о активном пользователе на уровне приложения
        AuthorizedUser authorizedUser = AuthorizedUser.getInstance();

        // инициализируем базу данных Realm
        AppRealm.init(this);
    }

}
