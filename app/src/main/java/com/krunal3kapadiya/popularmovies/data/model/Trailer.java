package com.krunal3kapadiya.popularmovies.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Krunal on 8/5/2017.
 */

public class Trailer {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("key")
    private String key;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
