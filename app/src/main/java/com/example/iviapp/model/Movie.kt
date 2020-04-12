package com.example.iviapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "movie_table")
data class Movie(
    @PrimaryKey
    @SerializedName("id")
    val id: Int,
    @SerializedName("poster_path")
    private var posterPath: String,
    @SerializedName("adult")
    var adult: Boolean,
    @SerializedName("overview")
    var overview: String,
    @SerializedName("release_date")
    var releaseDate: String,
    @SerializedName("original_title")
    var originalTitle: String,
    @SerializedName("original_language")
    var originalLanguage: String,
    @SerializedName("title")
    var title: String,
    @SerializedName("backdrop_path")
    private var backdropPath: String,
    @SerializedName("popularity")
    var popularity: Double,
    @SerializedName("vote_count")
    var voteCount: Int,
    @SerializedName("video")
    var video: Boolean,
    @SerializedName("vote_average")
    var voteAverage: Double,
    var isFavorite: Boolean
) :Serializable {

    fun getPosterPath(): String {
        return "https://image.tmdb.org/t/p/w500$posterPath"
    }

    fun getBackdropPath(): String {
        return "https://image.tmdb.org/t/p/w500$backdropPath"
    }

}