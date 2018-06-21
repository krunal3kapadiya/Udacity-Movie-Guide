package com.krunal3kapadiya.popularmovies.dashBoard.movies

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.Toast
import com.krunal3kapadiya.popularmovies.Constants
import com.krunal3kapadiya.popularmovies.R
import com.krunal3kapadiya.popularmovies.data.api.MovieApiClient
import com.krunal3kapadiya.popularmovies.data.api.MovieApiInterface
import com.krunal3kapadiya.popularmovies.data.model.MovieResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesViewModel : ViewModel() {

    fun getMovieListPop() {
        when {
            Constants.isNetworkAvailable(context!!) -> getPopularMovieList()
            else -> displayNetworkDisableError(activity_main_relative_layout, View.OnClickListener { getMovieListPop() })
        }
    }

    fun displayNetworkDisableError(view: View?, listener: View.OnClickListener) {
        val snackbar = Snackbar.make(view!!, R.string.internet_not_available, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry, listener)
        snackbar.show()
    }


    fun getPopularMovieList() {
        setLoading(true)
        val movieClient = MovieApiClient.client!!
                .create(MovieApiInterface::class.java)
        val getPopMovie = movieClient.getPopularMoviesList(Constants.API_KEY)
        getPopMovie.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                mMoviesArrayList!!.clear()
                response.body()!!.results?.let { mMoviesArrayList!!.addAll(it) }
                mAdapter!!.notifyDataSetChanged()

                setLoading(false)
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()

                setLoading(false)
            }
        })
    }

    fun setLoading(b: Boolean) {
        rv_list_movie_main!!.visibility = if (b) View.GONE else View.VISIBLE
        pb_main!!.visibility = if (b) View.VISIBLE else View.GONE
    }
}