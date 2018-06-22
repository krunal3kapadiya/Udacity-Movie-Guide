package com.krunal3kapadiya.popularmovies.dashBoard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import com.krunal3kapadiya.popularmovies.MovieDetailActivity
import com.krunal3kapadiya.popularmovies.R
import com.krunal3kapadiya.popularmovies.dashBoard.actors.ActorsFragment
import com.krunal3kapadiya.popularmovies.dashBoard.movies.MoviesFragment
import com.krunal3kapadiya.popularmovies.dashBoard.tvShows.TvShowsFragment
import com.krunal3kapadiya.popularmovies.data.adapter.MovieRVAdapter
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashBoardActivity : AppCompatActivity(), MovieRVAdapter.OnItemClick, SearchView.OnQueryTextListener {
    override fun onItemClick(pos: Int, view: ImageView?) {

    }

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, DashBoardActivity::class.java)
            context.startActivity(intent)
        }
    }

    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val adapter = TabFragmentAdapter(supportFragmentManager)

        adapter.addFragment(MoviesFragment.newInstance(), getString(R.string.title_movies_fragment))
        adapter.addFragment(TvShowsFragment.newInstance(), getString(R.string.title_tv_fragment))
        adapter.addFragment(ActorsFragment.newInstance(), getString(R.string.title_actors_fragment))

        viewPager.adapter = adapter

        tab_layout.setupWithViewPager(viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchMenuItem = menu.findItem(R.id.action_search)
        searchView = searchMenuItem.actionView as SearchView
        searchView!!.setOnQueryTextListener(this)
        searchView!!.queryHint = "Search"
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }
}