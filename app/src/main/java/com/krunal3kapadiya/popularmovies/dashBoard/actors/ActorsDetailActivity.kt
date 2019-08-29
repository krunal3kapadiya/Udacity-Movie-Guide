package com.krunal3kapadiya.popularmovies.dashBoard.actors

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.krunal3kapadiya.popularmovies.Constants
import com.krunal3kapadiya.popularmovies.R
import kotlinx.android.synthetic.main.activity_actor_detail.*

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
        // TODO create tab screen here
        //
        viewModel.getActorsDetail(actorId).observe(this, Observer {
            it?.adult
            it?.deathday//
            it?.gender
            it?.homepage
            it?.imdbId
            it?.popularity
            it?.id

            supportActionBar?.title = it?.name
            actor_place_of_birth.text = it?.birthday
//            actor_known_as.text = it?.alsoKnownAs
            actor_birthday.text = it?.placeOfBirth//
            actor_details.text = it?.biography
            Glide.with(this)
                    .load(Constants.BASE_IMAGE_URL + Constants.POSTER_SIZE + it?.profilePath)
                    .placeholder(R.mipmap.ic_movie)
                    .into(actor_image)

        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}