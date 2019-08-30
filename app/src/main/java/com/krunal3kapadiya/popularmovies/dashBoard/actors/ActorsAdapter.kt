package com.krunal3kapadiya.popularmovies.dashBoard.actors

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.graphics.Palette
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.krunal3kapadiya.popularmovies.Constants
import com.krunal3kapadiya.popularmovies.R
import kotlinx.android.synthetic.main.row_movies.view.*

class ActorsAdapter(listener: OnActorClickListener) : RecyclerView.Adapter<ActorsAdapter.ViewHolder>() {
    private var actorsList: ArrayList<Result>? = ArrayList()
    val clickListener = listener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(
                R.layout.row_movies,
                parent,
                false
        ))
    }

    override fun getItemCount(): Int {
        var size = 0
        when (actorsList != null) {
            true -> {
                size = actorsList?.size!!
            }
        }
        return size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        actorsList?.get(position)?.let { holder.bind(it) }

        holder.itemView.setOnClickListener {
            actorsList?.get(position)?.let { it1 -> clickListener.onActorClick(it1) }
        }

    }

    fun setData(actorsResults: List<Result>?) {
        actorsList?.clear()
        if (actorsResults != null) {
            actorsList?.addAll(actorsResults)
        }
        notifyDataSetChanged()
    }

    fun addData(it: List<Result>?) {
        if (it != null) {
            actorsList?.addAll(it)
        }
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(result: Result) {
            Glide.with(itemView.context)
                    .asBitmap()
                    .load(Constants.BASE_IMAGE_URL + Constants.POSTER_SIZE_500 + result.profilePath)
                    .placeholder(R.mipmap.ic_movie)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap>?) {
                            itemView.img_movie_row.setImageBitmap(bitmap)
                            val palette = Palette.from(bitmap).generate()
                            val themeLightColor = palette.getDominantColor(ContextCompat.getColor(itemView.context!!, R.color.colorAccent))
                            val themeDarkColor = palette.getDarkVibrantColor(ContextCompat.getColor(itemView.context!!, R.color.colorAccent))
                            itemView.tv_movie_title.setBackgroundColor(themeLightColor)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                        }
                    })
            itemView.tv_movie_title.text = result.name

        }
    }

    interface OnActorClickListener {
        fun onActorClick(result: Result)
    }
}