package com.krunal3kapadiya.popularmovies.dashBoard

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

internal class TabFragmentAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    private val fragmentList = ArrayList<Fragment>()
    private val fragmentTitleList = ArrayList<String>()

    /**
     * return fragment item
     */
    override fun getItem(position: Int): Fragment? {
        return fragmentList[position]
    }

    /**
     * return total number of list
     */
    override fun getCount(): Int {
        return fragmentList.size
    }

    /**
     * adding fragment
     */
    fun addFragment(newInstance: Fragment, s: String) {
        fragmentList.add(newInstance)
        fragmentTitleList.add(s)
    }

    /**
     * setting fragment title
     */
    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitleList[position]
    }
}