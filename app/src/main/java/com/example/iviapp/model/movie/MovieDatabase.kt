package com.example.iviapp.model.movie

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Movie::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object {
        var INSTANCE: MovieDatabase? = null
        fun getDatabase(context: Context): MovieDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    "app_database.db"
                ).allowMainThreadQueries()
                    .build()
            }
            return INSTANCE!!
        }
    }
}