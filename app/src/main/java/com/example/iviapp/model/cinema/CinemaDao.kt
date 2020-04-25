package com.example.iviapp.model.cinema

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CinemaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllCinema(list: List<Cinema>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCinema(cinema: Cinema)

    @Query("SELECT * FROM cinema_table")
    fun getAllCinema(): List<Cinema>
}