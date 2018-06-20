package com.krunal3kapadiya.popularmovies.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Krunal on 8/5/2017.
 */

public class ReviewsResponse {
    @SerializedName("id")
    private long movieId;
    @SerializedName("page")
    private int page;
    @SerializedName("results")
    private ArrayList<Reviews> reviewsArrayList;
    @SerializedName("total_pages")
    private int totalPages;

    public ArrayList<Reviews> getReviewsArrayList() {
        return reviewsArrayList;
    }

    public void setReviewsArrayList(ArrayList<Reviews> reviewsArrayList) {
        this.reviewsArrayList = reviewsArrayList;
    }
}
