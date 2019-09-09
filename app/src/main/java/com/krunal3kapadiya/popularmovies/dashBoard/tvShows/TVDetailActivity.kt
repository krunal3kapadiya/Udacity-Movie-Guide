package com.krunal3kapadiya.popularmovies.dashBoard.tvShows

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.krunal3kapadiya.popularmovies.BuildConfig
import com.krunal3kapadiya.popularmovies.Constants
import com.krunal3kapadiya.popularmovies.Injection
import com.krunal3kapadiya.popularmovies.R
import com.krunal3kapadiya.popularmovies.data.adapter.ReviewRVAdapter
import com.krunal3kapadiya.popularmovies.data.adapter.TrailerRVAdapter
import com.krunal3kapadiya.popularmovies.data.api.MovieApi
import com.krunal3kapadiya.popularmovies.data.api.MovieApiClient
import com.krunal3kapadiya.popularmovies.data.model.Reviews
import com.krunal3kapadiya.popularmovies.data.model.Trailer
import com.krunal3kapadiya.popularmovies.data.model.TvResult
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_movie_detail.*
import java.util.regex.Pattern

class TVDetailActivity(private var isFavorite: Boolean = false) : AppCompatActivity(), TrailerRVAdapter.OnItemClick, ReviewRVAdapter.OnReviewItemClick {

    private var mContext: Context? = null
    private var tvId: Long = 0

    private var mTrailerList: ArrayList<Trailer>? = null
    private var mReviewList: ArrayList<Reviews>? = null
    private var mTrailerAdapter: TrailerRVAdapter? = null
    private var mReviewAdapter: ReviewRVAdapter? = null
    private var themeLightColor: Int = 0
    private var themeDarkColor: Int = 0
    private var mBitmap: Bitmap? = null
    private var name = ""
    private var poster_url = ""

    companion object {
        val ARG_TV_ID = "tv_id"
        val ARG_TV_TITLE = "tv_title"
        val ARG_DARK_COLOR = "dark_color"
        val ARG_LIGHT_COLOR = "light_color"

        fun launch(
                context: Context,
                tvId: Int,
                tvTitle: String,
                themeDarkColor: Int,
                themeLightColor: Int
        ) {
            val intent = Intent(context, TVDetailActivity::class.java)
            intent.putExtra(ARG_TV_ID, tvId)
            intent.putExtra(ARG_TV_TITLE, tvTitle)
            intent.putExtra(ARG_DARK_COLOR, themeDarkColor)
            intent.putExtra(ARG_LIGHT_COLOR, themeLightColor)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        themeDarkColor = intent.getIntExtra(ARG_DARK_COLOR, 0)
        themeLightColor = intent.getIntExtra(ARG_LIGHT_COLOR, 0)

        mContext = this@TVDetailActivity
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = themeDarkColor
        }

        setContentView(R.layout.activity_movie_detail)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        name = intent.getStringExtra(ARG_TV_TITLE)
        supportActionBar!!.title = name
        val tvId = intent.getIntExtra(ARG_TV_ID, 0)
        val viewModelFactory = Injection.provideTvViewModel(this)
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(TvViewModel::class.java)
        viewModel.getTvDetail(tvId).observe(this, Observer {

            arb_movie_ratings!!.rating = java.lang.Float.valueOf((it?.vote_average!!.toDouble() / 2.0).toString())!!
            txt_movie_overview!!.text = it.overview
            val pattern = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})")
            val dateMatcher = pattern.matcher(it.first_air_date)
            if (dateMatcher.find()) {
                txt_movie_release!!.text = dateMatcher.group(1)
            }
            Glide.with(this)
                    .load(Constants.BASE_IMAGE_URL + Constants.POSTER_SIZE_500 + it.backdrop_path)
                    .into(movie_detail_image)
            poster_url = it.poster_path
            Glide.with(this)
                    .load(Constants.BASE_IMAGE_URL + Constants.POSTER_SIZE_500 + poster_url)
                    .into(img_movie_poster)

            this.tvId = it.id.toLong()
        })

        mReviewList = ArrayList()
        mReviewAdapter = ReviewRVAdapter(this, mReviewList!!, themeLightColor)
        mTrailerList = ArrayList()
        mTrailerAdapter = TrailerRVAdapter(this, mTrailerList!!)
        rv_movie_trailer!!.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_movie_trailer!!.adapter = mTrailerAdapter
        rv_movie_trailer!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {})
        rv_movie_reviews!!.layoutManager = LinearLayoutManager(this)
        rv_movie_reviews!!.adapter = mReviewAdapter

        img_movie_poster!!.setImageBitmap(mBitmap)

        getReviewList()
        getTrailerList()


        favorite_button!!.setOnClickListener {
            isFavorite = when {
                isFavorite -> {
                    favorite_button!!.setImageResource(R.mipmap.ic_favorite_white)
                    viewModel.removeTvShow(tvId)
                    false
                }
                else -> {
                    favorite_button!!.setImageResource(R.mipmap.ic_favorite_white_selected)
                    viewModel.addToFavourite(TvResult(this.tvId.toInt(), name, poster_url))
                    true
                }
            }
        }

        viewModel.getTvById(tvId).observe(this,
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
    }

    private fun getTrailerList() {
        val movieApiInterface = MovieApiClient.client!!
                .create(MovieApi::class.java)
        val getTrailers = movieApiInterface.getMovieTrailers(
                tvId,
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
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun getReviewList() {
        val movieApiInterface = MovieApiClient.client!!
                .create(MovieApi::class.java)
        val getReview = movieApiInterface.getMovieReviews(
                tvId,
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
                .setPositiveButton("Yes") { _, _ ->
                    val intent = Intent(
                            Intent.ACTION_VIEW, Uri.parse(getString(R.string.youtube_video, mTrailerList!![position].key)))
                    startActivity(intent)
                }
                .setNegativeButton("No") { _, _ ->
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
