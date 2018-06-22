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
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import butterknife.ButterKnife
import com.krunal3kapadiya.popularmovies.data.MovieContract
import com.krunal3kapadiya.popularmovies.data.adapter.ReviewRVAdapter
import com.krunal3kapadiya.popularmovies.data.adapter.TrailerRVAdapter
import com.krunal3kapadiya.popularmovies.data.api.MovieApiClient
import com.krunal3kapadiya.popularmovies.data.api.MovieApiInterface
import com.krunal3kapadiya.popularmovies.data.model.*
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.android.synthetic.main.activity_movie_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.regex.Pattern

class MovieDetailActivity : AppCompatActivity(), TrailerRVAdapter.OnItemClick, ReviewRVAdapter.OnReviewItemClick {

    private var mContext: Context? = null
    private var movieId: Long = 0

    private var mTrailerList: ArrayList<Trailer>? = null
    private var mReviewList: ArrayList<Reviews>? = null
    private var mTrailerAdapter: TrailerRVAdapter? = null
    private var mReviewAdapter: ReviewRVAdapter? = null
    private var themeLightColor: Int = 0
    private var themeDarkColor: Int = 0
    private var mBitmap: Bitmap? = null

    internal var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val movieItem = intent.getParcelableExtra<Movies>(ARG_MOVIE)

        mContext = this@MovieDetailActivity

        Picasso.with(this)
                .load(Constants.BASE_IMAGE_URL + Constants.POSTER_SIZE + movieItem.url)
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


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = themeDarkColor
        }


        setContentView(R.layout.activity_movie_detail)
        ButterKnife.bind(this)

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
        rv_movie_trailer!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {})
        rv_movie_reviews!!.layoutManager = LinearLayoutManager(this)
        rv_movie_reviews!!.adapter = mReviewAdapter

        img_movie_poster!!.setImageBitmap(mBitmap)

        getReviewList()
        getTrailerList()


        val cursor = contentResolver.query(MovieContract.MovieEntry.CONTENT_URI,
                null,
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                arrayOf(movieItem.id.toString()), null)

        val numRows = cursor!!.count
        cursor.close()
        isFavorite = numRows == 1


        Log.i("MovieList", "" + numRows)


        favorite_button!!.setOnClickListener {
            if (isFavorite) {
                favorite_button!!.setImageResource(R.mipmap.ic_favorite_white)
                contentResolver
                        .delete(MovieContract.MovieEntry.CONTENT_URI
                                .buildUpon()
                                .appendPath(movieItem.id.toString())
                                .build(), null, null)
                isFavorite = false
            } else {
                favorite_button!!.setImageResource(R.mipmap.ic_favorite_white_selected)
                val contentValues = ContentValues()
                contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movieItem.id)
                contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movieItem.name)
                contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER, movieItem.url)
                contentValues.put(MovieContract.MovieEntry.COLUMN_BACKDROP, movieItem.backDropPath)
                contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movieItem.overView)
                contentValues.put(MovieContract.MovieEntry.COLUMN_RATINGS, movieItem.rating)
                contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movieItem.releaseDate)
                contentResolver.insert(MovieContract.MovieEntry.CONTENT_URI, contentValues)
                isFavorite = true
            }
        }

        if (isFavorite)
            favorite_button!!.setImageResource(R.mipmap.ic_favorite_white_selected)
        else
            favorite_button!!.setImageResource(R.mipmap.ic_favorite_white)

        Picasso.with(this)
                .load(Constants.BASE_IMAGE_URL + Constants.POSTER_SIZE_500 + movieItem.backDropPath)
                .into(movie_detail_image)

        ctl_movie_detail!!.setContentScrimColor(themeLightColor)
        favorite_button!!.backgroundTintList = ColorStateList.valueOf(themeLightColor)
    }

    private fun getTrailerList() {
        val movieApiInterface = MovieApiClient.client!!
                .create(MovieApiInterface::class.java)
        val getTrailers = movieApiInterface.getMovieTrailers(movieId, Constants.API_KEY)
        getTrailers.enqueue(object : Callback<TrailerResponse> {
            override fun onResponse(call: Call<TrailerResponse>, response: Response<TrailerResponse>) {
                mTrailerList!!.clear()
                response.body()!!.trailerArrayList?.let { mTrailerList!!.addAll(it) }
                mTrailerAdapter!!.notifyDataSetChanged()

                card_movie_trailer!!.visibility = View.VISIBLE
            }

            override fun onFailure(call: Call<TrailerResponse>, t: Throwable) {}
        })
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
                .create(MovieApiInterface::class.java)
        val getReview = movieApiInterface.getMovieReviews(movieId, Constants.API_KEY)
        getReview.enqueue(object : Callback<ReviewsResponse> {
            override fun onResponse(call: Call<ReviewsResponse>, response: Response<ReviewsResponse>) {
                response.body()!!.reviewsArrayList?.let { mReviewList!!.addAll(it) }
                mReviewAdapter!!.notifyDataSetChanged()

                card_movie_review!!.visibility = View.VISIBLE
            }

            override fun onFailure(call: Call<ReviewsResponse>, t: Throwable) {
                Toast.makeText(mContext, "Error occured Review List ", Toast.LENGTH_SHORT).show()
            }
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