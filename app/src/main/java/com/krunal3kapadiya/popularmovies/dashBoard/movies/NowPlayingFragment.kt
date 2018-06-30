package com.krunal3kapadiya.popularmovies.dashBoard.movies

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.Snackbar
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
import kotlinx.android.synthetic.main.fragment_movies.*
import kotlinx.android.synthetic.main.fragment_now_playing.*
import java.util.*

class NowPlayingFragment : Fragment(), MovieRVAdapter.OnItemClick {
    override fun onItemClick(pos: Int, view: ImageView?) {
        val intent = Intent(context, MovieDetailActivity::class.java)
        intent.putExtra(MovieDetailActivity.ARG_MOVIE, mMoviesArrayList!![pos])

//        val options = view?.let {
//            ActivityOptionsCompat.makeSceneTransitionAnimation(
//                    activity!!,
//                    it,
//                    ViewCompat.getTransitionName(view))
//        }
//

        startActivity(intent)
    }

    companion object {
        fun newInstance(number: Int): NowPlayingFragment {
            val fragment = NowPlayingFragment()
            val bundle = Bundle()
            bundle.putInt("Number", number)
            fragment.arguments = bundle
            return fragment
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
        mAdapter = MovieRVAdapter(context!!, mMoviesArrayList!!, this)
        rv_list_movie_main!!.adapter = mAdapter
        val moviesViewModel = ViewModelProviders.of(this).get(MoviesViewModel::class.java)
        val number = arguments?.getInt("Number")
        moviesViewModel.getPopularMovieList(number)
        moviesViewModel.errorMessage.observe(this, android.arch.lifecycle.Observer {
            displayNetworkDisableError(moviesTab, View.OnClickListener { moviesViewModel.getPopularMovieList(number) })
        })

        moviesViewModel.movieArrayList.observe(this, android.arch.lifecycle.Observer {
            mAdapter!!.setData(it)
        })
    }

    fun displayNetworkDisableError(view: View?, listener: View.OnClickListener) {
        val snackbar = Snackbar.make(view!!, R.string.internet_not_available, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry, listener)
        snackbar.show()
    }
}