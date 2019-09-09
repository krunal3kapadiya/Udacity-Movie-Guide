package com.krunal3kapadiya.popularmovies.data.adapter

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.graphics.Palette
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.krunal3kapadiya.popularmovies.Constants
import com.krunal3kapadiya.popularmovies.R
import com.krunal3kapadiya.popularmovies.data.OnItemClick
import com.krunal3kapadiya.popularmovies.data.model.TvResult
import kotlinx.android.synthetic.main.row_movies.view.*

class TVRVAdapter(
        listener: OnItemClick
) : RecyclerView.Adapter<TVRVAdapter.ViewHolder>() {
    private val mOnItemClick: OnItemClick = listener
    private val mMovieArrayList: ArrayList<TvResult> = ArrayList()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(
                        R.layout.row_movies,
                        parent,
                        false
                )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
            holder: ViewHolder,
            position: Int
    ) {
        holder.bind(mMovieArrayList)
//        ViewCompat.setTransitionName(holder.itemView, mMovieArrayList[position].name)
//        setFadeAnimation(holder.itemView)
    }

    /*private fun setFadeAnimation(view: View) {
        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 1000
        view.startAnimation(anim)
    }*/


    override fun getItemCount(): Int {
        return mMovieArrayList.size
    }

    /*override fun getItemId(position: Int): Long {
            return position
    }*/

    override fun getItemViewType(position: Int): Int {
        return position
    }


    fun setData(it: List<TvResult>?) {
        mMovieArrayList.clear()
        if (it != null) {
            mMovieArrayList.addAll(it)
        }
        notifyDataSetChanged()
    }

    fun addData(it: List<TvResult>?) {
        if (it != null) {
            mMovieArrayList.addAll(it)
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var themeLightColor = 0
        var themeDarkColor = 0
        fun bind(mMovieArrayList: List<TvResult>) {
            with(mMovieArrayList) {
                Glide.with(itemView.context)
                        .asBitmap()
                        .load(Constants.BASE_IMAGE_URL + Constants.POSTER_SIZE_500 + mMovieArrayList[position].posterPath)
                        .placeholder(R.mipmap.ic_movie)

                        .into(object : CustomTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                itemView.img_movie_row.setImageBitmap(resource)
                                val palette = Palette.from(resource).generate()
                                themeLightColor = palette.getDominantColor(ContextCompat.getColor(itemView.context!!, R.color.colorAccent))
                                themeDarkColor = palette.getDarkVibrantColor(ContextCompat.getColor(itemView.context!!, R.color.colorAccent))
                                itemView.tv_movie_title.setBackgroundColor(themeLightColor)

                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                            }
                        })
                itemView.tv_movie_title.text = mMovieArrayList[adapterPosition].name
                itemView.img_movie_row.setOnClickListener {
                    mOnItemClick.onItemClick(
                            adapterPosition,
                            itemView.img_movie_row,
                            itemView.tv_movie_title.text.toString(),
                            mMovieArrayList[adapterPosition].id,
                            themeDarkColor,
                            themeLightColor
                    )
                }
            }
        }
    }
}
