package com.example.iviapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.iviapp.model.Genre
import com.example.iviapp.model.Movie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val poster: ImageView = findViewById(R.id.poster)
        val movieTitle: TextView = findViewById(R.id.title)
        val releaseDate: TextView = findViewById(R.id.releasedate)
        val adult: TextView = findViewById(R.id.adult)
        val rating: TextView = findViewById(R.id.rate)
        val popularity: TextView = findViewById(R.id.popularity)
        val back: ImageButton = findViewById(R.id.back)
        val overview: TextView = findViewById(R.id.overview)

        if (Build.VERSION.SDK_INT < 16){
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            var decorView: View = window.decorView
            var options: Int = View.SYSTEM_UI_FLAG_FULLSCREEN
            decorView.systemUiVisibility = options
        }

        /*
        lateinit var genres: List<Genre>
        var s: MutableList<String> = ArrayList()

        RetrofitService.getPostApi().getGenres(BuildConfig.THE_MOVIE_DB_API_TOKEN)
            .enqueue(object : Callback<List<Genre>>{
                override fun onFailure(call: Call<List<Genre>>, t: Throwable) {
                }
                override fun onResponse(call: Call<List<Genre>>, response: Response<List<Genre>>) {
                    if (response.isSuccessful){
                        genres = response.body() as List<Genre>
                    }
                }
            })


        val id = intent.extras?.getInt("movie_id", 0)
        lateinit var mov: Movie
        var call: Call<Movie>? = id?.let { RetrofitService.getPostApi().getMovie(it, BuildConfig.THE_MOVIE_DB_API_TOKEN) }
        call?.enqueue(object : Callback<Movie>{
            override fun onFailure(call: Call<Movie>, t: Throwable) {
            }
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                if (response.isSuccessful){
                    Glide.with(this@DetailActivity).load(response.body()?.getBackdropPath()).into(poster)
                    movieTitle.text = response.body()?.originalTitle
                    releaseDate.text = response.body()?.releaseDate
                    if (response.body()?.adult!!){
                        adult.text = "18+"
                    } else {
                        adult.text = "No"
                    }
                    rating.text = response.body()?.voteAverage.toString()
                    popularity.text = response.body()?.popularity.toString()
                    overview.text = response.body()?.overview
                    /*for (i:Int in response.body()!!.genreIds){
                        for (g: Genre in genres){
                            if (g.id == i){
                                s.add(g.name)
                            }
                        }
                    }
                    var text = ""
                    for (string: String in s){
                        text += "$string, "
                    }
                    overview.text = text

                     */
                }
            }
        })

         */
        Glide.with(this).load(intent.extras?.getString("backdrop_path")).into(poster)
        movieTitle.text = intent.extras?.getString("original_title")
        releaseDate.text = intent.extras?.getString("release_date")
        if (intent.getBooleanExtra("adult", false)){
            adult.text = "18+"
        } else {
            adult.text = "No"
        }
        rating.text = intent.extras?.getString("vote_average")
        popularity.text = intent.extras?.getString("popularity")
        overview.text = intent.extras?.getString("overview")

        back.setOnClickListener{
            onBackPressed()
        }
    }
}