package com.krunal3kapadiya.popularmovies.data.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.krunal3kapadiya.popularmovies.R
import com.krunal3kapadiya.popularmovies.data.model.Reviews
import kotlinx.android.synthetic.main.row_reviews.view.*
import java.util.*

class ReviewRVAdapter(context: Context, private val mReviewList: ArrayList<Reviews>, private val mThemeColor: Int) : RecyclerView.Adapter<ReviewRVAdapter.ViewHolder>() {
    private val onReviewItemClick: OnReviewItemClick

    init {
        onReviewItemClick = context as OnReviewItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_reviews, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mReviewList)
    }

    override fun getItemCount(): Int {
        return mReviewList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(mReviewList: ArrayList<Reviews>) {
            with(mReviewList) {
                itemView.row_view!!.setBackgroundColor(mThemeColor)

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    itemView.txt_row_review_content!!.text = Html.fromHtml(mReviewList[adapterPosition].content, Html.FROM_HTML_MODE_LEGACY)
                } else {
                    itemView.txt_row_review_content!!.text = Html.fromHtml(mReviewList[adapterPosition].content)
                }

                if (adapterPosition == mReviewList.size - 1) {
                    itemView.row_view!!.visibility = View.GONE
                } else {
                    itemView.row_view!!.visibility = View.VISIBLE
                }

                itemView.txt_row_review_content.setOnClickListener({ onReviewItemClick.onReviewItemClick(adapterPosition) })
            }
        }
    }

    interface OnReviewItemClick {
        fun onReviewItemClick(position: Int)
    }
}
