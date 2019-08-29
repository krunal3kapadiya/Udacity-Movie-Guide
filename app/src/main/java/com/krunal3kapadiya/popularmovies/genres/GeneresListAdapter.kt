package com.krunal3kapadiya.popularmovies.genres

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.krunal3kapadiya.popularmovies.R
import kotlinx.android.synthetic.main.row_generes.view.*

/**
 * @author krunal kapadiya
 * @link https://krunal3kapadiya.app
 * @date 14,April,2019
 */

class GeneresListAdapter(private val listener: OnItemClick) : RecyclerView.Adapter<GeneresListAdapter.ViewHolder>() {
    val mOnItemClick: OnItemClick = listener
    private val mGenresArrayList: ArrayList<Genres> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_generes,
                parent,
                false))
    }

    override fun getItemCount(): Int {
        return mGenresArrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mGenresArrayList)
    }

    fun setData(it: List<Genres>) {
        mGenresArrayList.clear()
        for (genres in it) {
            mGenresArrayList.add(genres)
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(mGenresArrayList: List<Genres>) {
            itemView.row_generes_title.text = mGenresArrayList[adapterPosition].name
            itemView.setOnClickListener { mOnItemClick.onItemClick(mGenresArrayList[adapterPosition]) }
        }
    }

    interface OnItemClick {
        fun onItemClick(genres: Genres)
    }

}