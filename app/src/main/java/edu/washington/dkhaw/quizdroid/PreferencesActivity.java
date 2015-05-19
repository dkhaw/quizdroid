package edu.washington.dkhaw.quizdroid;

import android.preference.PreferenceActivity;
import android.os.Bundle;


public class PreferencesActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
