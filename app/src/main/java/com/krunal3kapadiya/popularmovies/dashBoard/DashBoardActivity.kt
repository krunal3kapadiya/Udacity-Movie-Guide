package com.krunal3kapadiya.popularmovies.dashBoard

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.krunal3kapadiya.popularmovies.R
import com.krunal3kapadiya.popularmovies.SearchActivity
import com.krunal3kapadiya.popularmovies.about.AboutUsActivity
import com.krunal3kapadiya.popularmovies.dashBoard.actors.ActorsFragment
import com.krunal3kapadiya.popularmovies.dashBoard.movies.MoviesFragment
import com.krunal3kapadiya.popularmovies.dashBoard.tvShows.TvShowsFragment
import com.krunal3kapadiya.popularmovies.genres.GenresActivity
import com.krunal3kapadiya.popularmovies.login.LoginActivity
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*

class DashBoardActivity : AppCompatActivity(), SearchView.OnQueryTextListener, NavigationView.OnNavigationItemSelectedListener {

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


        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        tab_layout.addTab(tab_layout.newTab().setText(getString(R.string.title_movies_fragment)), 0, true)
        tab_layout.addTab(tab_layout.newTab().setText(getString(R.string.title_tv_fragment)), 1, false)
        tab_layout.addTab(tab_layout.newTab().setText(getString(R.string.title_actors_fragment)), 2, false)

        supportFragmentManager.beginTransaction().replace(R.id.frame, MoviesFragment.newInstance()).commit()

        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                var fragment: Fragment = MoviesFragment.newInstance()

                when (tab?.position) {
                    0 -> {
                        fragment = MoviesFragment.newInstance()
                    }
                    1 -> {
                        fragment = TvShowsFragment.newInstance()
                    }
                    2 -> {
                        fragment = ActorsFragment.newInstance()
                    }
                }
                supportFragmentManager.beginTransaction().replace(R.id.frame, fragment).commit()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchMenuItem = menu.findItem(R.id.action_search)
        searchView = searchMenuItem.actionView as SearchView
        searchView!!.setOnQueryTextListener(this)
        searchView!!.queryHint = "Search"
        return true
    }


    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let { SearchActivity.launch(this, it) }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_watchlist -> {
                // Handle the camera action
                Toast.makeText(this, "Coming Soon", Toast.LENGTH_LONG).show()
            }
            R.id.nav_favourites -> {
                Toast.makeText(this, "Coming Soon", Toast.LENGTH_LONG).show()
            }
            R.id.nav_rated -> {
                Toast.makeText(this, "Coming Soon", Toast.LENGTH_LONG).show()
            }
            R.id.nav_login -> {
                LoginActivity.launch(this)
            }
            R.id.nav_discover_movies -> {
                Toast.makeText(this, "Coming Soon", Toast.LENGTH_LONG).show()
            }
            R.id.nav_discover_tv_shows -> {
                Toast.makeText(this, "Coming Soon", Toast.LENGTH_LONG).show()
            }
            R.id.nav_genre -> {
                GenresActivity.launch(this)
            }
            R.id.nav_favourites_device -> {
                Toast.makeText(this, "Coming Soon", Toast.LENGTH_LONG).show()
            }
            R.id.nav_about -> {
                AboutUsActivity.launch(this)
            }
            R.id.nav_share -> {
                Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(
                            Intent.EXTRA_TEXT,
                            "I like this Popular Movie app. Download this app at below link \n http://play.google.com/store/apps/details?id=com.krunal3kapadiya.popularmovies \n also share your awesome feedback."
                    )
                    type = "text/plain"

                }.also { sendIntent -> startActivity(sendIntent) }
            }
            R.id.nav_rate -> {
                val uri = Uri.parse("market://details?id=" + this.packageName)
                Intent(Intent.ACTION_VIEW, uri)
                        .apply {
                            addFlags(
                                    Intent.FLAG_ACTIVITY_NO_HISTORY or
                                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK
                            ).also {
                                startActivity(it)
                            }
                        }
            }
            R.id.nav_apps -> {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(
                            "https://play.google.com/store/apps/dev?id=6554839555682225234")
                    setPackage("com.android.vending")
                }
                startActivity(intent)
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}