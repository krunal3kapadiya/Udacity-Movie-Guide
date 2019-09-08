package com.krunal3kapadiya.popularmovies.dashBoard.actors

import com.google.gson.annotations.SerializedName

data class ActorsResponse(@SerializedName("page")
                          val page: Int,
                          @SerializedName("total_results")
                          val totalResults: Int,
                          @SerializedName("total_pages")
                          val totalPages: Int,
                          @SerializedName("results")
                          val result: List<com.krunal3kapadiya.popularmovies.data.model.actors.Result>)

