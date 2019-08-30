package com.krunal3kapadiya.popularmovies.data.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.krunal3kapadiya.popularmovies.Constants
import com.krunal3kapadiya.popularmovies.R
import com.krunal3kapadiya.popularmovies.data.model.Cast
import kotlinx.android.synthetic.main.row_casts.view.*

/**
 * @author krunal kapadiya
 * @link https://krunal3kapadiya.app
 * @date 14,April,2019
 */

class ActorsListAdapter(
        listener: OnItemClick
) : RecyclerView.Adapter<ActorsListAdapter.ViewHolder>() {
    private val mOnItemClick: OnItemClick = listener
    private val mMovieArrayList: ArrayList<Cast> = ArrayList()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(
                        R.layout.row_casts,
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
    }

    override fun getItemCount(): Int {
        return mMovieArrayList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    fun setData(it: List<Cast>?) {
        mMovieArrayList.clear()
        if (it != null) {
            for (cast in it) {
                mMovieArrayList.add(cast)
            }
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(mMovieArrayList: List<Cast>) {
            with(mMovieArrayList) {
                Glide.with(itemView.context)
                        .load(Constants.BASE_IMAGE_URL + Constants.POSTER_SIZE_500 +
                                mMovieArrayList[adapterPosition].profile_path)
                        .apply(RequestOptions.circleCropTransform())
                        .into(itemView.row_cast_img)
                itemView.row_cast_img.setOnClickListener {
                    mOnItemClick.onItemClick(adapterPosition,
                            mMovieArrayList[adapterPosition])
                }
            }
        }
    }

    interface OnItemClick {
        fun onItemClick(pos: Int, cast: Cast)
    }
}
