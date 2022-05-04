package com.example.iviapp.view.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.iviapp.R
import com.example.iviapp.model.cinema.Cinema
import com.example.iviapp.view.TotemMarker
import com.example.iviapp.view_model.CinemaListViewModel
import com.example.iviapp.view_model.ViewModelProviderFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ui.IconGenerator

const val IMAGE_URL =
    "https://upload.wikimedia.org/wikipedia/commons/thumb/4/42/Shaqi_jrvej.jpg/1200px-Shaqi_jrvej.jpg"

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
        val iconGenerator = IconGenerator(this)
        iconGenerator.setBackground(null)

        val view = TotemMarker.newInstance(IMAGE_URL, this)
        iconGenerator.setContentView(view)
        val bitmap = BitmapDescriptorFactory.fromBitmap(
            iconGenerator.makeIcon()
        )
        for (cinema in cinemaList) {
            val marker = LatLng(cinema.latitude, cinema.longitude)
            val mk = MarkerOptions()
                .position(marker)
                .title(cinema.title)
                .icon(bitmap)
            map.addMarker(mk)
            map.moveCamera(CameraUpdateFactory.newLatLng(marker))
        }
        map.uiSettings.isZoomControlsEnabled = true
        map.setMinZoomPreference(12F)
        progressBar.visibility = View.GONE
    }

    private fun resizeMapIcons(iconName: String?, width: Int, height: Int): Bitmap {
        val imageBitmap = BitmapFactory.decodeResource(
            resources, resources.getIdentifier(
                iconName, "drawable",
                packageName
            )
        )
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false)
    }

}
