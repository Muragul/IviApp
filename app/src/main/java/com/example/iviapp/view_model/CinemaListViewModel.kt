package com.example.iviapp.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.iviapp.model.cinema.Cinema
import com.example.iviapp.model.cinema.CinemaDao
import com.example.iviapp.model.cinema.CinemaDatabase

class CinemaListViewModel(context: Context) : ViewModel() {
    private val cinemaDao: CinemaDao = CinemaDatabase.getDatabase(context).cinemaDao()

    fun getCinemaList(): List<Cinema> {
        return cinemaDao.getAllCinema()
    }

    fun addCinemaListToDatabase(list: List<Cinema>){
        cinemaDao.insertAllCinema(list)
    }
}