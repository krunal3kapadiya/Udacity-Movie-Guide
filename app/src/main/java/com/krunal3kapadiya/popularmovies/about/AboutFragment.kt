package com.krunal3kapadiya.popularmovies.about

import android.os.Bundle
import android.preference.PreferenceFragment
import android.support.v7.preference.PreferenceFragmentCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.krunal3kapadiya.popularmovies.R

class AboutFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(p0: Bundle?, p1: String?) {
        setPreferencesFromResource(R.xml.about, p1)
    }
}
