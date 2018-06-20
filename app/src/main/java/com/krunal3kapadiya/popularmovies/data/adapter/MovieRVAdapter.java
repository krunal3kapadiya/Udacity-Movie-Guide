package com.krunal3kapadiya.popularmovies.data.adapter;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.krunal3kapadiya.popularmovies.Constants;
import com.krunal3kapadiya.popularmovies.R;
import com.krunal3kapadiya.popularmovies.data.model.Movies;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Krunal on 7/26/2017.
 */

public class MovieRVAdapter extends RecyclerView.Adapter<MovieRVAdapter.ViewHolder> {

    private Context mContext;
    private List<Movies> mMovieArrayList;
    private OnItemClick mOnItemClick;

    public MovieRVAdapter(Context context, List<Movies> moviesArrayList) {
        mContext = context;
        mMovieArrayList = moviesArrayList;
        mOnItemClick = (OnItemClick) context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_movies, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.with(mContext)
                .load(Constants.BASE_IMAGE_URL + Constants.POSTER_SIZE + mMovieArrayList.get(position).getUrl())
                .placeholder(R.mipmap.ic_movie)
                .into(holder.mMovieImage);

        ViewCompat.setTransitionName(holder.itemView, mMovieArrayList.get(position).getName());

        setFadeAnimation(holder.itemView);
    }

    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000);
        view.startAnimation(anim);
    }


    @Override
    public int getItemCount() {
        return mMovieArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_movie_row)
        ImageView mMovieImage;

        @OnClick(R.id.img_movie_row)
        void onClick() {
            mOnItemClick.onItemClick(getAdapterPosition(), mMovieImage);
        }

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClick {
        void onItemClick(int pos, ImageView view);
    }
}
