package com.krunal3kapadiya.popularmovies

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.ImageView
import com.krunal3kapadiya.popularmovies.dashBoard.actors.ActorsAdapter
import com.krunal3kapadiya.popularmovies.dashBoard.actors.ActorsDetailActivity
import com.krunal3kapadiya.popularmovies.data.adapter.MovieRVAdapter
import com.krunal3kapadiya.popularmovies.data.adapter.TVRVAdapter
import com.krunal3kapadiya.popularmovies.data.model.Movies
import com.krunal3kapadiya.popularmovies.data.model.Result
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context, searchString: String, tabPosition: Int) {
            val intent = Intent(context, SearchActivity::class.java)
            intent.putExtra("searchString", searchString)
            intent.putExtra("tabPosition", tabPosition)
            context.startActivity(intent)
        }
    }

    private var scrollListener: EndlessRecyclerViewScrollListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        supportActionBar?.elevation = 0F
        supportActionBar!!.title = intent.getStringExtra("searchString")
        val layoutManager = GridLayoutManager(this, 3)

        search_rv.layoutManager = layoutManager
        val mAdapter = MovieRVAdapter(object : MovieRVAdapter.OnItemClick {
            override fun onItemClick(pos: Int, view: ImageView?, movies: Movies) {
                val intent = Intent(this@SearchActivity, MovieDetailActivity::class.java)
                intent.putExtra(MovieDetailActivity.ARG_MOVIE, movies)
                startActivity(intent)
            }
        })


        val tvAdapter = TVRVAdapter(this@SearchActivity, object : TVRVAdapter.OnItemClick {
            override fun onItemClick(pos: Int, view: ImageView?, movies: Result) {
                val intent = Intent(this@SearchActivity, TVDetailActivity::class.java)
                intent.putExtra(MovieDetailActivity.ARG_MOVIE, movies)
                startActivity(intent)
            }

        })

        val mActorAdapter = ActorsAdapter(object : ActorsAdapter.OnActorClickListener {
            override fun onActorClick(result: com.krunal3kapadiya.popularmovies.dashBoard.actors.Result) {
                ActorsDetailActivity.launch(this@SearchActivity, result.id)
            }
        })

        when (intent.getIntExtra("tabPosition", 0)) {
            0 -> {
                search_rv.adapter = mAdapter
            }
            1 -> {
                search_rv.adapter = tvAdapter
            }
            2 -> {
                search_rv.adapter = mActorAdapter
            }
        }


        loadNextDataFromApi(mAdapter, tvAdapter, mActorAdapter, 1)
        scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                Log.d("SCROLLVIEW_LOADED", "SCROLLED ".plus(page).plus(" ").plus(totalItemsCount))
                val nextPage = page + 1
                loadNextDataFromApi(mAdapter, tvAdapter, mActorAdapter, nextPage)
            }
        }
        // Adds the scroll listener to RecyclerView
        search_rv.addOnScrollListener(scrollListener as EndlessRecyclerViewScrollListener)

//        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
//            override fun getSpanSize(position: Int): Int {
//                return when (mAdapter.getItemViewType(position)) {
//                    MovieRVAdapter.VIEW_TYPES.data -> 1
//                    MovieRVAdapter.VIEW_TYPES.loading -> 2
//                    else -> -1
//                }
//            }
//        }
    }

    private fun loadNextDataFromApi(adapter: MovieRVAdapter,
                                    tvAdapter: TVRVAdapter,
                                    actorAdapter: ActorsAdapter,
                                    page: Int) {
        val viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        val tabPosition = intent.getIntExtra("tabPosition", 0)
        viewModel.searchMovies(intent.getStringExtra("searchString"),
                intent.getIntExtra("tabPosition", 0),
                page)

        Log.d(SearchActivity::class.java.name, "Page ".plus(page))

        when (tabPosition) {
            0 -> viewModel.searchMovie.observe(this, Observer {
                adapter.addData(it?.results)
            })
            1 -> {
                viewModel.searchTV.observe(this, Observer {
                    tvAdapter.addData(it?.results)
                })
            }
            2 -> {
                viewModel.searchActor.observe(this, Observer {
                    actorAdapter.addData(it?.result)
                })
            }
        }

    }
}