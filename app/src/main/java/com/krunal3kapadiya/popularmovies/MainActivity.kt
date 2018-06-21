package com.krunal3kapadiya.popularmovies

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.database.Cursor
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import butterknife.ButterKnife
import com.facebook.stetho.Stetho
import com.krunal3kapadiya.popularmovies.dashBoard.DashBoardActivity
import com.krunal3kapadiya.popularmovies.data.MovieContract
import com.krunal3kapadiya.popularmovies.data.adapter.MovieRVAdapter
import com.krunal3kapadiya.popularmovies.data.api.MovieApiClient
import com.krunal3kapadiya.popularmovies.data.api.MovieApiInterface
import com.krunal3kapadiya.popularmovies.data.model.MovieResponse
import com.krunal3kapadiya.popularmovies.data.model.Movies
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity(), MovieRVAdapter.OnItemClick, SearchView.OnQueryTextListener {
    private var mContext: Context? = null
    private var mMoviesArrayList: ArrayList<Movies>? = null
    private var mAdapter: MovieRVAdapter? = null


    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DashBoardActivity.launch(this)

        ButterKnife.bind(this)
        mContext = this@MainActivity

        mMoviesArrayList = ArrayList()

        var SPAN = 2

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            SPAN = 4
        }

        val layoutManager = GridLayoutManager(this, SPAN)
        rv_list_movie_main!!.layoutManager = layoutManager
        mAdapter = MovieRVAdapter(this, mMoviesArrayList!!)
        rv_list_movie_main!!.adapter = mAdapter

        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build())

        getMovieListPop()
    }


    fun getMovieListPop() {
        if (Constants.isNetworkAvailable(mContext!!)) {
            getPopularMovieList()
        } else {
            displayNetworkDisableError(activity_main_relative_layout, View.OnClickListener { getMovieListPop() })
        }
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
                Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show()

                setLoading(false)
            }
        })
    }

    fun getTopRatedMovieList() {
        setLoading(true)
        val movieClient = MovieApiClient.client!!
                .create(MovieApiInterface::class.java)
        val getTopRatedMovie = movieClient.getTopRatedMovies(Constants.API_KEY)
        getTopRatedMovie.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                mMoviesArrayList!!.clear()
                response.body()!!.results?.let { mMoviesArrayList!!.addAll(it) }
                mAdapter!!.notifyDataSetChanged()

                setLoading(false)
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                setLoading(false)
                Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun displayNetworkDisableError(view: View?, listener: View.OnClickListener) {
        val snackbar = Snackbar.make(view!!, R.string.internet_not_available, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry, listener)
        snackbar.show()
    }

    fun setLoading(b: Boolean) {
        rv_list_movie_main!!.visibility = if (b) View.GONE else View.VISIBLE
        pb_main!!.visibility = if (b) View.VISIBLE else View.GONE
    }

    override fun onItemClick(pos: Int, view: ImageView?) {
        val intent = Intent(this, MovieDetailActivity::class.java)
        intent.putExtra(MovieDetailActivity.ARG_MOVIE, mMoviesArrayList!![pos])

        val options = view?.let {
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this,
                    it,
                    ViewCompat.getTransitionName(view))
        }


        startActivity(intent, options?.toBundle())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchMenuItem = menu.findItem(R.id.action_search)
        searchView = searchMenuItem.actionView as SearchView
        searchView!!.setOnQueryTextListener(this)
        searchView!!.queryHint = "Search"
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sort_pop -> {
                getMovieListPop()
                item.isChecked = !item.isChecked
                return true
            }
            R.id.action_sort_rate -> {
                getMovieListTop()
                item.isChecked = !item.isChecked
                return true
            }
            R.id.action_favorite -> {
                mMoviesArrayList!!.clear()
                // getContentResolver().notifyChange();
                contentResolver.query(MovieContract.MovieEntry.CONTENT_URI,
                        arrayOf(MovieContract.MovieEntry._ID), null, null, null)
                // initialize loader
                supportLoaderManager.initLoader(CURSOR_LOADER_ID, null, object : LoaderManager.LoaderCallbacks<Cursor> {
                    override fun onCreateLoader(id: Int, args: Bundle): Loader<Cursor> {
                        return CursorLoader(mContext!!, MovieContract.MovieEntry.CONTENT_URI, null, null, null, null)
                    }

                    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
                        var movies: Movies
                        while (data.moveToNext()) {
                            movies = Movies(
                                    data.getInt(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID)),
                                    data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE)),
                                    data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER)),
                                    data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_BACKDROP)),
                                    data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW)),
                                    data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATINGS)),
                                    data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE))
                            )
                            mMoviesArrayList!!.add(movies)
                        }
                        mAdapter!!.notifyDataSetChanged()
                    }

                    override fun onLoaderReset(loader: Loader<Cursor>) {

                    }
                })


                item.isChecked = !item.isChecked
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun getMovieListTop() {
        if (Constants.isNetworkAvailable(mContext!!)) {
            getTopRatedMovieList()
        } else {
            displayNetworkDisableError(activity_main_relative_layout, View.OnClickListener { getMovieListTop() })
        }
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        return false
    }

    companion object {
        private val CURSOR_LOADER_ID = 0
    }
}
