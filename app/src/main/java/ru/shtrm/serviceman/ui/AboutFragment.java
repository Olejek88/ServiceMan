package ru.shtrm.serviceman.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import ru.shtrm.serviceman.R;

public class AboutFragment extends PreferenceFragmentCompat {

    private Preference prefRate, prefLicenses,
                prefSourceCode, prefSendAdvices;

    private Preference prefVersion;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.about_prefs);

        initPrefs();

        prefVersion.setSummary("1");

        // Rate
        prefRate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                try {
                    Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (android.content.ActivityNotFoundException ex){
                    showError();
                }
                return true;
            }
        });

        // Licenses
        prefLicenses.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getContext(), PrefsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(PrefsActivity.EXTRA_FLAG, PrefsActivity.FLAG_LICENSES);
                startActivity(intent);
                return true;
            }
        });

    }

    /**
     * Init the preferences.
     */
    private void initPrefs() {
        prefVersion = findPreference("version");
        prefRate = findPreference("rate");
        prefLicenses = findPreference("licenses");
        prefSourceCode = findPreference("source_code");
        prefSendAdvices = findPreference("send_advices");
    }

    private void showError() {
        Toast.makeText(getContext(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
    }

}
