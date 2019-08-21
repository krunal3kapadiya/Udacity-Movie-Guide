package com.krunal3kapadiya.popularmovies

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

class SearchActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context, searchString: String) {
            val intent = Intent(context, SearchActivity::class.java)
            intent.putExtra("searchString", searchString)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        viewModel.searchMovies(intent.getStringExtra("searchString"))
                .observe(this, Observer {
                    Toast.makeText(this, "Title is " + it?.title, Toast.LENGTH_LONG).show()
                })
    }
}