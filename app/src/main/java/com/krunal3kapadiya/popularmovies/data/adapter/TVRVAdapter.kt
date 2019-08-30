package com.krunal3kapadiya.popularmovies.data.adapter

import android.content.Context
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
import com.krunal3kapadiya.popularmovies.data.model.Movies
import com.krunal3kapadiya.popularmovies.data.model.Result
import kotlinx.android.synthetic.main.row_movies.view.*

class TVRVAdapter(
        private val mContext: Context,
        private val listener: OnItemClick
) : RecyclerView.Adapter<TVRVAdapter.ViewHolder>() {
    private val mOnItemClick: OnItemClick
    private val context: Context
    private val mMovieArrayList: ArrayList<Result> = ArrayList()

    init {
        context = mContext
        mOnItemClick = listener
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


    fun setData(it: List<Result>?) {
        mMovieArrayList.clear()
        if (it != null) {
            mMovieArrayList.addAll(it)
        }
        notifyDataSetChanged()
    }

    fun addData(it: List<Result>?) {
        if (it != null) {
            mMovieArrayList.addAll(it)
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(mMovieArrayList: List<Result>) {
            with(mMovieArrayList) {
                Glide.with(itemView.context)
                        .asBitmap()
                        .load(Constants.BASE_IMAGE_URL + Constants.POSTER_SIZE_500 + mMovieArrayList[position].posterPath)
                        .placeholder(R.mipmap.ic_movie)

                        .into(object : CustomTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                itemView.img_movie_row.setImageBitmap(resource)
                                val palette = Palette.from(resource).generate()
                                val themeLightColor = palette.getDominantColor(ContextCompat.getColor(itemView.context!!, R.color.colorAccent))
                                val themeDarkColor = palette.getDarkVibrantColor(ContextCompat.getColor(itemView.context!!, R.color.colorAccent))
                                itemView.tv_movie_title.setBackgroundColor(themeLightColor)

                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                            }
                        })
                itemView.tv_movie_title.text = mMovieArrayList[adapterPosition].name
                itemView.img_movie_row.setOnClickListener { mOnItemClick.onItemClick(adapterPosition, itemView.img_movie_row, mMovieArrayList[adapterPosition]) }
            }
        }
    }

    interface OnItemClick {
        fun onItemClick(pos: Int, view: ImageView?, movies: Result)
    }
}
