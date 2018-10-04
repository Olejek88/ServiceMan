package ru.shtrm.serviceman.ui;

import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import ru.shtrm.serviceman.R;

public class AboutFragment extends PreferenceFragmentCompat {

    private Preference prefVersion;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.about_prefs);
        initPrefs();
        prefVersion.setSummary("1.0.2");
    }

    /**
     * Init the preferences.
     */
    private void initPrefs() {
        prefVersion = findPreference("version");
    }
}
