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
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.mob_dev_portfolio.database.Pet
import com.example.mob_dev_portfolio.database.PetAppDatabase
import com.example.mob_dev_portfolio.database.Walk
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Google Maps API Reference to: CodingZest Google Maps in Android 2023 (Link: https://www.youtube.com/watch?v=pOKPQ8rYe6g&list=PLHQRWugvckFrWppucVnQ6XhiJyDbaCU79)
class AddWalkActivity : AppCompatActivity(), OnMapReadyCallback  {

    private lateinit var binding: ActivityAddWalkBinding
    private lateinit var myMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var datePickerDialog: DatePickerDialog
    private var currentLocation: Location? = null
    private val defaultLocation = LatLng(51.4837, -3.1681)
    private var locationPermissionGranted = false
    private val db by lazy { PetAppDatabase.getDatabase(this)}
    private val calendar: Calendar = Calendar.getInstance()


    private val markers: ArrayList<LatLng> = ArrayList()
    private var polyline: Polyline? = null

    private var petObjects: List<Pet> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddWalkBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        backButtonHandler()
        spinnerHandler()
        datePicker()
        timePicker()
        addWalkButtonHandler()
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
        mapRouteHandler()
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

    fun mapRouteHandler() {
        myMap.setOnMapClickListener { markerPosition ->
            myMap.addMarker(MarkerOptions().position(markerPosition))
            markers.add(markerPosition)

            if(markers.size > 1) {
                polyline = myMap.addPolyline(PolylineOptions().addAll(markers))
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

    private fun backButtonHandler() {
        binding.backButton.setOnClickListener {
            finish()
            myMap.clear()
            markers.clear()
            polyline = null
        }
    }

    // Spinner handler partly based on: https://developer.android.com/develop/ui/views/components/spinner
    // Adapted to fetch from database
    private fun spinnerHandler() {
        db.petDao().getPets().observe(this) { pets ->

            petObjects = pets
            val petNames = pets.map {it.name}

            if(petNames.isEmpty()) {
                binding.noPetsInDatabase.visibility = android.view.View.VISIBLE
                binding.pleaseGoBack.visibility = android.view.View.VISIBLE
            } else {
                binding.noPetsInDatabase.visibility = android.view.View.GONE
                binding.pleaseGoBack.visibility = android.view.View.GONE


                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, petNames)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinner.adapter = adapter
            }
        }


    }
    // Function taken from my AddPetFragment class
    private fun datePicker() {
        binding.walkDateEt.setOnClickListener{
            val currentYear = calendar.get(Calendar.YEAR)
            val currentMonth = calendar.get(Calendar.MONTH)
            val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

            datePickerDialog = DatePickerDialog(this, { _, year, month, day ->
                val selectedDate = "$day/${month + 1}/$year"
                binding.walkDateEt.setText(selectedDate)
            }, currentYear, currentMonth, currentDay)
            datePickerDialog.show()

        }
    }
    // TimePickerDialog Pop Up adapted from: https://www.youtube.com/watch?v=8vY_svhd0Uo
    private fun timePicker() {
        binding.walkTimeEt.setOnClickListener{
            val hours = calendar.get(Calendar.HOUR_OF_DAY)
            val minutes = calendar.get(Calendar.MINUTE)
            val timePickerDialog = TimePickerDialog(this, {_, hour, minute ->
                val selectedTime = "$hour:$minute"
                binding.walkTimeEt.setText(selectedTime)

            }, hours, minutes, true)
            timePickerDialog.show()
        }
    }

    private fun addWalkButtonHandler() {
        binding.nextButton.setOnClickListener {
            insertWalk()

            Toast.makeText(this, getString(R.string.walk_added), Toast.LENGTH_SHORT).show()
        }
    }

    private fun insertWalk() {
        val selectedPetPosition = binding.spinner.selectedItemPosition
        val selectedPet = petObjects[selectedPetPosition]
        val selectedPetId = selectedPet.id

        val name = binding.walkNameEt.text.toString()
        val date = binding.walkDateEt.text.toString()
        val time = binding.walkTimeEt.text.toString()
        val walkRoute = markersToJson()

        if(name.isNotEmpty() && date.isNotEmpty() && time.isNotEmpty() && walkRoute.isNotEmpty()) {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val newWalk = Walk(0, selectedPetId, name, date, time, walkRoute)
                    db.walkDao().addWalk(newWalk)
                }

            }
        } else {
            Toast.makeText(this, getString(R.string.remember_to_fill_in_all_fields), Toast.LENGTH_SHORT).show()
        }
        myMap.clear()
        markers.clear()
        polyline = null

    }
    // Reference to: https://google.github.io/gson/UserGuide.html#using-gson-with-gradleandroid
    private fun markersToJson(): String {
        val markersJson = Gson().toJson(markers)
        return markersJson
    }




    companion object {
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    }

}