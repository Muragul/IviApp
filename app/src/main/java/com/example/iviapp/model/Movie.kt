package com.example.iviapp.model

import com.google.gson.annotations.SerializedName
import retrofit2.Call

class Movie {
    @SerializedName("poster_path")
    private var posterPath: String? = null

    @SerializedName("adult")
    var adult = false

    @SerializedName("overview")
    var overview: String? = null

    @SerializedName("release_date")
    var releaseDate: String? = null

    @SerializedName("genre_ids")
    var genreIds: List<Int> = ArrayList()

    @SerializedName("id")
    var id: Int? = null

    @SerializedName("original_title")
    var originalTitle: String? = null

    @SerializedName("original_language")
    var originalLanguage: String? = null

    @SerializedName("title")
    private var title: String? = null

    @SerializedName("backdrop_path")
    private var backdropPath: String? = null

    @SerializedName("popularity")
    var popularity: Double? = null

    @SerializedName("vote_count")
    private var voteCount: Int? = null

    @SerializedName("video")
    private var video: Boolean? = null

    @SerializedName("vote_average")
    var voteAverage: Double? = null


    private var baseImageUrl: String = "https://image.tmdb.org/t/p/w500"

    fun getPosterPath():String {
        return baseImageUrl + posterPath
    }
    fun getBackdropPath():String{
        return baseImageUrl + backdropPath
    }

}