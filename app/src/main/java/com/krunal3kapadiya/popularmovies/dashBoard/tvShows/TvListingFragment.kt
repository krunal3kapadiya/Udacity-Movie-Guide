package com.krunal3kapadiya.popularmovies.dashBoard.tvShows

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.krunal3kapadiya.popularmovies.MovieDetailActivity
import com.krunal3kapadiya.popularmovies.R
import com.krunal3kapadiya.popularmovies.data.adapter.MovieRVAdapter
import com.krunal3kapadiya.popularmovies.data.model.Movies
import kotlinx.android.synthetic.main.fragment_now_playing.*

class TvListingFragment : Fragment(), MovieRVAdapter.OnItemClick {
    override fun onItemClick(pos: Int, view: ImageView?) {
        val intent = Intent(context, MovieDetailActivity::class.java)
        intent.putExtra(MovieDetailActivity.ARG_MOVIE, mMoviesArrayList!![pos])
        startActivity(intent)
    }

    companion object {
        fun newInstance(id: Int): TvListingFragment {
            val tvListingFragment = TvListingFragment()
            val arg = Bundle()
            arg.putInt("ID", id)
            tvListingFragment.arguments = arg
            return tvListingFragment
        }
    }

    private var mMoviesArrayList: ArrayList<Movies>? = null
    private var mAdapter: MovieRVAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_now_playing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProviders.of(this).get(TvViewModel::class.java)

        mMoviesArrayList = ArrayList()

        var SPAN = 2

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            SPAN = 4
        }

        val layoutManager = GridLayoutManager(context, SPAN)
        rv_list_movie_main.layoutManager = layoutManager
        mAdapter = MovieRVAdapter(context!!, mMoviesArrayList!!, this)
        rv_list_movie_main.adapter = mAdapter
        val number = arguments?.getInt("ID")
        viewModel.getPopularTvList(number)
        viewModel.movieArrayList.observe(this, android.arch.lifecycle.Observer {
            mAdapter!!.setData(it)
        })


    }
}