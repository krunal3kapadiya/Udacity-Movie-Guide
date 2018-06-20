package com.krunal3kapadiya.popularmovies.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Krunal on 8/5/2017.
 */

public class TrailerResponse {
    @SerializedName("results")
    private ArrayList<Trailer> trailerArrayList;

    public ArrayList<Trailer> getTrailerArrayList() {
        return trailerArrayList;
    }

    public void setTrailerArrayList(ArrayList<Trailer> trailerArrayList) {
        this.trailerArrayList = trailerArrayList;
    }
}
