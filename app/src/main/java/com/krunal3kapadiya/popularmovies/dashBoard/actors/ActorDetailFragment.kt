package com.krunal3kapadiya.popularmovies.dashBoard.actors

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.krunal3kapadiya.popularmovies.Constants
import com.krunal3kapadiya.popularmovies.R
import kotlinx.android.synthetic.main.activity_actor_detail.*

/**
 * @author krunal kapadiya
 * @link https://krunal3kapadiya.app
 * @date 14,April,2019
 */

class ActorDetailFragment : Fragment() {
    companion object {
        fun newInstance(actorId: Int) = ActorDetailFragment()
                .apply {
                    arguments = Bundle().apply {
                        putInt("actorId", actorId)
                    }
                }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_actor_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var actorId = 0
        arguments?.let {
            actorId = it.getInt("actorId")
        }

        val viewModel = ViewModelProviders.of(this).get(ActorsViewModel::class.java)
        viewModel.getActorsDetail(actorId).observe(this, Observer {
            it?.adult
            it?.deathday//
            it?.gender
            it?.homepage
            it?.imdbId
            it?.popularity
            it?.id

            actor_place_of_birth.text = it?.birthday
//            actor_known_as.text = it?.alsoKnownAs.toString()
            actor_birthday.text = it?.placeOfBirth//
            actor_details.text = it?.biography
            Glide.with(this)
                    .load(Constants.BASE_IMAGE_URL + Constants.POSTER_SIZE_500 + it?.profilePath)
                    .placeholder(R.mipmap.ic_movie)
                    .into(actor_image)
        })

        super.onViewCreated(view, savedInstanceState)
    }

}