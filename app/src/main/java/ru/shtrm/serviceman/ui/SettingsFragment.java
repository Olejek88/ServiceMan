package ru.shtrm.serviceman.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import java.util.ArrayList;
import java.util.List;

import ru.shtrm.serviceman.R;
//import ru.shtrm.serviceman.db.LoadTestData;
import ru.shtrm.serviceman.mvp.MainActivity;
import ru.shtrm.serviceman.retrofit.Api;
import ru.shtrm.serviceman.retrofit.UsersTask;
import ru.shtrm.serviceman.rfid.RfidDriverBase;

public class SettingsFragment extends PreferenceFragmentCompat {
    private static final String TAG = "Settings";

    private Activity mainActivityConnector = null;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_prefs);

        mainActivityConnector = getActivity();

        Preference updateAppButton = getPreferenceManager().findPreference("updateApp");
        if (updateAppButton != null) {
            updateAppButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference arg0) {
                    if (mainActivityConnector != null) {
                        MainActivity.updateApk(mainActivityConnector);
                    }

                    return true;
                }
            });
        }

//        Preference loadTestDataBtn = this.findPreference(getString(R.string.load_test_data));
//        loadTestDataBtn.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            @Override
//            public boolean onPreferenceClick(Preference preference) {
//                LoadTestData.LoadAllTestData();
//                LoadTestData.LoadAllTestData2();
//                LoadTestData.LoadAllTestData3();
//                LoadTestData.LoadAllTestData4();
//                return true;
//            }
//        });
//
//        Preference deleteTestDataBtn = this.findPreference(getString(R.string.delete_test_data));
//        deleteTestDataBtn.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            @Override
//            public boolean onPreferenceClick(Preference preference) {
//                LoadTestData.DeleteSomeData();
//                return true;
//            }
//        });

        String[] driverClassList;
        List<String> drvNames = new ArrayList<>();
        List<String> drvKeys = new ArrayList<>();
        String name;
        ListPreference drvList;

        // получаем список драйверов
        driverClassList = RfidDriverBase.getRfidDriversClass();
        // строим список драйверов с именами и классами
        for (String classPath : driverClassList) {
            name = RfidDriverBase.getDriverName(classPath);
            if (name != null) {
                drvNames.add(name);
                drvKeys.add(classPath);
            }
        }

        // элемент интерфейса со списком драйверов считывателей для выбора rfid драйвера
        // по умолчанию, т.е. того типа, метки которого использует организация
        drvList = (ListPreference) this.findPreference(getResources().getString(
                R.string.default_rfid_driver_key));

        // указываем названия и значения для элементов списка
        drvList.setEntries(drvNames.toArray(new String[]{""}));
        drvList.setEntryValues(drvKeys.toArray(new String[]{""}));


        drvNames.clear();
        drvKeys.clear();

        // получаем список драйверов
        driverClassList = RfidDriverBase.getUhfDriversClass();
        // строим список драйверов с именами и классами
        for (String classPath : driverClassList) {
            name = RfidDriverBase.getDriverName(classPath);
            if (name != null) {
                drvNames.add(name);
                drvKeys.add(classPath);
            }
        }

        // элемент интерфейса со списком драйверов UHF считывателей, для выбора драйвера того
        // считывателя который установлен в устройстве
        drvList = (ListPreference) this.findPreference(getResources().getString(
                R.string.default_uhf_driver_key));

        // указываем названия и значения для элементов списка
        drvList.setEntries(drvNames.toArray(new String[]{""}));
        drvList.setEntryValues(drvKeys.toArray(new String[]{""}));

        this.findPreference(getString(R.string.api_url)).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Api.API_URL = String.valueOf(newValue);
                return true;
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity mainActivityConnector = getActivity();
        if (mainActivityConnector == null) {
            onDestroyView();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // после изменения настроек пытаемся получить токен и список пользователей
        Context context = getContext();
        if (context != null) {
            UsersTask task = new UsersTask(context);
            task.execute();
        }
    }
}
