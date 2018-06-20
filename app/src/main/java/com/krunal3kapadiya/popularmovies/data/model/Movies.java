package com.krunal3kapadiya.popularmovies.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Krunal on 7/26/2017.
 */

public class Movies implements Parcelable {

    @SerializedName("id")
    private int mId;
    @SerializedName("original_title")
    private String mName;
    @SerializedName("poster_path")
    private String mUrl;
    @SerializedName("overview")
    private String mOverView;
    @SerializedName("vote_average")
    private double mRating;
    @SerializedName("release_date")
    private String mReleaseDate;
    @SerializedName("backdrop_path")
    private String mBackDropPath;


    public Movies(int id, String original_title, String poster_path, String backDropPath, String overview, double vote_average, String release_date) {
        mId = id;
        mName = original_title;
        mBackDropPath = backDropPath;
        mUrl = poster_path;
        mOverView = overview;
        mRating = (int) vote_average;
        mReleaseDate = release_date;
    }

    private Movies(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mBackDropPath = in.readString();
        mUrl = in.readString();
        mRating = in.readInt();
        mReleaseDate = in.readString();
        mOverView = in.readString();
        mBackDropPath = in.readString();
    }

    public static final Creator<Movies> CREATOR = new Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel in) {
            return new Movies(in);
        }

        @Override
        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.mReleaseDate = releaseDate;
    }

    public String getOverView() {
        return mOverView;
    }

    public void setOverView(String overView) {
        mOverView = overView;
    }

    public int getRating() {
        return (int) mRating;
    }

    public void setRating(int rating) {
        this.mRating = rating;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mId);
        parcel.writeString(mName);
        parcel.writeString(mBackDropPath);
        parcel.writeString(mUrl);
        parcel.writeInt((int) mRating);
        parcel.writeString(mReleaseDate);
        parcel.writeString(mOverView);
        parcel.writeString(mBackDropPath);
    }

    public String getBackDropPath() {

        return mBackDropPath;
    }

    public void setBackDropPath(String backDropPath) {
        mBackDropPath = backDropPath;
    }
}
