package com.krunal3kapadiya.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;
import com.krunal3kapadiya.popularmovies.data.MovieContract;
import com.krunal3kapadiya.popularmovies.data.adapter.MovieRVAdapter;
import com.krunal3kapadiya.popularmovies.data.api.MovieApiClient;
import com.krunal3kapadiya.popularmovies.data.api.MovieApiInterface;
import com.krunal3kapadiya.popularmovies.data.model.MovieResponse;
import com.krunal3kapadiya.popularmovies.data.model.Movies;

import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;

import javax.annotation.Nonnull;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements
        MovieRVAdapter.OnItemClick {

    private static final int CURSOR_LOADER_ID = 0;
    private Context mContext;
    private ArrayList<Movies> mMoviesArrayList;
    private MovieRVAdapter mAdapter;

    @BindView(R.id.rv_list_movie_main)
    RecyclerView recyclerView;
    @BindView(R.id.activity_main_relative_layout)
    RelativeLayout activity_main_relative_layout;
    @BindView(R.id.pb_main)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = MainActivity.this;

        mMoviesArrayList = new ArrayList<>();

        int SPAN = 2;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            SPAN = 4;
        }

        final RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, SPAN);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new MovieRVAdapter(this, mMoviesArrayList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
//                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
//
//                if (!isLoading && !isLastPage) {
//                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
//                            && firstVisibleItemPosition >= 0
//                            && totalItemCount >= PAGE_SIZE) {
//                        loadMoreItems();
//                    }
//                }
            }
        });
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build());

        getMovieListPop();

        // TODO Add pagination
    }


    public void getMovieListPop() {
        if (Constants.isNetworkAvailable(mContext)) {
            getPopularMovieList();
        } else {
            displayNetworkDisableError(activity_main_relative_layout, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getMovieListPop();
                }
            });
        }
    }

    public void getPopularMovieList() {
        setLoading(true);
        final MovieApiInterface movieClient = MovieApiClient.getClient()
                .create(MovieApiInterface.class);
        Call<MovieResponse> getPopMovie = movieClient.getPopularMoviesList(Constants.API_KEY);
        getPopMovie.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                mMoviesArrayList.clear();
                mMoviesArrayList.addAll(response.body().getResults());
                mAdapter.notifyDataSetChanged();

                setLoading(false);
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();

                setLoading(false);
            }
        });
    }

    public void getTopRatedMovieList() {
        setLoading(true);
        final MovieApiInterface movieClient = MovieApiClient.getClient()
                .create(MovieApiInterface.class);
        Call<MovieResponse> getTopRatedMovie = movieClient.getTopRatedMovies(Constants.API_KEY);
        getTopRatedMovie.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                mMoviesArrayList.clear();
                mMoviesArrayList.addAll(response.body().getResults());
                mAdapter.notifyDataSetChanged();

                setLoading(false);
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                setLoading(false);
                Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void displayNetworkDisableError(View view, View.OnClickListener listener) {
        Snackbar snackbar = Snackbar.make(view, R.string.internet_not_available, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry, listener);
        snackbar.show();
    }

    public void setLoading(boolean b) {
        recyclerView.setVisibility(b ? View.GONE : View.VISIBLE);
        mProgressBar.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onItemClick(@Nonnull int pos, @Nonnull ImageView view) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.ARG_MOVIE, mMoviesArrayList.get(pos));

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                view,
                ViewCompat.getTransitionName(view));


        startActivity(intent, options.toBundle());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_pop:
                getMovieListPop();
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                return true;
            case R.id.action_sort_rate:
                getMovieListTop();
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                return true;
            case R.id.action_favorite:
                mMoviesArrayList.clear();
                // getContentResolver().notifyChange();
                getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                        new String[]{MovieContract.MovieEntry._ID}, null, null, null);
                // initialize loader
                getSupportLoaderManager().initLoader(CURSOR_LOADER_ID, null, new LoaderManager.LoaderCallbacks<Cursor>() {
                    @Override
                    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                        return new CursorLoader(mContext, MovieContract.MovieEntry.CONTENT_URI,
                                null, null, null, null);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                        Movies movies;
                        while (data.moveToNext()) {
                            movies = new Movies(
                                    data.getInt(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID)),
                                    data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE)),
                                    data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER)),
                                    data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_BACKDROP)),
                                    data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW)),
                                    data.getInt(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATINGS)),
                                    data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE))
                            );
                            mMoviesArrayList.add(movies);
                        }
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onLoaderReset(Loader<Cursor> loader) {

                    }
                });


                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getMovieListTop() {
        if (Constants.isNetworkAvailable(mContext)) {
            getTopRatedMovieList();
        } else {
            displayNetworkDisableError(activity_main_relative_layout, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getMovieListTop();
                }
            });
        }
    }

    public class MyObserver extends ContentObserver {

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public MyObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
        }
    }
}
