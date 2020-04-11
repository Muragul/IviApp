package com.example.iviapp

import com.example.iviapp.model.MoviesResponse
import com.google.gson.JsonObject

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

object RetrofitService {

    private const val BASE_URL = "https://api.themoviedb.org/3/"

    fun getPostApi(): PostApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(PostApi::class.java)
    }
}

interface PostApi {

    @GET("movie/popular")
    fun getPopularMovieList(
        @Query("api_key") apiKey: String
    ): Call<MoviesResponse>

    @GET("movie/{movie_id}")
    fun getMovie(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Call<JsonObject>

    @GET("account/{account_id}/favorite/movies")
    fun getFavorites(
        @Path("account_id") id: Int,
        @Query("api_key") apiKey: String,
        @Query("session_id") sessionId: String
    ): Call<MoviesResponse>

    @POST("account/{account_id}/favorite")
    fun rate(
        @Path("account_id") accountId: Int?,
        @Query("api_key") apiKey: String,
        @Query("session_id") sessionId: String?,
        @Body body: JsonObject
    ): Call<JsonObject>

    @GET("movie/{movie_id}/account_states")
    fun hasLike(
        @Path("movie_id") movieId: Int?,
        @Query("api_key") apiKey: String,
        @Query("session_id") sessionId: String?
    ): Call<JsonObject>

    @GET("authentication/token/new")
    fun getToken(
        @Query("api_key") apiKey: String
    ): Call<JsonObject>

    @POST("authentication/token/validate_with_login")
    fun logIn(
        @Query("api_key") apiKey: String,
        @Body body: JsonObject
    ): Call<JsonObject>

    @POST("authentication/session/new")
    fun getSession(
        @Query("api_key") apiKey: String,
        @Body body: JsonObject
    ): Call<JsonObject>

    @HTTP(
        method = "DELETE",
        path = "authentication/session",
        hasBody = true
    )
    fun deleteSession(
        @Query("api_key") apiKey: String,
        @Body body: JsonObject
    ): Call<JsonObject>

    @GET("account")
    fun getAccount(
        @Query("api_key") apiKey: String,
        @Query("session_id") sessionId: String
    ): Call<JsonObject>


}
