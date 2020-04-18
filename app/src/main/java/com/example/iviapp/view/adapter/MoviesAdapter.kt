package com.example.iviapp.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.iviapp.view.activity.DetailActivity
import com.example.iviapp.R
import com.example.iviapp.model.Movie


class MoviesAdapter(var context: Context, var movieList: List<Movie>) :
    RecyclerView.Adapter<MoviesAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.movie_card, viewGroup, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: MoviesAdapter.MyViewHolder, i: Int) {
        viewHolder.bind(movieList[i])
    }

    override fun getItemCount(): Int = movieList.size

    inner class MyViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(post: Movie?) {
            val title = view.findViewById<TextView>(R.id.title)
            val thumbnail = view.findViewById<ImageView>(R.id.thumbnail)

            title.text = post?.originalTitle

            Glide.with(context)
                .load(post!!.getPosterPath())
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(thumbnail)

            view.setOnClickListener {
                val intent = Intent(view.context, DetailActivity::class.java)
                intent.putExtra("movie_id", post.id)
                view.context.startActivity(intent)
            }
        }
    }


}