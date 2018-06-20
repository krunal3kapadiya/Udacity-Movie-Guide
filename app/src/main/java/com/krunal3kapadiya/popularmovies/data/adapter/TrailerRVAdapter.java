package com.krunal3kapadiya.popularmovies.data.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.krunal3kapadiya.popularmovies.R;
import com.krunal3kapadiya.popularmovies.data.model.Trailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Krunal on 8/5/2017.
 */

public class TrailerRVAdapter extends RecyclerView.Adapter<TrailerRVAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Trailer> mTrailerList;
    private OnItemClick onItemClick;

    public TrailerRVAdapter(Context context, ArrayList<Trailer> trailerList) {
        mContext = context;
        mTrailerList = trailerList;
        onItemClick = (OnItemClick) context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_trailer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //TODO add string here
        Picasso.with(mContext)
                .load(mContext.getString(R.string.youtube_image, mTrailerList.get(position).getKey()))
                .into(holder.mThumb);
    }

    @Override
    public int getItemCount() {
        return mTrailerList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.row_trailer_thumb)
        ImageView mThumb;

        @OnClick(R.id.row_trailer_thumb)
        void onClick() {
            onItemClick.onItemClick(getAdapterPosition());
        }

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClick {
        void onItemClick(int pos);
    }
}
