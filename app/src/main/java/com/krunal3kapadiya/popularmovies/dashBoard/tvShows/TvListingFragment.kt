package com.krunal3kapadiya.popularmovies.dashBoard.tvShows

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.krunal3kapadiya.popularmovies.Constants
import com.krunal3kapadiya.popularmovies.MovieDetailActivity
import com.krunal3kapadiya.popularmovies.R
import com.krunal3kapadiya.popularmovies.TVDetailActivity
import com.krunal3kapadiya.popularmovies.data.adapter.TVRVAdapter
import com.krunal3kapadiya.popularmovies.data.model.Result
import kotlinx.android.synthetic.main.fragment_now_playing.*

class TvListingFragment : Fragment(), TVRVAdapter.OnItemClick {
    override fun onItemClick(pos: Int, view: ImageView?, movies: Result) {
        val intent = Intent(context, TVDetailActivity::class.java)
        intent.putExtra(MovieDetailActivity.ARG_MOVIE, movies)
        startActivity(intent)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProviders.of(this).get(TvViewModel::class.java)
        val layoutManager = GridLayoutManager(context, Constants.SPAN_RECYCLER_VIEW)
        rv_list_movie_main.layoutManager = layoutManager
        mAdapter = TVRVAdapter(context!!, this)
        rv_list_movie_main.adapter = mAdapter
        val number = arguments?.getInt("ID")
        viewModel.getPopularTvList(number)
        viewModel.movieArrayList.observe(this, android.arch.lifecycle.Observer {
            mAdapter!!.setData(it)
        })
    }
}