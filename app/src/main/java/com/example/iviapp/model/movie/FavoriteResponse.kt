package com.example.iviapp.model.movie

import com.google.gson.annotations.SerializedName

data class FavoriteResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("favorite")
    val favorite: Boolean,
    @SerializedName("rated")
    val rated: Object,
    @SerializedName("watchlist")
    val watchlist: Boolean
)