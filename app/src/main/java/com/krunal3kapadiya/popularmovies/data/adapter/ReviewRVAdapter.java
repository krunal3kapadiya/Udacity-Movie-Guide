package com.krunal3kapadiya.popularmovies.data.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.krunal3kapadiya.popularmovies.R;
import com.krunal3kapadiya.popularmovies.data.model.Reviews;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Krunal on 8/5/2017.
 */

public class ReviewRVAdapter extends RecyclerView.Adapter<ReviewRVAdapter.ViewHolder> {

    private ArrayList<Reviews> mReviewList;
    private OnReviewItemClick onReviewItemClick;
    private int mThemeColor;

    public ReviewRVAdapter(Context context, ArrayList<Reviews> reviewList, int themeColor) {
        this.mReviewList = reviewList;
        onReviewItemClick = (OnReviewItemClick) context;
        mThemeColor = themeColor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_reviews, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            holder.mContent.setText(Html.fromHtml(mReviewList.get(position).getContent(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            holder.mContent.setText(Html.fromHtml(mReviewList.get(position).getContent()));
        }

        if (position == mReviewList.size() - 1) {
            holder.rowView.setVisibility(View.GONE);
        } else {
            holder.rowView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mReviewList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_row_review_content)
        TextView mContent;

        @BindView(R.id.row_view)
        View rowView;

        @OnClick(R.id.txt_row_review_content)
        void onClick() {
            onReviewItemClick.onReviewItemClick(getAdapterPosition());
        }

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            rowView.setBackgroundColor(mThemeColor);
        }
    }

    public interface OnReviewItemClick {
        void onReviewItemClick(int position);
    }
}
