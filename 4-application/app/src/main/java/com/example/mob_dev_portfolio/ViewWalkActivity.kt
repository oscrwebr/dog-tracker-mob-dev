package com.example.mob_dev_portfolio

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mob_dev_portfolio.database.PetAppDatabase
import com.example.mob_dev_portfolio.database.Walk
import com.example.mob_dev_portfolio.databinding.ActivityPhotoViewBinding
import com.example.mob_dev_portfolio.databinding.ActivityViewWalkBinding
import com.example.mob_dev_portfolio.fragments.WalksFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ViewWalkActivity: AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityViewWalkBinding
    private val db by lazy { PetAppDatabase.getDatabase(this)}
    private lateinit var myMap: GoogleMap
    private var walkExtra: Walk? = null


    private lateinit var walkRoute: List<LatLng>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewWalkBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        backButton()
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun viewWalkActivityHandler() {
        walkExtra = intent.getSerializableExtra(WalksFragment.WALK_ROUTE, Walk::class.java)

        binding.walkNameView.text = walkExtra?.walkName
        binding.dateView.text = walkExtra?.date
        binding.timeView.text = walkExtra?.time
        // Reference for decoding JSON: https://medium.com/@tscephalos/parse-json-with-gson-df8219bbbc20#:~:text=Gson%20Deserialization,JSON%20arrays%20into%20Java%20arrays.
        val type = object: TypeToken<List<LatLng>>() {}.type
        walkRoute = Gson().fromJson(walkExtra?.walkRoute, type)
        displayWalkRoute()


    }

    override fun onMapReady(googleMap: GoogleMap) {
        myMap = googleMap
        viewWalkActivityHandler()
    }


    private fun displayWalkRoute() {
        myMap.addPolyline(PolylineOptions().addAll(walkRoute))
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(walkRoute[0], 15f))
    }

    private fun backButton() {
        binding.floatingActionButton3.setOnClickListener {
            finish()
        }
    }

}