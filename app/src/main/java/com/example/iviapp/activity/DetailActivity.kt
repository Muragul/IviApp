package com.example.iviapp.activity

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.example.iviapp.*
import com.example.iviapp.model.CurrentUser
import com.example.iviapp.model.FavoriteResponse
import com.example.iviapp.model.Movie
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class DetailActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var poster: ImageView
    private lateinit var movieTitle: TextView
    lateinit var releaseDate: TextView
    lateinit var adult: TextView
    lateinit var rating: TextView
    lateinit var popularity: TextView
    lateinit var overview: TextView
    lateinit var save: ImageButton
    var isFav: Boolean = false
    private var movieId: Int = 1
    private val job = Job()

    private var movieDao: MovieDao? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        poster = findViewById(R.id.poster)
        movieTitle = findViewById(R.id.title)
        releaseDate = findViewById(R.id.releasedate)
        adult = findViewById(R.id.adult)
        rating = findViewById(R.id.rate)
        popularity = findViewById(R.id.popularity)
        overview = findViewById(R.id.overview)
        val back: ImageButton = findViewById(R.id.back)
        save = findViewById(R.id.save)

        movieDao = MovieDatabase.getDatabase(context = this).movieDao()

        if (Build.VERSION.SDK_INT < 16) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        } else {
            val decorView: View = window.decorView
            val options: Int = View.SYSTEM_UI_FLAG_FULLSCREEN
            decorView.systemUiVisibility = options
        }

        movieId = intent.getIntExtra("movie_id", 1)
        getMovieCoroutine()

        back.setOnClickListener {
            onBackPressed()
        }

        save.setOnClickListener {
            if (isFav) {
                Glide.with(this).load(R.drawable.ic_turned_in).into(save)
            } else {
                Glide.with(this).load(R.drawable.ic_turned).into(save)
            }
            likeMovieCoroutine(!isFav)
        }

    }

    private fun fillViews(movie: Movie) {
        overview.text = movie.overview
        Glide.with(this@DetailActivity).load(movie.getBackdropPath())
            .into(poster)
        movieTitle.text = movie.originalTitle
        releaseDate.text = movie.releaseDate
        if (movie.adult)
            adult.text = "18+"
        else
            adult.text = "No"
        rating.text = movie.voteAverage.toString()
        popularity.text = movie.popularity.toString()
        isFavoriteCoroutine()
    }

//    private fun cacheFillViews(movie: List<Movie>){
//        overview.text = movie.overview
//        movieTitle.text = movie.originalTitle
//        releaseDate.text = movie.releaseDate
//        if (movie.adult)
//            adult.text = "18+"
//        else
//            adult.text = "No"
//        rating.text = movie.voteAverage.toString()
//        popularity.text = movie.popularity.toString()
//        isFavoriteCoroutine()
//
//    }


    private fun isFavoriteCoroutine() {
        launch {
            val response = RetrofitService.getPostApi().isFavoriteCoroutine(
                movieId,
                BuildConfig.THE_MOVIE_DB_API_TOKEN,
                CurrentUser.user?.sessionId
            )
            if (response.isSuccessful) {
                val like = Gson().fromJson(
                    response.body(),
                    FavoriteResponse::class.java
                ).favorite
                isFav = if (like) {
                    save.setImageResource(R.drawable.ic_turned)
                    true
                } else {
                    save.setImageResource(R.drawable.ic_turned_in)
                    false
                }
            }
        }
    }

    private fun likeMovieCoroutine(isFavorite: Boolean) {
        launch {
            val body = JsonObject().apply {
                addProperty("media_type", "movie")
                addProperty("media_id", movieId)
                addProperty("favorite", isFavorite)
            }
            RetrofitService.getPostApi().rateCoroutine(
                CurrentUser.user?.accountId,
                BuildConfig.THE_MOVIE_DB_API_TOKEN,
                CurrentUser.user?.sessionId,
                body
            )
        }
    }

    private fun getMovieCoroutine() {
        launch {

            try {
                val response = RetrofitService.getPostApi().getMovieCoroutine(movieId,BuildConfig.THE_MOVIE_DB_API_TOKEN)
                if(response.isSuccessful){
                    val result = Gson().fromJson<Movie>(response.body(),Movie::class.java)
                    fillViews(result)
                    if(result == null){
                        movieDao?.insertForDetail(result as Movie)
                    }
                    result
                }else{
                    movieDao?.getForDetail()
                }

            }catch (e: Exception){
                movieDao?.getForDetail()

            }



        }
    }

}