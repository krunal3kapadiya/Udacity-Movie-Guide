package com.krunal3kapadiya.popularmovies.dashBoard.movies

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SnapHelper
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.bumptech.glide.Glide
import com.krunal3kapadiya.popularmovies.BuildConfig
import com.krunal3kapadiya.popularmovies.Constants
import com.krunal3kapadiya.popularmovies.Injection
import com.krunal3kapadiya.popularmovies.R
import com.krunal3kapadiya.popularmovies.dashBoard.ViewImageActivity
import com.krunal3kapadiya.popularmovies.dashBoard.actors.ActorsDetailActivity
import com.krunal3kapadiya.popularmovies.data.adapter.ActorsListAdapter
import com.krunal3kapadiya.popularmovies.data.adapter.ReviewRVAdapter
import com.krunal3kapadiya.popularmovies.data.adapter.TrailerRVAdapter
import com.krunal3kapadiya.popularmovies.data.api.MovieApi
import com.krunal3kapadiya.popularmovies.data.api.MovieApiClient
import com.krunal3kapadiya.popularmovies.data.model.Cast
import com.krunal3kapadiya.popularmovies.data.model.Movies
import com.krunal3kapadiya.popularmovies.data.model.Reviews
import com.krunal3kapadiya.popularmovies.data.model.Trailer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_movie_detail.*
import java.util.regex.Pattern

class MovieDetailActivity(private var isFavorite: Boolean = false) : AppCompatActivity(), TrailerRVAdapter.OnItemClick, ReviewRVAdapter.OnReviewItemClick {

    private var mContext: Context? = null
    private var movieId: Long = 0

    private var mTrailerList: ArrayList<Trailer>? = null
    private var mReviewList: ArrayList<Reviews>? = null
    private var mTrailerAdapter: TrailerRVAdapter? = null
    private var mReviewAdapter: ReviewRVAdapter? = null
    private var themeLightColor: Int = 0
    private var themeDarkColor: Int = 0

    companion object {
        var ARG_MOVIE_ID = "movie_id"
        var ARG_MOVIE_TITLE = "movie_title"
        var ARG_DARK_COLOR = "dark_color"
        var ARG_LIGHT_COLOR = "light_color"

        fun launch(context: Context,
                   movieId: Int,
                   movieTitle: String,
                   themeDarkColor: Int,
                   themeLightColor: Int) {
            val intent = Intent(context, MovieDetailActivity::class.java)
            intent.putExtra(ARG_MOVIE_ID, movieId)
            intent.putExtra(ARG_MOVIE_TITLE, movieTitle)
            intent.putExtra(ARG_DARK_COLOR, themeDarkColor)
            intent.putExtra(ARG_LIGHT_COLOR, themeLightColor)
            context.startActivity(intent)
        }
    }

