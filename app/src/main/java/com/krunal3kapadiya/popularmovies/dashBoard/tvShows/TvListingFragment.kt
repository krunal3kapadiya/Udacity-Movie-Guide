package com.krunal3kapadiya.popularmovies.dashBoard.tvShows

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.krunal3kapadiya.popularmovies.Constants
import com.krunal3kapadiya.popularmovies.view.EndlessRecyclerViewScrollListener
import com.krunal3kapadiya.popularmovies.R
import com.krunal3kapadiya.popularmovies.data.adapter.TVRVAdapter
import com.krunal3kapadiya.popularmovies.data.model.TvResult
import kotlinx.android.synthetic.main.fragment_now_playing.*

class TvListingFragment : Fragment(), TVRVAdapter.OnItemClick {
    override fun onItemClick(pos: Int, view: ImageView?, tvResult: TvResult, darkColor: Int, lightColor: Int) {
        context?.let {
            TVDetailActivity.launch(
                    context = it,
                    tvResult = tvResult,
                    lightColor = darkColor,
                    darkColor = lightColor
            )
        }
    }

    companion object {
        fun newInstance(id: Int): TvListingFragment {
            val tvListingFragment = TvListingFragment()
            val arg = Bundle()
            arg.putInt("ID", id)
            tvListingFragment.arguments = arg
            return tvListingFragment
        }
    }

    private var mAdapter: TVRVAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_now_playing, container, false)
    }

    private var scrollListener: EndlessRecyclerViewScrollListener? = null
    lateinit var viewModel: TvViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TvViewModel::class.java)
        val layoutManager = GridLayoutManager(context, Constants.SPAN_RECYCLER_VIEW)
        rv_list_movie_main.layoutManager = layoutManager
        mAdapter = TVRVAdapter(this)
        rv_list_movie_main.adapter = mAdapter
        loadNextDataFromApi(mAdapter!!, 1)
        scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                Log.d("SCROLLVIEW_LOADED", "SCROLLED ".plus(page).plus(" ").plus(totalItemsCount))
                val nextPage = page + 1
                loadNextDataFromApi(mAdapter!!, nextPage)
            }
        }
        rv_list_movie_main.addOnScrollListener(scrollListener as EndlessRecyclerViewScrollListener)
    }

    fun loadNextDataFromApi(mAdapter: TVRVAdapter, page: Int) {
        val number = arguments?.getInt("ID")
        viewModel.isLoading.observe(this, android.arch.lifecycle.Observer {
            if (it!!) {
                pb_main.visibility = View.VISIBLE
                rv_list_movie_main.visibility = View.GONE
            } else {
                pb_main.visibility = View.GONE
                rv_list_movie_main.visibility = View.VISIBLE
            }
        })

        viewModel.getPopularTvList(number, page)
        viewModel.movieArrayList.observe(this, android.arch.lifecycle.Observer {
            mAdapter.setData(it)
        })
    }
}