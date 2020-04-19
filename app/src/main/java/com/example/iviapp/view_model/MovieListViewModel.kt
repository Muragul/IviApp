package com.example.iviapp.view_model

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.iviapp.BuildConfig
import com.example.iviapp.model.account.CurrentUser
import com.example.iviapp.model.movie.Movie
import com.example.iviapp.model.movie.MovieDao
import com.example.iviapp.model.movie.MovieDatabase
import com.example.iviapp.model.network.RetrofitService
import com.example.iviapp.view.activity.DetailActivity
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class MovieListViewModel(context: Context) : ViewModel(), CoroutineScope {
    private val job = Job()
    val liveData = MutableLiveData<State>()
    private val movieDao: MovieDao = MovieDatabase.getDatabase(context).movieDao()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun getMovies() {
        launch {
            liveData.value = State.ShowLoading
            val list = withContext(Dispatchers.IO) {
                try {
                    if (DetailActivity.needToSycn) {
                        val savedMovieList = movieDao.getAll()
                        for (movie in savedMovieList) {
                            val body = JsonObject().apply {
                                addProperty("media_type", "movie")
                                addProperty("media_id", movie.id)
                                addProperty("favorite", movie.isFavorite)
                            }
                            RetrofitService.getPostApi().rateCoroutine(
                                CurrentUser.user.accountId,
                                BuildConfig.THE_MOVIE_DB_API_TOKEN,
                                CurrentUser.user.sessionId,
                                body
                            )
                        }
                        DetailActivity.needToSycn = false
                    }
                    val response = RetrofitService.getPostApi()
                        .getPopularMovieListCoroutine(BuildConfig.THE_MOVIE_DB_API_TOKEN)
                    if (response.isSuccessful) {
                        val result = response.body()?.getResults()
                        if (!result.isNullOrEmpty()) {
                            movieDao.insertAll(result)
                        }
                        val response1 = RetrofitService.getPostApi()
                            .getFavoritesCoroutine(
                                CurrentUser.user.accountId,
                                BuildConfig.THE_MOVIE_DB_API_TOKEN,
                                CurrentUser.user.sessionId.toString()
                            )
                        if (response1.isSuccessful) {
                            val result = response1.body()?.getResults()
                            if (!result.isNullOrEmpty()) {
                                for (movie in result)
                                    movie.isFavorite = true
                                movieDao.insertAll(result)
                            }
                        }
                        result
                    } else {
                        movieDao.getAll()
                    }
                } catch (e: Exception) {
                    movieDao.getAll()
                }
            }
            liveData.value = State.HideLoading
            liveData.value = State.Result(list as List<Movie>)
        }
    }

    fun getFavorites() {
        launch {
            liveData.value = State.ShowLoading
            val list = withContext(Dispatchers.IO) {
                try {
                    if (DetailActivity.needToSycn) {
                        val savedMovieList = movieDao.getAll()
                        for (movie in savedMovieList) {
                            val body = JsonObject().apply {
                                addProperty("media_type", "movie")
                                addProperty("media_id", movie.id)
                                addProperty("favorite", movie.isFavorite)
                            }
                            RetrofitService.getPostApi().rateCoroutine(
                                CurrentUser.user.accountId,
                                BuildConfig.THE_MOVIE_DB_API_TOKEN,
                                CurrentUser.user.sessionId,
                                body
                            )
                        }
                        DetailActivity.needToSycn = false
                    }
                    val response = RetrofitService.getPostApi()
                        .getFavoritesCoroutine(
                            CurrentUser.user.accountId,
                            BuildConfig.THE_MOVIE_DB_API_TOKEN,
                            CurrentUser.user.sessionId.toString()
                        )
                    if (response.isSuccessful) {
                        val result = response.body()?.getResults()
                        if (!result.isNullOrEmpty()) {
                            for (movie in result)
                                movie.isFavorite = true
                            movieDao.insertAll(result)
                        }
                        result
                    } else {
                        movieDao.getFavorite()
                    }
                } catch (e: Exception) {
                    movieDao.getFavorite()
                }
            }
            liveData.value = State.HideLoading
            liveData.value = State.Result(list as List<Movie>)
        }
    }

    sealed class State {
        object ShowLoading : State()
        object HideLoading : State()
        data class Result(val list: List<Movie>) : State()
    }

}