package com.example.iviapp.view.activity

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.iviapp.R
import com.example.iviapp.model.cinema.Cinema
import com.example.iviapp.view_model.CinemaListViewModel
import com.example.iviapp.view_model.ViewModelProviderFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : FragmentActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var cinemaListViewModel: CinemaListViewModel
    private lateinit var progressBar: ProgressBar
    private lateinit var cinemaList: List<Cinema>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        progressBar = findViewById(R.id.progressBar)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val viewModelProviderFactory = ViewModelProviderFactory(this)
        cinemaListViewModel = ViewModelProvider(this, viewModelProviderFactory)
            .get(CinemaListViewModel::class.java)
        cinemaList = cinemaListViewModel.getCinemaList()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        for (cinema in cinemaList) {
            val marker = LatLng(cinema.latitude, cinema.longitude)
            map.addMarker(MarkerOptions().position(marker).title(cinema.title))
            map.moveCamera(CameraUpdateFactory.newLatLng(marker))
        }
        map.uiSettings.isZoomControlsEnabled = true
        map.setMinZoomPreference(12F)
        progressBar.visibility = View.GONE
    }
}
