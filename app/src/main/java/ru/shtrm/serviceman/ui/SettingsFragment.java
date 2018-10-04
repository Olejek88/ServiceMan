package ru.shtrm.serviceman.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.data.ReferenceUpdate;
import ru.shtrm.serviceman.data.User;
import ru.shtrm.serviceman.db.LoadTestData;
import ru.shtrm.serviceman.retrofit.Api;
import ru.shtrm.serviceman.retrofit.UsersTask;

public class SettingsFragment extends PreferenceFragmentCompat {

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
        Activity mainActivityConnector = getActivity();
        if (mainActivityConnector == null)
            onDestroyView();
    }
}
