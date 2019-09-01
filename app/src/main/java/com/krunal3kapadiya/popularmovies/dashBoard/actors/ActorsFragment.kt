package com.krunal3kapadiya.popularmovies.dashBoard.actors

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.krunal3kapadiya.popularmovies.Constants
import com.krunal3kapadiya.popularmovies.EndlessRecyclerViewScrollListener
import com.krunal3kapadiya.popularmovies.R
import kotlinx.android.synthetic.main.fragment_actors.*

class ActorsFragment : Fragment(), ActorsAdapter.OnActorClickListener {
    override fun onActorClick(result: Result) {
        ActorsDetailActivity.launch(activity!!, result.id)
    }

    companion object {
        fun newInstance() = ActorsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_actors, container, false)
    }

    lateinit var viewModel: ActorsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ActorsViewModel::class.java)
        val layoutManager = GridLayoutManager(context, Constants.SPAN_RECYCLER_VIEW)
        actorsList.layoutManager = layoutManager
        val adapter = ActorsAdapter(this)
        actorsList.adapter = adapter
        loadNextDataFromApi(adapter, 1)

        val scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                Log.d("SCROLLVIEW_LOADED", "SCROLLED ".plus(page).plus(" ").plus(totalItemsCount))
                val nextPage = page + 1
                loadNextDataFromApi(adapter, nextPage)
            }
        }
        actorsList.addOnScrollListener(scrollListener as EndlessRecyclerViewScrollListener)
    }

    fun loadNextDataFromApi(adapter: ActorsAdapter, page: Int) {
        viewModel.isLoading.observe(this, android.arch.lifecycle.Observer {
            if (it!!) {
                pb_main.visibility = View.VISIBLE
                actorsList.visibility = View.GONE
            } else {
                pb_main.visibility = View.GONE
                actorsList.visibility = View.VISIBLE
            }
        })

        viewModel.getActorsList(page).observe(this, Observer {
            adapter.setData(it)
        })
    }
}