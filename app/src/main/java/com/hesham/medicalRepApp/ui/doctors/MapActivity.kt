package com.hesham.medicalRepApp.ui.doctors

import android.app.Activity
import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.hesham.medicalRepApp.R
import com.hesham.medicalRepApp.databinding.ActivityMapBinding
import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.hesham.medicalRepApp.methods.Utilities.Companion.checkPermissions
import java.util.*


class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapBinding
    private var marker: Marker? = null
    private var mlatling: LatLng? = null
    private val EXTRA_DATA = "EXTRA_DATA"
    private lateinit var currentLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        checkPermissions(this)
        currentLocationIntoMap()

        binding.saveBtn.setOnClickListener {
            if (mlatling != null) {
                val data = Intent()
                val location= arrayListOf(mlatling!!.latitude,mlatling!!.longitude)
                data.putExtra(EXTRA_DATA, location)
                setResult(Activity.RESULT_OK, data)
                val test=data.extras?.get(EXTRA_DATA)
                Log.i("Test","$test")
                finish()
            } else
                Toast.makeText(this, "you must Choose location to save", Toast.LENGTH_LONG).show()
        }
        mMap.setOnMapLongClickListener { latling ->
            if (marker != null)
                marker!!.remove()
            marker = mMap.addMarker(
                MarkerOptions().position(latling).title("Marker in Your Location")
                    .snippet("description")
            )
            mlatling = latling
            binding.saveBtn.isClickable = true
        }
        mMap.setOnMapClickListener {
            marker?.remove()
            mlatling = null
        }
    }

    private fun currentLocationIntoMap(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            // Enable the location layer on the map
            mMap.isMyLocationEnabled = true

            // Get the last known user location and move the camera to it
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    val currentLatLng = LatLng(it.latitude, it.longitude)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16f))
                }
            }
        } else {
            // Request location permissions if not granted
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, enable the location layer and move the camera to the user's location
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                mMap.isMyLocationEnabled = true
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    location?.let {
                        val currentLatLng = LatLng(it.latitude, it.longitude)
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16f))
                    }
                }
            }
        }
    }
}