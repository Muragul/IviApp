package com.example.iviapp.model

import com.google.gson.annotations.SerializedName

class MoviesResponse {
    @SerializedName("page")
    private val page = 0

    @SerializedName("results")
    private val results: List<Movie>? = null

    @SerializedName("total_results")
    private val totalResults = 0

    @SerializedName("total_pages")
    private val totalPages = 0

    fun getResults(): List<Movie?>? {
        return results
    }
}