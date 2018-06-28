package com.krunal3kapadiya.popularmovies.dashBoard.tvShows

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.krunal3kapadiya.popularmovies.R
import com.krunal3kapadiya.popularmovies.dashBoard.TabFragmentAdapter
import kotlinx.android.synthetic.main.fragment_movies.*

class TvShowsFragment : Fragment() {

    companion object {
        fun newInstance(): TvShowsFragment {
            return TvShowsFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TabFragmentAdapter(activity!!.supportFragmentManager)

        adapter.addFragment(TvListingFragment.newInstance(1), "Airing Today")
        adapter.addFragment(TvListingFragment.newInstance(2), "On the Air")
        adapter.addFragment(TvListingFragment.newInstance(3), "Popular")
        adapter.addFragment(TvListingFragment.newInstance(4), "Top Rated")

        moviesViewPager.adapter = adapter

        moviesTab.setupWithViewPager(moviesViewPager)
    }
}