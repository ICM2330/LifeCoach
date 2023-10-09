package com.example.lifecoach_.activities.habits.auxiliar

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.os.StrictMode
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.example.lifecoach_.R
import com.example.lifecoach_.activities.habits.view.MuscularHabitViewActivity
import com.example.lifecoach_.databinding.ActivitySearchGymForHabitBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import java.util.Locale

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

    //Markers and geopoints
    private var lastLocationMarker: Marker? = null
    private var lastLocationGeoPoint: GeoPoint? = null
    private var listMarkersNearestGyms = mutableListOf<Marker>()
    private var listGeoPointsNearestGyms = mutableListOf<GeoPoint>()

    //Route elements:
    private var roadOverlay: Polyline? = null

    //Road Manager
    private lateinit var roadManager: RoadManager

    // -- IMPLEMENTATION METHODS OF LIFECYCLE OF ACTIVITY ---
    // On Create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchGymForHabitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Configuration.getInstance().load(
            this, androidx.preference.PreferenceManager.getDefaultSharedPreferences(this)
        )


        //Geocoder initialization
        geocoder = Geocoder(baseContext, Locale.getDefault())

        //Allow network access in the main thread - Just for performance purposes
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        //Road Manager initialization
        roadManager = OSRMRoadManager(this, "ANDROID")

        // Manage the actions with the buttons
        manageButtons()
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

    // Method to manage the buttons of the view with interaction
    private fun manageButtons() {
        manageButtonSearchNearestGym()
        manageButtonComeBackToHabitView()
    }

    // Method to manage the button of come back to the view of the habit
    private fun manageButtonComeBackToHabitView() {
        binding.buttonBack.setOnClickListener {
            startActivity(Intent(baseContext, MuscularHabitViewActivity::class.java))
        }
    }

    // Method to manage the button of search nearest gym
    private fun manageButtonSearchNearestGym() {
        binding.searchNearestGymButton.setOnClickListener {
            val lowerLeftLatitude = lastLocation!!.latitude - 0.1
            val lowerLeftLongitude = lastLocation!!.longitude - 0.1
            val upperRightLatitude = lastLocation!!.latitude + 0.1
            val upperRightLongitude = lastLocation!!.longitude + 0.1

            val placesList = geocoder.getFromLocationName("Gimnasio", 5,
                lowerLeftLatitude, lowerLeftLongitude, upperRightLatitude, upperRightLongitude)
            if (!placesList.isNullOrEmpty()){
                // Clear the map
                map.overlays.clear()

                // Put the marker of the last location
                lastLocationGeoPoint = GeoPoint(lastLocation!!.latitude, lastLocation!!.longitude)
                lastLocationMarker = Marker(map)
                lastLocationMarker!!.position = lastLocationGeoPoint
                lastLocationMarker!!.title = "Ubicación Actual"
                val myIcon = resources.getDrawable(R.drawable.userpin, theme)
                lastLocationMarker!!.icon = myIcon
                lastLocationMarker!!.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                map.overlays.add(lastLocationMarker)


                // Put the marker in every found place
                for (place in placesList){
                    val actualGeoPoint = GeoPoint(place.latitude, place.longitude)
                    val actualMarker = Marker(map)
                    actualMarker.position = actualGeoPoint
                    actualMarker.title = place.featureName
                    val myIcon = resources.getDrawable(R.drawable.pesoicon, theme)
                    actualMarker.icon = myIcon
                    actualMarker.snippet = place.getAddressLine(0)
                    actualMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

                    listGeoPointsNearestGyms.add(actualGeoPoint)
                    listMarkersNearestGyms.add(actualMarker)

                    map.overlays.add(actualMarker)
                }
            }
            else{
                Toast.makeText(baseContext, "No se encontraron gimnasios cercanos a tu ubicación", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Method to draw the five nearest gyms
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun drawFiveNearestGymsMarkers() {
        val lowerLeftLatitude = lastLocation!!.latitude - 0.1
        val lowerLeftLongitude = lastLocation!!.longitude - 0.1
        val upperRightLatitude = lastLocation!!.latitude + 0.1
        val upperRightLongitude = lastLocation!!.longitude + 0.1

        val placesList = geocoder.getFromLocationName("Gimnasio", 5,
            lowerLeftLatitude, lowerLeftLongitude, upperRightLatitude, upperRightLongitude)

        if (!placesList.isNullOrEmpty()){
            // Clear the map
            map.overlays.clear()

            // Put the marker of the last location
            lastLocationGeoPoint = GeoPoint(lastLocation!!.latitude, lastLocation!!.longitude)
            lastLocationMarker = Marker(map)
            lastLocationMarker!!.position = lastLocationGeoPoint
            lastLocationMarker!!.title = "Ubicación Actual"
            val myIcon = resources.getDrawable(R.drawable.userpin, theme)
            lastLocationMarker!!.icon = myIcon
            lastLocationMarker!!.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            map.overlays.add(lastLocationMarker)


            // Put the marker in every found place
            for (place in placesList){
                val actualGeoPoint = GeoPoint(place.latitude, place.longitude)
                val actualMarker = Marker(map)
                actualMarker.position = actualGeoPoint
                actualMarker.title = place.featureName
                val myIcon = resources.getDrawable(R.drawable.pesoicon, theme)
                actualMarker.icon = myIcon
                actualMarker.snippet = place.getAddressLine(0)
                actualMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

                listGeoPointsNearestGyms.add(actualGeoPoint)
                listMarkersNearestGyms.add(actualMarker)

                map.overlays.add(actualMarker)
            }
        }
        else{
            Toast.makeText(baseContext, "No se encontraron gimnasios cercanos a tu ubicación", Toast.LENGTH_LONG).show()
        }
    }
}