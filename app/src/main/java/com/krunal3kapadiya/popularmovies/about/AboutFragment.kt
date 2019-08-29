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

//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
//                              savedInstanceState: Bundle?): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_about, container, false)
//    }

//    companion object {
//        @JvmStatic
//        fun newInstance() = AboutFragment()
//    }
}
