package com.krunal3kapadiya.popularmovies.dashBoard.movies

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.krunal3kapadiya.popularmovies.*
import com.krunal3kapadiya.popularmovies.data.OnItemClick
import com.krunal3kapadiya.popularmovies.data.adapter.MovieRVAdapter
import com.krunal3kapadiya.popularmovies.data.model.Movies
import com.krunal3kapadiya.popularmovies.view.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.fragment_now_playing.*

class NowPlayingFragment : Fragment(), OnItemClick {
    override fun onItemClick(pos: Int,
                             view: ImageView?,
                             title: String,
                             id: Int,
                             themeDarkColor: Int,
                             themeLightColor: Int) {
        context?.let {
            MovieDetailActivity.launch(
                    context = it,
                    movieId = id,
                    movieTitle = title,
                    themeDarkColor = themeDarkColor,
                    themeLightColor = themeLightColor)
        }
    }

//    override fun onItemClick(pos: Int, view: ImageView?, movies: Movies) {

//
//        val options = view?.let {
//            ViewCompat.getTransitionName(view)?.let { it1 ->
//                ActivityOptionsCompat.makeSceneTransitionAnimation(
//                        activity!!,
//                        it,
//                        it1)
//            }
//        }
//        val options = ActivityOptions
//                .makeSceneTransitionAnimation(activity, view, "robot")
//        startActivity(intent, options.toBundle())
//    }

    companion object {
        fun newInstance(number: Int): NowPlayingFragment {
            val fragment = NowPlayingFragment()
            val bundle = Bundle()
            bundle.putInt("Number", number)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var mAdapter: MovieRVAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_now_playing, container, false)
    }

    lateinit var moviesViewModel: MoviesViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = GridLayoutManager(context, Constants.SPAN_RECYCLER_VIEW)
        rv_list_movie_main!!.layoutManager = layoutManager
        mAdapter = MovieRVAdapter(this)
        rv_list_movie_main!!.adapter = mAdapter

        val viewModelFactory = Injection.provideMoviesViewModel(context!!)
        moviesViewModel = ViewModelProviders.of(this, viewModelFactory).get(MoviesViewModel::class.java)

        loadNextDataFromApi(mAdapter!!, 1)

        val scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                Log.d("SCROLLVIEW_LOADED", "SCROLLED ".plus(page).plus(" ").plus(totalItemsCount))
                val nextPage = page + 1
                loadNextDataFromApi(mAdapter!!, nextPage)
            }
        }
        rv_list_movie_main.addOnScrollListener(scrollListener as EndlessRecyclerViewScrollListener)
    }

    fun displayNetworkDisableError(view: View?, listener: View.OnClickListener) {
        val snackbar = Snackbar.make(view!!,
                R.string.internet_not_available,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry, listener)
        snackbar.show()
    }

    fun loadNextDataFromApi(mAdapter: MovieRVAdapter, page: Int) {
        if (Constants.isNetworkAvailable(context = context!!)) {


            val number = arguments?.getInt("Number")

            moviesViewModel.isLoading.observe(this, android.arch.lifecycle.Observer {
                if (it!!) {
                    pb_main.visibility = View.VISIBLE
                    rv_list_movie_main.visibility = View.GONE
                } else {
                    pb_main.visibility = View.GONE
                    rv_list_movie_main.visibility = View.VISIBLE
                }
            })
            moviesViewModel.getPopularMovieList(number, page)
            moviesViewModel.errorMessage.observe(this, android.arch.lifecycle.Observer {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                displayNetworkDisableError(rv_list_movie_main, View.OnClickListener {
                    moviesViewModel.getPopularMovieList(number, page)
                })
            })

            moviesViewModel.movieArrayList.observe(this, android.arch.lifecycle.Observer {
                mAdapter.setData(it)
            })
        } else {
            displayNetworkDisableError(rv_list_movie_main, View.OnClickListener {
                val in_ = Intent(Settings.ACTION_DATA_ROAMING_SETTINGS)
                startActivity(in_)
            })
        }
    }
}