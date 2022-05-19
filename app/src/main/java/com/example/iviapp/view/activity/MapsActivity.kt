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
import com.example.iviapp.view.CustomClusterRenderer
import com.example.iviapp.view.TotemItem
import com.example.iviapp.view.images
import com.example.iviapp.view_model.CinemaListViewModel
import com.example.iviapp.view_model.ViewModelProviderFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager

class MapsActivity : FragmentActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var cinemaListViewModel: CinemaListViewModel
    private lateinit var progressBar: ProgressBar
    private lateinit var cinemaList: List<Cinema>
    private var clusterManager: ClusterManager<TotemItem>? = null

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
        map.uiSettings.isZoomControlsEnabled = true
        map.moveCamera(CameraUpdateFactory.newLatLng(LatLng(43.211989, 76.842285)))
        progressBar.visibility = View.GONE
        initClusterManager()
    }

    private fun initClusterManager() {
        clusterManager = ClusterManager(this, map)
        val customRenderer = CustomClusterRenderer(this, map, clusterManager)
        clusterManager?.apply {
            renderer = customRenderer
            map.setOnCameraIdleListener(this)
            map.setOnMarkerClickListener(this)
            map.setOnInfoWindowClickListener(this)
            map.setInfoWindowAdapter(this.getMarkerManager())
            setOnClusterItemClickListener { item ->
                false
            }
            addItems()
            cluster()
        }
    }

    private fun addItems() {
        // Set some lat/lng coordinates to start with.
        var lat = 43.211989
        var lng = 76.842285

        // Add ten cluster items in close proximity, for purposes of this example.
        for (i in 0..9) {
            val offset = i / 60.0
            lat += offset
            lng += offset
            val offsetItem =
                TotemItem("Title $i", lat, lng, images[i])
            clusterManager?.addItem(offsetItem)
        }
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
