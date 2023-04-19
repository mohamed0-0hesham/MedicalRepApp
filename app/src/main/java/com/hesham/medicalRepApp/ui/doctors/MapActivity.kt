package com.hesham.medicalRepApp.ui.doctors

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.hesham.medicalRepApp.R
import com.hesham.medicalRepApp.databinding.ActivityMapBinding

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapBinding
    private var marker: Marker? = null
    private var mlatling: LatLng? = null
    private val EXTRA_DATA = "EXTRA_DATA"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        binding.saveBtn.setOnClickListener {
            if (mlatling != null) {
                val data = Intent()
                val location= arrayListOf(mlatling!!.latitude,mlatling!!.longitude)
                data.putExtra(EXTRA_DATA, location)
                setResult(Activity.RESULT_OK, data)
                val test=data.extras?.get(EXTRA_DATA)
                Log.i("Test","$test")
                finish()
                Toast.makeText(this, "$mlatling", Toast.LENGTH_LONG).show()
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
}