    lateinit var poster_url: String
    lateinit var name: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        themeLightColor = intent.getIntExtra(ARG_LIGHT_COLOR, 0)
        themeDarkColor = intent.getIntExtra(ARG_DARK_COLOR, 0)
        mContext = this@MovieDetailActivity

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = themeDarkColor
        }

        setContentView(R.layout.activity_movie_detail)
        setSupportActionBar(toolbar)
        name = intent.getStringExtra(ARG_MOVIE_TITLE)
        supportActionBar!!.title = name
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val viewModelFactory = Injection.provideMoviesViewModel(this)
        val moviesViewModel = ViewModelProviders.of(this, viewModelFactory).get(MoviesViewModel::class.java)
        val movieId = intent.getIntExtra(ARG_MOVIE_ID, 0)

        moviesViewModel.getMovieDetail(movieId).observe(this, Observer {
            arb_movie_ratings!!.rating = java.lang.Float.valueOf((it?.vote_average!!.toDouble() / 2.0).toString())!!
            txt_movie_overview!!.text = it.overview

            val pattern = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})")
            val dateMatcher = pattern.matcher(it.release_date)
            if (dateMatcher.find()) {
                txt_movie_release!!.text = dateMatcher.group(1)
            }

            Glide.with(this)
                    .load(Constants.BASE_IMAGE_URL + Constants.POSTER_SIZE_500 + it.poster_path)
                    .into(img_movie_poster)

            Glide.with(this)
                    .load(Constants.BASE_IMAGE_URL + Constants.POSTER_SIZE_500 + it.backdrop_path)
                    .placeholder(ContextCompat.getDrawable(this, R.mipmap.ic_movie))
                    .into(movie_detail_image)
            poster_url = it.poster_path
            val backdropUrl = it.backdrop_path
            img_movie_poster.setOnClickListener { ViewImageActivity.launch(this, poster_url) }
            movie_detail_image.setOnClickListener { ViewImageActivity.launch(this, backdropUrl) }
        })

        mReviewList = ArrayList()
        mReviewAdapter = ReviewRVAdapter(this, mReviewList!!, themeLightColor)
        mTrailerList = ArrayList()
        mTrailerAdapter = TrailerRVAdapter(this, mTrailerList!!)
        rv_movie_trailer!!.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_movie_trailer!!.adapter = mTrailerAdapter
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(rv_movie_trailer!!)

        rv_movie_trailer!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {})
        rv_movie_reviews!!.layoutManager = LinearLayoutManager(this)
        rv_movie_reviews!!.adapter = mReviewAdapter
        getReviewList()
        getTrailerList()
        moviesViewModel.getCast(movieId)
        casts_view.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        snapHelper.attachToRecyclerView(casts_view)
        val actorsAdapter = ActorsListAdapter(object : ActorsListAdapter.OnItemClick {
            override fun onItemClick(pos: Int, cast: Cast) {
                ActorsDetailActivity.launch(this@MovieDetailActivity, cast.id)
            }
        })
        val cast: ArrayList<Cast> = ArrayList()
        casts_view.adapter = actorsAdapter
        moviesViewModel.mCastArrayList.observe(this, android.arch.lifecycle.Observer {
            actorsAdapter.setData(it)
            cast.addAll(it!!)
        })

        favorite_button!!.setOnClickListener {
            if (isFavorite) {
                favorite_button!!.setImageResource(R.mipmap.ic_favorite_white)
                isFavorite = false
                moviesViewModel.removeMovies(movieId)
            } else {
                favorite_button!!.setImageResource(R.mipmap.ic_favorite_white_selected)
                isFavorite = true
                moviesViewModel.addToFavourite(Movies(movieId, name, poster_url))
            }
        }

        moviesViewModel.getMoviesById(movieId).observe(this,
                Observer {
                    it?.let {
                        isFavorite = true
                        favorite_button!!.setImageResource(R.mipmap.ic_favorite_white_selected)
                    }
                })

        if (isFavorite)
            favorite_button!!.setImageResource(R.mipmap.ic_favorite_white_selected)
        else
            favorite_button!!.setImageResource(R.mipmap.ic_favorite_white)


        ctl_movie_detail!!.setContentScrimColor(themeLightColor)
        favorite_button!!.backgroundTintList = ColorStateList.valueOf(themeLightColor)

        moviesViewModel.errorMessage.observe(this, Observer {
            Toast.makeText(this@MovieDetailActivity, it, Toast.LENGTH_LONG).show()
        })
    }

    private fun getTrailerList() {
        val movieApiInterface = MovieApiClient.client!!
                .create(MovieApi::class.java)
        val getTrailers = movieApiInterface.getMovieTrailers(
                movieId,
                BuildConfig.TMDB_API_KEY
        )
        getTrailers.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mTrailerList!!.clear()
                    (mTrailerList as ArrayList).addAll(it.trailerArrayList!!)
                    mTrailerAdapter!!.notifyDataSetChanged()

                    card_movie_trailer!!.visibility = View.VISIBLE
                }, {})
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun getReviewList() {
        val movieApiInterface = MovieApiClient.client!!
                .create(MovieApi::class.java)
        val getReview = movieApiInterface.getMovieReviews(
                movieId,
                BuildConfig.TMDB_API_KEY
        )
        getReview.observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    it!!.reviewsArrayList.let { (mReviewList as ArrayList).addAll(it!!) }
                    mReviewAdapter!!.notifyDataSetChanged()

                    card_movie_review!!.visibility = View.VISIBLE
                }, {

                })
    }

    override fun onItemClick(position: Int) {
        val dialog = AlertDialog.Builder(this)
                .setMessage("Are you sure want to open other app?")
                .setPositiveButton("Yes") { dialogInterface, i ->
                    val intent = Intent(
                            Intent.ACTION_VIEW, Uri.parse(getString(R.string.youtube_video, mTrailerList!![position].key)))
                    startActivity(intent)
                }
                .setNegativeButton("No") { dialogInterface, i ->
                    // Do Nothing
                }
                .create()
        dialog.show()
    }

    override fun onReviewItemClick(position: Int) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mReviewList!![position].reviewURL))
        startActivity(intent)
    }

}
