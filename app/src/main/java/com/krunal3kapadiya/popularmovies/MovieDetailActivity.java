package com.krunal3kapadiya.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.krunal3kapadiya.popularmovies.data.MovieContract;
import com.krunal3kapadiya.popularmovies.data.adapter.ReviewRVAdapter;
import com.krunal3kapadiya.popularmovies.data.adapter.TrailerRVAdapter;
import com.krunal3kapadiya.popularmovies.data.api.MovieApiClient;
import com.krunal3kapadiya.popularmovies.data.api.MovieApiInterface;
import com.krunal3kapadiya.popularmovies.data.model.Movies;
import com.krunal3kapadiya.popularmovies.data.model.Reviews;
import com.krunal3kapadiya.popularmovies.data.model.ReviewsResponse;
import com.krunal3kapadiya.popularmovies.data.model.Trailer;
import com.krunal3kapadiya.popularmovies.data.model.TrailerResponse;
import com.krunal3kapadiya.popularmovies.view.MarqueeToolbar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity implements TrailerRVAdapter.OnItemClick, ReviewRVAdapter.OnReviewItemClick {

    public static String ARG_MOVIE = "movie";

    private Context mContext;
    private long movieId;

    private ArrayList<Trailer> mTrailerList;
    private ArrayList<Reviews> mReviewList;
    private TrailerRVAdapter mTrailerAdapter;
    private ReviewRVAdapter mReviewAdapter;
    private int themeLightColor;
    private int themeDarkColor;
    private Bitmap mBitmap;

    @BindView(R.id.card_movie_trailer)
    CardView card_movie_trailer;
    @BindView(R.id.card_movie_review)
    CardView card_movie_review;
    @BindView(R.id.ctl_movie_detail)
    CollapsingToolbarLayout ctl_movie_detail;
    @BindView(R.id.arb_movie_ratings)
    AppCompatRatingBar movieRating;
    @BindView(R.id.txt_movie_overview)
    TextView movieOverView;
    @BindView(R.id.txt_movie_release)
    TextView movieRelease;
    @BindView(R.id.img_movie_poster)
    ImageView moviePoster;
    @BindView(R.id.rv_movie_trailer)
    RecyclerView rvTrailer;
    @BindView(R.id.rv_movie_reviews)
    RecyclerView rvReviews;
    @BindView(R.id.favorite_button)
    FloatingActionButton fab;
    @BindView(R.id.movie_detail_image)
    ImageView imageView;
    @BindView(R.id.toolbar)
    MarqueeToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Movies movieItem = getIntent().getParcelableExtra(ARG_MOVIE);

        mContext = MovieDetailActivity.this;

        Picasso.with(this)
                .load(Constants.BASE_IMAGE_URL + Constants.POSTER_SIZE + movieItem.getUrl())
                .placeholder(R.mipmap.ic_movie)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        mBitmap = bitmap;
                        Palette palette = Palette.from(bitmap).generate();
                        themeLightColor = palette.getDominantColor(ContextCompat.getColor(mContext, R.color.colorAccent));
                        themeDarkColor = palette.getDarkVibrantColor(ContextCompat.getColor(mContext, R.color.colorAccent));

                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(themeDarkColor);
        }


        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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


        getSupportActionBar().setTitle(movieItem.getName());
        movieRating.setRating(Float.valueOf(String.valueOf(movieItem.getRating() / 2.0)));
        movieOverView.setText(movieItem.getOverView());

        Pattern pattern = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})");
        Matcher dateMatcher = pattern.matcher(movieItem.getReleaseDate());
        if (dateMatcher.find()) {
            movieRelease.setText(dateMatcher.group(1));
        }

        movieId = movieItem.getId();

        mReviewList = new ArrayList<>();
        mReviewAdapter = new ReviewRVAdapter(this, mReviewList, themeLightColor);
        mTrailerList = new ArrayList<>();
        mTrailerAdapter = new TrailerRVAdapter(this, mTrailerList);
        rvTrailer.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvTrailer.setAdapter(mTrailerAdapter);
        rvTrailer.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        rvReviews.setLayoutManager(new LinearLayoutManager(this));
        rvReviews.setAdapter(mReviewAdapter);

        moviePoster.setImageBitmap(mBitmap);

        getReviewList();
        getTrailerList();


        Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                null,
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{String.valueOf(movieItem.getId())},
                null);

        final int numRows = cursor.getCount();
        cursor.close();
        isFavorite = numRows == 1;


        Log.i("MovieList", "" + numRows);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFavorite) {
                    fab.setImageResource(R.mipmap.ic_favorite_white);
                    getContentResolver()
                            .delete(MovieContract.MovieEntry.CONTENT_URI
                                            .buildUpon()
                                            .appendPath(String.valueOf(movieItem.getId()))
                                            .build(),
                                    null,
                                    null);
                    isFavorite = false;
                } else {
                    fab.setImageResource(R.mipmap.ic_favorite_white_selected);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movieItem.getId());
                    contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movieItem.getName());
                    contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER, movieItem.getUrl());
                    contentValues.put(MovieContract.MovieEntry.COLUMN_BACKDROP, movieItem.getBackDropPath());
                    contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movieItem.getOverView());
                    contentValues.put(MovieContract.MovieEntry.COLUMN_RATINGS, movieItem.getRating());
                    contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movieItem.getReleaseDate());
                    getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
                    isFavorite = true;
                }
            }
        });

        if (isFavorite)
            fab.setImageResource(R.mipmap.ic_favorite_white_selected);
        else
            fab.setImageResource(R.mipmap.ic_favorite_white);

        Picasso.with(this)
                .load(Constants.BASE_IMAGE_URL + Constants.POSTER_SIZE_500 + movieItem.getBackDropPath())
                .into(imageView);

        ctl_movie_detail.setContentScrimColor(themeLightColor);
        fab.setBackgroundTintList(ColorStateList.valueOf(themeLightColor));
    }

    private void getTrailerList() {
        MovieApiInterface movieApiInterface = MovieApiClient.getClient()
                .create(MovieApiInterface.class);
        Call<TrailerResponse> getTrailers = movieApiInterface.getMovieTrailers(movieId, Constants.API_KEY);
        getTrailers.enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                mTrailerList.clear();
                mTrailerList.addAll(response.body().getTrailerArrayList());
                mTrailerAdapter.notifyDataSetChanged();

                card_movie_trailer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {
            }
        });
    }

    boolean isFavorite = false;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getReviewList() {
        MovieApiInterface movieApiInterface = MovieApiClient.getClient()
                .create(MovieApiInterface.class);
        Call<ReviewsResponse> getReview = movieApiInterface.getMovieReviews(movieId, Constants.API_KEY);
        getReview.enqueue(new Callback<ReviewsResponse>() {
            @Override
            public void onResponse(Call<ReviewsResponse> call, Response<ReviewsResponse> response) {
                mReviewList.addAll(response.body().getReviewsArrayList());
                mReviewAdapter.notifyDataSetChanged();

                card_movie_review.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<ReviewsResponse> call, Throwable t) {
                Toast.makeText(mContext, "Error occured Review List ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(
                Intent.ACTION_VIEW, Uri.parse(getString(R.string.youtube_video, mTrailerList.get(position).getKey())));
        startActivity(intent);
    }

    @Override
    public void onReviewItemClick(int position) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mReviewList.get(position).getReviewURL()));
        startActivity(intent);
    }
}
