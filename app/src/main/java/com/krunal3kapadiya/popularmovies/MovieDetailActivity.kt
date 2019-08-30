package com.krunal3kapadiya.popularmovies

import android.arch.lifecycle.ViewModelProviders
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.Palette
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SnapHelper
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.krunal3kapadiya.popularmovies.dashBoard.actors.ActorsDetailActivity
import com.krunal3kapadiya.popularmovies.dashBoard.movies.MoviesViewModel
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
import kotlinx.android.synthetic.main.row_casts.*
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
    private var mBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val movieItem = intent.getParcelableExtra<Movies>(ARG_MOVIE)

        mContext = this@MovieDetailActivity

        Glide.with(this)
                .asBitmap()
                .load(Constants.BASE_IMAGE_URL + Constants.POSTER_SIZE + movieItem.url)
                .placeholder(R.mipmap.ic_movie)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        mBitmap = resource
                        val palette = Palette.from(resource).generate()
                        themeLightColor = palette.getDominantColor(ContextCompat.getColor(mContext!!, R.color.colorAccent))
                        themeDarkColor = palette.getDarkVibrantColor(ContextCompat.getColor(mContext!!, R.color.colorAccent))
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = themeDarkColor
        }


        setContentView(R.layout.activity_movie_detail)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //        try {
        //            Field field =getSupportActionBar().getClass().getDeclaredField("mTitleTextView");
        //            field.setAccessible(true);
        //            TextView title = (TextView) field.get(this);
        //            title.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        //            title.setMarqueeRepeatLimit(-1);


        //            Field f = toolbar.getClass().getDeclaredField("mTitleTextView");
        //            f.setAccessible(true);
        //            //TODO add scroll text
        //            TextView toolbarText = (TextView) f.get(toolbar);
        //            toolbarText.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        //            toolbarText.setFocusable(true);
        //            toolbarText.setFocusableInTouchMode(true);
        //            toolbarText.requestFocus();
        //            toolbarText.setSingleLine(true);
        //            toolbarText.setSelected(true);
        //            toolbarText.setMarqueeRepeatLimit(-1);

        //            title.setText("Hello Android ! This is a sample marquee text. That's great. Enjoy");
        //        } catch (NoSuchFieldException | IllegalAccessException e) {
        //            e.printStackTrace();
        //        }


        supportActionBar!!.title = movieItem.name
        arb_movie_ratings!!.rating = java.lang.Float.valueOf((movieItem.rating!!.toDouble() / 2.0).toString())!!
        txt_movie_overview!!.text = movieItem.overView

        val pattern = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})")
        val dateMatcher = pattern.matcher(movieItem.releaseDate)
        if (dateMatcher.find()) {
            txt_movie_release!!.text = dateMatcher.group(1)
        }

        movieId = movieItem.id.toLong()

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

        Glide.with(this)
                .load(Constants.BASE_IMAGE_URL + Constants.POSTER_SIZE_500 + movieItem.url)
                .into(img_movie_poster)

        img_movie_poster.setOnClickListener { ViewImageActivity.launch(this, movieItem.url) }
        getReviewList()
        getTrailerList()

        val moviesViewModel = ViewModelProviders.of(this).get(MoviesViewModel::class.java)
        moviesViewModel.getCast(movieItem.id)

        casts_view.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        snapHelper.attachToRecyclerView(casts_view)
        val actorsAdapter = ActorsListAdapter(object : ActorsListAdapter.OnItemClick {
            override fun onItemClick(pos: Int, cast: Cast) {
                ActorsDetailActivity.launch(this@MovieDetailActivity, cast.id)
            }
        })
        casts_view.adapter = actorsAdapter
        moviesViewModel.mCastArrayList.observe(this, android.arch.lifecycle.Observer {
            actorsAdapter.setData(it)
        })

        favorite_button!!.setOnClickListener {
            if (isFavorite) {
                favorite_button!!.setImageResource(R.mipmap.ic_favorite_white)
                isFavorite = false
            } else {
                favorite_button!!.setImageResource(R.mipmap.ic_favorite_white_selected)
                val contentValues = ContentValues()
                isFavorite = true
            }
        }

        if (isFavorite)
            favorite_button!!.setImageResource(R.mipmap.ic_favorite_white_selected)
        else
            favorite_button!!.setImageResource(R.mipmap.ic_favorite_white)

        Glide.with(this)
                .load(Constants.BASE_IMAGE_URL + Constants.POSTER_SIZE_500 + movieItem.backDropPath)
                .placeholder(ContextCompat.getDrawable(this, R.mipmap.ic_movie))
                .into(movie_detail_image)

        movie_detail_image.setOnClickListener {
            ViewImageActivity.launch(this, movieItem.backDropPath)
        }
        ctl_movie_detail!!.setContentScrimColor(themeLightColor)
        favorite_button!!.backgroundTintList = ColorStateList.valueOf(themeLightColor)
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

    companion object {
        var ARG_MOVIE = "movie"
    }
}
