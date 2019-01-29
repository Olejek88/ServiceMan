package ru.shtrm.serviceman.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.ReferenceUpdate;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.db.LoadTestData;
import ru.shtrm.serviceman.retrofit.Api;
import ru.shtrm.serviceman.retrofit.UsersTask;
import ru.shtrm.serviceman.rfid.RfidDriverBase;

public class SettingsFragment extends PreferenceFragmentCompat {
    private Activity mainActivityConnector;
    private static final String TAG = "Settings";
    private PreferenceScreen basicSettingScr, driverSettingScr;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_prefs);

        Preference button = this.findPreference(getString(R.string.load_test_data));
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                LoadTestData.LoadAllTestData();
                LoadTestData.LoadAllTestData2();
                LoadTestData.LoadAllTestData3();
                LoadTestData.LoadAllTestData4();
                return true;
            }
        });

        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(mainActivityConnector.getApplicationContext());
        // получаем список драйверов
        String[] driverClassList = RfidDriverBase.getDriverClassList();
        // строим список драйверов с именами и классами
        List<String> drvNames = new ArrayList<>();
        List<String> drvKeys = new ArrayList<>();
        String name;

        for (String classPath : driverClassList) {
            name = RfidDriverBase.getDriverName(classPath);
            if (name != null) {
                drvNames.add(name);
                drvKeys.add(classPath);
            }
        }

        // элемент интерфейса со списком драйверов считывателей
        ListPreference drvList = (ListPreference) this.findPreference(getResources().getString(
                R.string.rfidDriverListPrefKey));
        driverSettingScr = (PreferenceScreen) this.findPreference(getResources()
                .getString(R.string.rfidDrvSettingKey));

        // указываем названия и значения для элементов списка
        drvList.setEntries(drvNames.toArray(new String[]{""}));
        drvList.setEntryValues(drvKeys.toArray(new String[]{""}));

        // при изменении драйвера, включаем дополнительный экран с настройками драйвера
        drvList.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                //String value = (String) newValue;
                //showRfidDriverScreen(value);
                return true;
            }
        });

        // проверяем есть ли настройки у драйвера
/*
        String currentDrv = preferences.getString(
                getResources().getString(R.string.rfidDriverListPrefKey), null);
        showRfidDriverScreen(currentDrv);
*/

        Preference button2 = this.findPreference(getString(R.string.delete_test_data));
        button2.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                LoadTestData.DeleteSomeData();
                return true;
            }
        });

        this.findPreference(getString(R.string.api_url)).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Api.API_URL = String.valueOf(newValue);
                Context context = getContext();
                if (context != null) {
                    SharedPreferences sp = context
                            .getSharedPreferences(User.SERVICE_USER_UUID, Context.MODE_PRIVATE);
                    String token = sp.getString("token", null);
                    if (token != null) {
                        String lastUpdateDate = ReferenceUpdate.lastChangedAsStr(User.class.getSimpleName());
                        UsersTask task = new UsersTask(context, token, lastUpdateDate);
                        task.execute();
                    }
                }

                return true;
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivityConnector = getActivity();
        if (mainActivityConnector == null)
            onDestroyView();
    }
    
    private boolean isDriverSettingsScreen(String classPath, PreferenceScreen rootScreen) {

        Class<?> driverClass;
        PreferenceScreen screen;

        try {
            // пытаемся получить класс драйвера
            driverClass = Class.forName(classPath);

            // пытаемся создать объект драйвера
            Constructor<?> c = driverClass.getConstructor();
            RfidDriverBase driver = (RfidDriverBase) c.newInstance();

            // передаём драйверу "чистый" экран
            rootScreen.removeAll();

            // пытаемся вызвать метод
/*
            screen = driver.getSettingsScreen(rootScreen);
            if (screen == null) {
                return false;
            }
*/
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
            return false;
        }

        return true;
    }

    void showRfidDriverScreen(String value) {
        // проверяем есть ли настройки у драйвера
        if (value != null && isDriverSettingsScreen(value, driverSettingScr)) {
            basicSettingScr.addPreference(driverSettingScr);
        } else {
            basicSettingScr.removePreference(driverSettingScr);
        }
    }
}
