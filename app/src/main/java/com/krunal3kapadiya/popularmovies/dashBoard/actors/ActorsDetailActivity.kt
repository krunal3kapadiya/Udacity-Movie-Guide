package com.krunal3kapadiya.popularmovies.dashBoard.actors

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.krunal3kapadiya.popularmovies.R
import com.krunal3kapadiya.popularmovies.dashBoard.TabFragmentAdapter
import kotlinx.android.synthetic.main.fragment_movies.*

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
        setContentView(R.layout.fragment_movies)
        val actorId = intent.getIntExtra("actorId", 0)
        val viewModel = ViewModelProviders.of(this).get(ActorsViewModel::class.java)

        val adapter = TabFragmentAdapter(supportFragmentManager)

        adapter.addFragment(ActorDetailFragment.newInstance(actorId), "Biography")
        adapter.addFragment(ActorsMoviesFragment.newInstance(actorId), "Movies")
        adapter.addFragment(ActorsTvShowsFragment.newInstance(actorId), "TVShows")
        moviesViewPager.offscreenPageLimit = 1
        moviesViewPager.adapter = adapter
        moviesTab.setupWithViewPager(moviesViewPager)
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