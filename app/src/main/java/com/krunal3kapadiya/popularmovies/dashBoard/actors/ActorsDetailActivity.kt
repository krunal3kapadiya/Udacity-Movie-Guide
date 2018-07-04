package com.krunal3kapadiya.popularmovies.dashBoard.actors

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.krunal3kapadiya.popularmovies.R

class ActorsDetailActivity : AppCompatActivity() {
    companion object {
        fun launch(context: Context, id: Int) {
            val intent = Intent(context, ActorsDetailActivity::class.java)
            intent.putExtra("actorId", id)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actor_detail)

        val actorId = intent.getIntExtra("actorId", 0)
        val viewModel = ViewModelProviders.of(this).get(ActorsViewModel::class.java)

        viewModel.getActorsDetail(actorId).observe(this, Observer {
            it?.adult
            it?.profilePath
            it?.alsoKnownAs
            it?.biography
            it?.deathday
            it?.birthday
            it?.gender
            it?.homepage
            it?.imdbId
            it?.name
            it?.placeOfBirth
            it?.popularity
            it?.id
        })
    }
}