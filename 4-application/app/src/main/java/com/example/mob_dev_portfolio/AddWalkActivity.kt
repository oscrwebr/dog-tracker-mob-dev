package com.example.mob_dev_portfolio

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mob_dev_portfolio.databinding.ActivityAddWalkBinding
import com.example.mob_dev_portfolio.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.Manifest
import android.annotation.SuppressLint

// Google Maps API Reference to: CodingZest Google Maps in Android 2023 (Link: https://www.youtube.com/watch?v=pOKPQ8rYe6g&list=PLHQRWugvckFrWppucVnQ6XhiJyDbaCU79)
class AddWalkActivity : AppCompatActivity(), OnMapReadyCallback  {

    private lateinit var binding: ActivityAddWalkBinding
    private lateinit var myMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var currentLocation: Location? = null
    private val defaultLocation = LatLng(51.4837, -3.1681)
    private var locationPermissionGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddWalkBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        // Reference to Current Place Tutorial from Google Maps SDK Documentation: https://developers.google.com/maps/documentation/android-sdk/current-place-tutorial
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        getLocationPermission()

        // Google Maps SDK Documentation: https://developers.google.com/maps/documentation/android-sdk/map
        // Cannot use binding on a <fragment> in XML, so must use findFragmentById
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        myMap = googleMap
        showCurrentLocation()
        getLocation()
    }


    // Function adapted from Google Maps SDK Documentation: https://developers.google.com/maps/documentation/android-sdk/current-place-tutorial
    @SuppressLint("MissingPermission")
    fun getLocation() {
        if(locationPermissionGranted) {
            val locationResult = fusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    currentLocation = task.result
                    if (currentLocation != null) {
                        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(currentLocation!!.latitude, currentLocation!!.longitude), 15f))
                    } else {
                        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15f))
                    }
                }
            }
        }
    }

    // Function adapted from: https://developers.google.com/maps/documentation/android-sdk/current-place-tutorial
    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }

    // Override function adapted from: https://developers.google.com/maps/documentation/android-sdk/current-place-tutorial
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        locationPermissionGranted = false
        if(requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
        showCurrentLocation()
        getLocation()
    }
    @SuppressLint("MissingPermission")
    fun showCurrentLocation() {
        if(locationPermissionGranted) {
            myMap.isMyLocationEnabled = true
            myMap.uiSettings.isMyLocationButtonEnabled = true
        } else {
            myMap.isMyLocationEnabled = false
            myMap.uiSettings.isMyLocationButtonEnabled = false
            currentLocation = null
        }
    }


    companion object {
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    }

}