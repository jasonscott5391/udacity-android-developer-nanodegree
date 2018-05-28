package com.udacity.popularmovies.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

import com.udacity.popularmovies.R;
import com.udacity.popularmovies.repository.MovieRepository;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_popular_movies);
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        SharedPreferences sharedPreferences = preferenceScreen.getSharedPreferences();

        int count = preferenceScreen.getPreferenceCount();
        for (int i = 0; i < count; i++) {
            Preference preference = getPreferenceScreen().getPreference(i);
            String value = sharedPreferences.getString(preference.getKey(), "");
            setPreferenceSummary(preference, value);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equalsIgnoreCase(getString(R.string.pref_movie_sort_key))) {
            MovieRepository.updateMovies(getActivity(), sharedPreferences.getString(key, getString(R.string.pref_movie_sort_popular)));
        }

        Preference preference = findPreference(key);
        if (preference != null) {
            setPreferenceSummary(preference, sharedPreferences.getString(key, ""));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }


    private void setPreferenceSummary(Preference preference, Object value) {
        String stringValue = String.valueOf(value);

        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else {
            preference.setSummary(stringValue);
        }
    }
}
