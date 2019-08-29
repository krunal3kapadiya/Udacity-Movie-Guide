package com.krunal3kapadiya.popularmovies

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
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.krunal3kapadiya.popularmovies.data.adapter.ReviewRVAdapter
import com.krunal3kapadiya.popularmovies.data.adapter.TrailerRVAdapter
import com.krunal3kapadiya.popularmovies.data.api.MovieApi
import com.krunal3kapadiya.popularmovies.data.api.MovieApiClient
import com.krunal3kapadiya.popularmovies.data.model.Result
import com.krunal3kapadiya.popularmovies.data.model.Reviews
import com.krunal3kapadiya.popularmovies.data.model.Trailer
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_movie_detail.*
import java.util.regex.Pattern

class TVDetailActivity(private var isFavorite: Boolean = false) : AppCompatActivity(), TrailerRVAdapter.OnItemClick, ReviewRVAdapter.OnReviewItemClick {

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
        val movieItem = intent.getParcelableExtra<Result>(ARG_MOVIE)

        mContext = this@TVDetailActivity

        movieItem.backdropPath?.let {
            Picasso.with(this)
                    .load(Constants.BASE_IMAGE_URL + Constants.POSTER_SIZE_500 + movieItem.backdropPath)
                    .placeholder(R.mipmap.ic_movie)
                    .into(object : Target {
                        override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                            mBitmap = bitmap
                            val palette = Palette.from(bitmap).generate()
                            themeLightColor = palette.getDominantColor(ContextCompat.getColor(mContext!!, R.color.colorAccent))
                            themeDarkColor = palette.getDarkVibrantColor(ContextCompat.getColor(mContext!!, R.color.colorAccent))

                        }

                        override fun onBitmapFailed(errorDrawable: Drawable) {

                        }

                        override fun onPrepareLoad(placeHolderDrawable: Drawable) {

                        }
                    })

        }



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
        arb_movie_ratings!!.rating = java.lang.Float.valueOf((movieItem.voteAverage!!.toDouble() / 2.0).toString())!!
        txt_movie_overview!!.text = movieItem.overview

        val pattern = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})")
        /*val dateMatcher = pattern.matcher(movieItem?.firstAirDate)
        if (dateMatcher.find()) {
            txt_movie_release!!.text = dateMatcher.group(1)
        }*/

        movieId = movieItem.id.toLong()

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
                .load(Constants.BASE_IMAGE_URL + Constants.POSTER_SIZE_500 + movieItem.backdropPath)
                .into(movie_detail_image)

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
