package com.example.iviapp.model.network

import com.example.iviapp.model.movie.MoviesResponse
import com.google.gson.JsonObject
import retrofit2.Response
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
    suspend fun getPopularMovieListCoroutine(
        @Query("api_key") apiKey: String
    ): Response<MoviesResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieCoroutine(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Response<JsonObject>

    @GET("account/{account_id}/favorite/movies")
    suspend fun getFavoritesCoroutine(
        @Path("account_id") id: Int,
        @Query("api_key") apiKey: String,
        @Query("session_id") sessionId: String
    ): Response<MoviesResponse>

    @POST("account/{account_id}/favorite")
    suspend fun rateCoroutine(
        @Path("account_id") accountId: Int?,
        @Query("api_key") apiKey: String,
        @Query("session_id") sessionId: String?,
        @Body body: JsonObject
    ): Response<JsonObject>

    @GET("movie/{movie_id}/account_states")
    suspend fun isFavoriteCoroutine(
        @Path("movie_id") movieId: Int?,
        @Query("api_key") apiKey: String,
        @Query("session_id") sessionId: String?
    ): Response<JsonObject>

    @GET("authentication/token/new")
    suspend fun getTokenCoroutine(
        @Query("api_key") apiKey: String
    ): Response<JsonObject>

    @POST("authentication/token/validate_with_login")
    suspend fun logInCoroutine(
        @Query("api_key") apiKey: String,
        @Body body: JsonObject
    ): Response<JsonObject>

    @POST("authentication/session/new")
    suspend fun getSessionCoroutine(
        @Query("api_key") apiKey: String,
        @Body body: JsonObject
    ): Response<JsonObject>

    @HTTP(
        method = "DELETE",
        path = "authentication/session",
        hasBody = true
    )
    suspend fun deleteSessionCoroutine(
        @Query("api_key") apiKey: String,
        @Body body: JsonObject
    ): Response<JsonObject>

    @GET("account")
    suspend fun getAccountCoroutine(
        @Query("api_key") apiKey: String,
        @Query("session_id") sessionId: String
    ): Response<JsonObject>

}
