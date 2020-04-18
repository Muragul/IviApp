package com.example.iviapp.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<Movie>)

    @Query("SELECT * FROM movie_table ORDER BY popularity DESC")
    fun getAll(): List<Movie>

    @Query("SELECT * FROM movie_table WHERE isFavorite=1")
    fun getFavorite(): List<Movie>

    // get data for Detail Activity
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertForDetail(movie: Movie)

    @Query("SELECT * FROM movie_table WHERE id = :id LIMIT 1")
    fun getForDetail(id: Int): Movie
}