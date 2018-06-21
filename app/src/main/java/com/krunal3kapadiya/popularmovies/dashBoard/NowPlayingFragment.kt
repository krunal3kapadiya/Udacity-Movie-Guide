package com.krunal3kapadiya.popularmovies.dashBoard

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.krunal3kapadiya.popularmovies.Constants
import com.krunal3kapadiya.popularmovies.MovieDetailActivity
import com.krunal3kapadiya.popularmovies.R
import com.krunal3kapadiya.popularmovies.data.adapter.MovieRVAdapter
import com.krunal3kapadiya.popularmovies.data.api.MovieApiClient
import com.krunal3kapadiya.popularmovies.data.api.MovieApiInterface
import com.krunal3kapadiya.popularmovies.data.model.MovieResponse
import com.krunal3kapadiya.popularmovies.data.model.Movies
import kotlinx.android.synthetic.main.fragment_now_playing.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class NowPlayingFragment : Fragment(), MovieRVAdapter.OnItemClick {
    override fun onItemClick(pos: Int, view: ImageView?) {
        val intent = Intent(context, MovieDetailActivity::class.java)
        intent.putExtra(MovieDetailActivity.ARG_MOVIE, mMoviesArrayList!![pos])

        val options = view?.let {
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity!!,
                    it,
                    ViewCompat.getTransitionName(view))
        }


        startActivity(intent, options?.toBundle())
    }

    companion object {
        fun newInstance(): NowPlayingFragment {
            return NowPlayingFragment()
        }
    }

    private var mMoviesArrayList: ArrayList<Movies>? = null
    private var mAdapter: MovieRVAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_now_playing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mMoviesArrayList = ArrayList()

        var SPAN = 2

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            SPAN = 4
        }

        val layoutManager = GridLayoutManager(context, SPAN)
        rv_list_movie_main!!.layoutManager = layoutManager
        mAdapter = MovieRVAdapter(context!!, mMoviesArrayList!!)
        rv_list_movie_main!!.adapter = mAdapter

        getMovieListPop()
    }


}