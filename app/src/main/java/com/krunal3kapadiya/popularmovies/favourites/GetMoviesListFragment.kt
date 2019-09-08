package com.krunal3kapadiya.popularmovies.favourites

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.krunal3kapadiya.popularmovies.*
import com.krunal3kapadiya.popularmovies.dashBoard.actors.ActorsAdapter
import com.krunal3kapadiya.popularmovies.dashBoard.actors.ActorsDetailActivity
import com.krunal3kapadiya.popularmovies.dashBoard.movies.MovieDetailActivity
import com.krunal3kapadiya.popularmovies.dashBoard.tvShows.TVDetailActivity
import com.krunal3kapadiya.popularmovies.data.adapter.MovieRVAdapter
import com.krunal3kapadiya.popularmovies.data.adapter.TVRVAdapter
import com.krunal3kapadiya.popularmovies.data.model.Movies
import com.krunal3kapadiya.popularmovies.data.model.TvResult
import kotlinx.android.synthetic.main.fragment_now_playing.*

class GetMoviesListFragment : Fragment() {

    companion object {
        fun newInstance(pos: Int) =
                GetMoviesListFragment().apply {
                    arguments = Bundle().apply {
                        putInt("position", pos)
                    }
                }
    }

    private lateinit var viewModel: FavouriteViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_now_playing, container, false)
    }

    var position: Int = 0
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let {
            position = it.getInt("position")
        }

        val viewModelFactory = activity?.let { Injection.provideFavouriteViewModel(it) }
        viewModel = ViewModelProviders.of(this,
                viewModelFactory).get(FavouriteViewModel::class.java)

        val mAdapter = MovieRVAdapter(listener = object : MovieRVAdapter.OnItemClick {
            override fun onItemClick(
                    pos: Int,
                    view: ImageView?,
                    movies: Movies,
                    themeDarkColor: Int,
                    themeLightColor: Int
            ) {
                MovieDetailActivity.launch(
                        context = activity!!,
                        movies = movies,
                        themeDarkColor = themeDarkColor,
                        themeLightColor = themeLightColor)
            }
        })


        val tvAdapter =
                TVRVAdapter(listener = object : TVRVAdapter.OnItemClick {
                    override fun onItemClick(
                            pos: Int,
                            view: ImageView?,
                            movies: TvResult,
                            darkColor: Int,
                            lightColor: Int
                    ) {
                        TVDetailActivity.launch(
                                context = activity!!,
                                tvResult = movies,
                                lightColor = darkColor,
                                darkColor = lightColor
                        )

                    }
                })

        val mActorAdapter = ActorsAdapter(object : ActorsAdapter.OnActorClickListener {
            override fun onActorClick(result: com.krunal3kapadiya.popularmovies.data.model.actors.Result) {
                activity?.let { ActorsDetailActivity.launch(it, result.id) }
            }
        })

        val layoutManager = GridLayoutManager(activity, 3)
        rv_list_movie_main.layoutManager = layoutManager

        when (position) {
            0 -> {
                rv_list_movie_main.adapter = mAdapter
                loadDataSource(position, mAdapter)
            }
            1 -> {
                rv_list_movie_main.adapter = tvAdapter
                tvAdapter?.let { loadDataSource(position, it) }
            }
            2 -> {
                rv_list_movie_main.adapter = mActorAdapter
                loadDataSource(position, mActorAdapter)
            }
        }
    }

    fun loadDataSource(tabPosition: Int, adapter: Any) {
        when (tabPosition) {
            0 -> viewModel.getAllMovies().observe(this, Observer
            {
                (adapter as MovieRVAdapter).setData(it as ArrayList<Movies>?)
            })
            1 -> {
//                viewModel.getAllTvShows().observe(this, Observer {
//                    (adapter as TVRVAdapter).addData(it)
//                })
            }
            2 -> {
//                viewModel.getAllActors().observe(this, Observer {
//                    (adapter as ActorsAdapter).addData(it)
//                })
            }
        }

    }
}
