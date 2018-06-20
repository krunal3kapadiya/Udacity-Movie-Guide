package com.krunal3kapadiya.popularmovies.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Krunal on 8/5/2017.
 */

public class MovieResponse {
    @SerializedName("results")
    private ArrayList<Movies> results;

    public ArrayList<Movies> getResults() {
        return results;
    }

    public void setResults(ArrayList<Movies> results) {
        this.results = results;
    }
}
