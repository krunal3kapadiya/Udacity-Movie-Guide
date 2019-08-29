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
import com.krunal3kapadiya.popularmovies.data.adapter.MovieRVAdapter
import com.krunal3kapadiya.popularmovies.data.model.Movies
import kotlinx.android.synthetic.main.activity_search.*
import android.support.v4.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T


class SearchActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context, searchString: String) {
            val intent = Intent(context, SearchActivity::class.java)
            intent.putExtra("searchString", searchString)
            context.startActivity(intent)
        }
    }

    private var scrollListener: EndlessRecyclerViewScrollListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        supportActionBar?.elevation = 0F
        supportActionBar!!.title = intent.getStringExtra("searchString")
        val layoutManager = GridLayoutManager(this, 2)

        search_rv.layoutManager = layoutManager
        val mAdapter = MovieRVAdapter(object : MovieRVAdapter.OnItemClick {
            override fun onItemClick(pos: Int, view: ImageView?, movies: Movies) {
                val intent = Intent(this@SearchActivity, MovieDetailActivity::class.java)
                intent.putExtra(MovieDetailActivity.ARG_MOVIE, movies)
                startActivity(intent)
            }
        })


//                SearchAdapter(this, object : SearchAdapter.OnItemClick {
//            override fun onItemClick(pos: Int, view: ImageView?, movies: SearchResult) {
//                Toast.makeText(applicationContext, "Coming soon..", Toast.LENGTH_LONG).show()
//            }
//        })

        search_rv.adapter = mAdapter
        loadNextDataFromApi(mAdapter, 1)
        scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                Log.d("SCROLLVIEW_LOADED", "SCROLLED ".plus(page).plus(" ").plus(totalItemsCount))
                val nextPage = page + 1
                loadNextDataFromApi(mAdapter, nextPage)
            }
        }
        // Adds the scroll listener to RecyclerView
        search_rv.addOnScrollListener(scrollListener)

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

    private fun loadNextDataFromApi(adapter: MovieRVAdapter, page: Int) {
        val viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        viewModel.searchMovies(intent.getStringExtra("searchString"), page)
                .observe(this, Observer {
                    //TODO check for null values
                    adapter.addData(it?.results)
                })

    }
}