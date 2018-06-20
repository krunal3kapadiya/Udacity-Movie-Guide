package com.krunal3kapadiya.popularmovies.data.model;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Krunal on 8/5/2017.
 */

public class Reviews {
    @SerializedName("id")
    private String id;
    @SerializedName("auther")
    private String auther;
    @SerializedName("content")
    private String content;
    @SerializedName("url")
    private String reviewURL;

    public String getReviewURL() {
        return reviewURL;
    }

    public void setReviewURL(String reviewURL) {
        this.reviewURL = reviewURL;
    }

    public String getAuther() {
        return auther;
    }

    public void setAuther(String auther) {
        this.auther = auther;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    protected Reviews(Parcel in) {
        id = in.readString();
        auther = in.readString();
        content = in.readString();
        reviewURL = in.readString();
    }
}
