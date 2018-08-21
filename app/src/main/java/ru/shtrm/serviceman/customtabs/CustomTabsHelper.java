package ru.shtrm.serviceman.customtabs;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import ru.shtrm.serviceman.R;
import ru.shtrm.serviceman.util.SettingsUtil;

public class CustomTabsHelper {

    public static void openUrl(Context context, String url) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        if (sharedPreferences.getBoolean(SettingsUtil.KEY_CUSTOM_TABS, true)) {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            builder.build().launchUrl(context, Uri.parse(url));
        } else {
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, R.string.error_no_browser, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
