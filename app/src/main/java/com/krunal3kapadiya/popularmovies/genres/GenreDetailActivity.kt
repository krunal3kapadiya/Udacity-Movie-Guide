package com.krunal3kapadiya.popularmovies.genres

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.MenuItem
import android.widget.ImageView
import com.krunal3kapadiya.popularmovies.MovieDetailActivity
import com.krunal3kapadiya.popularmovies.R
import com.krunal3kapadiya.popularmovies.data.adapter.MovieRVAdapter
import com.krunal3kapadiya.popularmovies.data.model.Movies
import kotlinx.android.synthetic.main.activity_genere_detail.*

class GenreDetailActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context, genres: Genres) {
            val intent = Intent(context, GenreDetailActivity::class.java)
            intent.putExtra("genres", genres)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genere_detail)
        supportActionBar?.elevation = 0F
        val genres = intent.getParcelableExtra<Genres>("genres")
        supportActionBar?.title = genres.name
        val viewModel = ViewModelProviders.of(this).get(GenresViewModel::class.java)
        val adapter = MovieRVAdapter(object : MovieRVAdapter.OnItemClick {
            override fun onItemClick(pos: Int, view: ImageView?, movies: Movies) {
                val intent = Intent(this@GenreDetailActivity, MovieDetailActivity::class.java)
                intent.putExtra(MovieDetailActivity.ARG_MOVIE, movies)
                startActivity(intent)
            }
        })
        generes_detail_screen.layoutManager = GridLayoutManager(this, 2)
        generes_detail_screen.adapter = adapter
        viewModel.getGenresById(genres.id).observe(this, Observer {
            adapter.setData(it)
        })
        //TODO make API call for genres and get the list
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
