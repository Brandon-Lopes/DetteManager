package com.oc.rss.dettemanager;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Brandon and Maxime on 28/11/2016.
 */

public class SettingsActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}
