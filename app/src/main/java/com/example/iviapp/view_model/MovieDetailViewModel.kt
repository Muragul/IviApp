package com.example.iviapp.view_model

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.iviapp.BuildConfig
import com.example.iviapp.model.account.CurrentUser
import com.example.iviapp.model.movie.FavoriteResponse
import com.example.iviapp.model.movie.Movie
import com.example.iviapp.model.movie.MovieDao
import com.example.iviapp.model.movie.MovieDatabase
import com.example.iviapp.model.network.RetrofitService
import com.example.iviapp.view.activity.DetailActivity
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class MovieDetailViewModel(context: Context) : ViewModel(), CoroutineScope {
    private val job = Job()
    var liveData = MutableLiveData<State>()
    private val movieDao: MovieDao = MovieDatabase.getDatabase(context).movieDao()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun getMovie(movieId: Int) {
        launch {
            liveData.value = State.ShowLoading
            var movie: Movie
            try {
                val response = RetrofitService.getPostApi()
                    .getMovieCoroutine(movieId, BuildConfig.THE_MOVIE_DB_API_TOKEN)
                if (response.isSuccessful) {
                    val result = Gson().fromJson(response.body(), Movie::class.java)
                    movie = result
                } else {
                    movieDao.getForDetail(movieId)
                    movie = movieDao.getForDetail(movieId)
                }
            } catch (e: Exception) {
                movieDao.getForDetail(movieId)
                movie = movieDao.getForDetail(movieId)
            }
            liveData.value = State.HideLoading
            liveData.value = State.Result(movie)
        }
    }

    fun likeMovie(isFavorite: Boolean, movieId: Int) {
        launch {
            try {
                val body = JsonObject().apply {
                    addProperty("media_type", "movie")
                    addProperty("media_id", movieId)
                    addProperty("favorite", isFavorite)
                }
                RetrofitService.getPostApi().rateCoroutine(
                    CurrentUser.user.accountId,
                    BuildConfig.THE_MOVIE_DB_API_TOKEN,
                    CurrentUser.user.sessionId,
                    body
                )
                val movie = movieDao.getForDetail(movieId)
                movie.isFavorite = !movie.isFavorite
                movieDao.insertForDetail(movie)
            } catch (e: Exception) {
                val movie = movieDao.getForDetail(movieId)
                movie.isFavorite = !movie.isFavorite
                movieDao.insertForDetail(movie)
                DetailActivity.needToSycn = true
            }
        }
    }

    fun isFavoriteMovie(movieId: Int) {
        launch {
            try {
                val response = RetrofitService.getPostApi().isFavoriteCoroutine(
                    movieId,
                    BuildConfig.THE_MOVIE_DB_API_TOKEN,
                    CurrentUser.user.sessionId
                )
                if (response.isSuccessful) {
                    val like = Gson().fromJson(
                        response.body(),
                        FavoriteResponse::class.java
                    ).favorite
                    liveData.value = State.IsFavorite(like)
                }
            } catch (e: Exception) {
                val movie = movieDao.getForDetail(movieId)
                liveData.value = State.IsFavorite(movie.isFavorite)
            }
        }
    }


    sealed class State {
        object ShowLoading : State()
        object HideLoading : State()
        data class Result(val movie: Movie) : State()
        data class IsFavorite(val isFavorite: Boolean) : State()
    }

}