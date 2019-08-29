package com.krunal3kapadiya.popularmovies.genres

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import com.krunal3kapadiya.popularmovies.R
import kotlinx.android.synthetic.main.activity_genres.*

class GenresActivity : AppCompatActivity() {
    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, GenresActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genres)
        val viewModel = ViewModelProviders.of(this).get(GenresViewModel::class.java)
        val adapter = GeneresListAdapter(object : GeneresListAdapter.OnItemClick {
            override fun onItemClick(genres: Genres) {
                GenreDetailActivity.launch(this@GenresActivity, genres)
            }
        })
        listGenres.layoutManager = GridLayoutManager(this, 2)
        listGenres.adapter = adapter
        viewModel.getGenresList().observe(this, Observer {
            it?.genres?.let { it1 -> adapter.setData(it1) }
        })
    }
}