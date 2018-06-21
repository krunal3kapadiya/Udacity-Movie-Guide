package com.krunal3kapadiya.popularmovies.dashBoard.movies

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.krunal3kapadiya.popularmovies.R
import com.krunal3kapadiya.popularmovies.dashBoard.NowPlayingFragment
import com.krunal3kapadiya.popularmovies.dashBoard.TabFragmentAdapter
import kotlinx.android.synthetic.main.fragment_movies.*

class MoviesFragment : Fragment() {

    companion object {
        fun newInstance(): MoviesFragment {
            return MoviesFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TabFragmentAdapter(activity!!.supportFragmentManager)

        adapter.addFragment(NowPlayingFragment.newInstance(), "Now Playing")
        adapter.addFragment(NowPlayingFragment.newInstance(), "Popular")
        adapter.addFragment(NowPlayingFragment.newInstance(), "Upcoming")
        adapter.addFragment(NowPlayingFragment.newInstance(), "Top Rated")
        adapter.addFragment(NowPlayingFragment.newInstance(), "Movies by the year")

        moviesViewPager.adapter = adapter

        moviesTab.setupWithViewPager(moviesViewPager)
    }
}