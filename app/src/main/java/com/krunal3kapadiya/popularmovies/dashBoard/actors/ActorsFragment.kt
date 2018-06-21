package com.krunal3kapadiya.popularmovies.dashBoard.actors

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.krunal3kapadiya.popularmovies.R

class ActorsFragment : Fragment() {
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
    }
}