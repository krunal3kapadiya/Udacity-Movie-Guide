package com.krunal3kapadiya.popularmovies.dashBoard.actors

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.krunal3kapadiya.popularmovies.Constants
import com.krunal3kapadiya.popularmovies.R
import com.krunal3kapadiya.popularmovies.data.model.Cast
import kotlinx.android.synthetic.main.fragment_now_playing.*

/**
 * @author krunal kapadiya
 * @link https://krunal3kapadiya.app
 * @date 14,April,2019
 */

class ActorsTvShowsFragment : Fragment() {
    companion object {
        fun newInstance(actorId: Int) = ActorsTvShowsFragment().apply {
            arguments = Bundle().apply {
                putInt("actorId", actorId)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_now_playing, container, false)
    }

    lateinit var adapter: CastListAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProviders.of(this).get(ActorsViewModel::class.java)
        var actorId = 0
        arguments?.let {
            actorId = it.getInt("actorId")
        }

        val layoutManager = GridLayoutManager(context, Constants.SPAN_RECYCLER_VIEW)
        rv_list_movie_main!!.layoutManager = layoutManager
        adapter = CastListAdapter(object : CastListAdapter.OnItemClick {
            override fun onItemClick(pos: Int, view: ImageView?, movies: Cast) {

            }
        })
        rv_list_movie_main!!.adapter = adapter

        viewModel.getActorsTVShows(actorId).observe(this, Observer {
            adapter.setData(it?.cast as ArrayList<Cast>?)
        })
    }
}