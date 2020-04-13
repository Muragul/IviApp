package com.example.iviapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.iviapp.activity.DetailActivity
import com.example.iviapp.model.Movie

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<Movie>)

    @Query("SELECT * FROM movie_table")
    fun getAll(): List<Movie>

    @Query("SELECT * FROM movie_table WHERE isFavorite=1")
    fun getFavorite(): List<Movie>

    // get data for Detail Activity
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertForDetail(movie: Movie)

    @Query("SELECT * FROM movie_table WHERE id = :id LIMIT 1")
    fun getForDetail(id: Int): Movie
}