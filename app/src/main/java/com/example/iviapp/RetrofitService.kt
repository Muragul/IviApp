package com.example.iviapp

import com.example.iviapp.model.Genre
import com.example.iviapp.model.Movie
import com.example.iviapp.model.MoviesResponse
import com.google.gson.JsonObject

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

object RetrofitService {

    const val BASE_URL = "https://api.themoviedb.org/3/"

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
    fun getPopularMovieList(@Query("api_key") apiKey: String): Call<MoviesResponse>

    @GET("movie/{movie_id}")
    fun getMovie(@Path("movie_id") movieId: Int, @Query("api_key") apiKey: String) : Call<Movie>

    @GET("account/{account_id}/favorite/movies")
    fun getFavorites(@Query("api_key")apiKey: String): Call<MoviesResponse>

    @GET("/genre/movie/list")
    fun getGenres(@Query("api_key")apiKey: String): Call<List<Genre> >

    @GET("movie/top_rated")
    fun getTopRatedMovieList(@Query("api_key")apiKey: String): Call<MoviesResponse>

}