package ru.shtrm.serviceman.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.db.LoadTestData;

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
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity mainActivityConnector = getActivity();
        // TODO решить что делать если контекст не приехал
        if (mainActivityConnector ==null)
            onDestroyView();
    }
}
