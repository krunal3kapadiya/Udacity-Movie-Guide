package com.krunal3kapadiya.popularmovies.favourites

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.krunal3kapadiya.popularmovies.R
import com.krunal3kapadiya.popularmovies.dashBoard.TabFragmentAdapter


class FavouriteActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, FavouriteActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val adapter = TabFragmentAdapter(supportFragmentManager)
        adapter.addFragment(GetMoviesListFragment.newInstance(0), "Movies")
        adapter.addFragment(GetMoviesListFragment.newInstance(1), "TV Shows")
        adapter.addFragment(GetMoviesListFragment.newInstance(2), "Actors")
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = adapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
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