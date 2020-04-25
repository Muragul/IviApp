package com.example.iviapp.model.cinema

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Cinema::class], version = 1)
abstract class CinemaDatabase : RoomDatabase() {

    abstract fun cinemaDao(): CinemaDao

    companion object {
        var INSTANCE: CinemaDatabase? = null
        fun getDatabase(context: Context): CinemaDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    CinemaDatabase::class.java,
                    "cinema_database.db"
                ).allowMainThreadQueries()
                    .build()
            }
            return INSTANCE!!
        }
    }
}