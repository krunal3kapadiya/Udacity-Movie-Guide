package com.krunal3kapadiya.popularmovies.dashBoard.actors

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.krunal3kapadiya.popularmovies.R
import kotlinx.android.synthetic.main.fragment_actors.*

class ActorsFragment : Fragment(), ActorsAdapter.OnActorClickListener {
    override fun onActorClick(result: Result) {
        ActorsDetailActivity.launch(activity!!,result.id)
    }

    companion object {
        fun newInstance(): ActorsFragment {
            return ActorsFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_actors, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProviders.of(this).get(ActorsViewModel::class.java)

        actorsList.layoutManager = GridLayoutManager(context, 2)
        val adapter = ActorsAdapter(this)
        actorsList.adapter = adapter

        viewModel.getActorsList().observe(this, Observer {
            adapter.setData(it)
        })
    }
}