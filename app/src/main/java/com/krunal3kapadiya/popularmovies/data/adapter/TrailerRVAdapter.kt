package com.krunal3kapadiya.popularmovies.data.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.krunal3kapadiya.popularmovies.R
import com.krunal3kapadiya.popularmovies.data.model.Trailer
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_trailer.view.*
import java.util.*

class TrailerRVAdapter(private val mContext: Context, private val mTrailerList: ArrayList<Trailer>) : RecyclerView.Adapter<TrailerRVAdapter.ViewHolder>() {
    private val onItemClick: OnItemClick

    init {
        onItemClick = mContext as OnItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_trailer, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mTrailerList)
    }

    override fun getItemCount(): Int {
        return mTrailerList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(mTrailerList: ArrayList<Trailer>) {
            with(mTrailerList) {
                Picasso.with(mContext)
                        .load(mContext.getString(R.string.youtube_image, mTrailerList[position].key))
                        .into(itemView.row_trailer_thumb)
            }

            itemView.row_trailer_thumb.setOnClickListener({ onItemClick.onItemClick(adapterPosition) })
        }
    }

    interface OnItemClick {
        fun onItemClick(pos: Int)
    }
}
