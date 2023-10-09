package com.example.lifecoach_.activities.habits.auxiliar

import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.example.lifecoach_.R
import com.example.lifecoach_.databinding.ActivitySearchGymForHabitBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import org.osmdroid.config.Configuration
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polyline

class SearchGymForHabitActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySearchGymForHabitBinding

    // Variables to manage the location and it's permissions and updates
    private lateinit var locationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var lastLocation: Location? = null

    // Permission -> Register For Activity Result
    private val getPermissionLocation = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startLocationUpdates()
        }
    }

    //Variables to store the map
    private lateinit var map: MapView

    //Geocoder Attributes
    private lateinit var geocoder: Geocoder

    //Route elements:
    private var roadOverlay: Polyline? = null

    // -- IMPLEMENTATION METHODS OF LIFECYCLE OF ACTIVITY ---
    // On Create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchGymForHabitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Configuration.getInstance().load(
            this, androidx.preference.PreferenceManager.getDefaultSharedPreferences(this)
        )


    }

    // On Resume
    override fun onResume() {
        super.onResume()
        // Initialize the map


    }

    // On Pause
    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }


    // Method to start the location updates
    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationClient.requestLocationUpdates(
                locationRequest, locationCallback, Looper.getMainLooper()
            )
        }
    }

    // Method to stop the location updates
    private fun stopLocationUpdates() {
        locationClient.removeLocationUpdates(locationCallback)
    }
}