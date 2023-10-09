package com.example.lifecoach_.activities.habits.auxiliar

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.os.StrictMode
import android.util.Log
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.lifecoach_.R
import com.example.lifecoach_.activities.habits.view.MuscularHabitViewActivity
import com.example.lifecoach_.databinding.ActivitySearchGymForHabitBinding
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import java.util.Locale

@Suppress("DEPRECATION", "NAME_SHADOWING")
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

    //Variable to know if the user is going to a gym
    private var isGoingToGym = false

    // -- IMPLEMENTATION METHODS OF LIFECYCLE OF ACTIVITY ---
    // On Create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchGymForHabitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Configuration.getInstance().load(
            this, androidx.preference.PreferenceManager.getDefaultSharedPreferences(this)
        )

        // Init Map
        map = binding.osmMap
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)

        // Use the zoom controller of the map (Not using long or click event, so no trouble)
        map.zoomController.setVisibility(org.osmdroid.views.CustomZoomButtonsController.Visibility.ALWAYS)

        // Do a general zoom to the earth
        map.controller.setZoom(5.0)
        map.controller.animateTo(GeoPoint(4.6287662, -74.0636298647595 ))

        //Geocoder initialization
        geocoder = Geocoder(baseContext, Locale.getDefault())

        // Do the stuff related with the last location and their elements
        locationClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
            .setWaitForAccurateLocation(true).setMinUpdateIntervalMillis(5000).build()

        locationCallback = object : LocationCallback() {
            @SuppressLint("UseCompatLoadingForDrawables")
            override fun onLocationResult(result: LocationResult) {
                super.onLocationResult(result)
                if (result.lastLocation != null) {
                    val currentLocation = result.lastLocation!!

                    if (lastLocation == null) {
                        // If it's the first location, simply register it
                        lastLocation = currentLocation
                        lastLocationGeoPoint =
                            GeoPoint(currentLocation.latitude, currentLocation.longitude)
                        lastLocationMarker = Marker(map)
                        lastLocationMarker?.position = lastLocationGeoPoint
                        lastLocationMarker?.title = "Last Location"
                        val myIcon = resources.getDrawable(R.drawable.userpin, theme)
                        lastLocationMarker?.icon = myIcon
                        lastLocationMarker?.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        map.overlays.add(lastLocationMarker)
                        map.controller.setZoom(18.0)
                        map.controller.animateTo(lastLocationGeoPoint)

                        // Draw the five nearest gyms
                        drawFiveNearestGymsMarkers()
                    } else {
                        // Calculate the distance between the current and previous location
                        val distance = lastLocation!!.distanceTo(currentLocation)

                        // If the distance is greater than or equal to 30 meters, register the location and draw again the five nearest gyms
                        if (distance >= 30.0) {
                            // If the distance is greater than or equal to 30 meters, register the location
                            map.overlays.clear()

                            lastLocation = currentLocation
                            lastLocationGeoPoint =
                                GeoPoint(currentLocation.latitude, currentLocation.longitude)
                            lastLocationMarker = Marker(map)
                            lastLocationMarker?.position = lastLocationGeoPoint
                            lastLocationMarker?.title = "Last Location"
                            val myIcon = resources.getDrawable(R.drawable.userpin, theme)
                            lastLocationMarker?.icon = myIcon
                            lastLocationMarker?.setAnchor(
                                Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM
                            )
                            map.overlays.add(lastLocationMarker)
                            map.controller.setZoom(18.0)
                            map.controller.animateTo(lastLocationGeoPoint)

                            // Draw the five nearest gyms
                            drawFiveNearestGymsMarkers()
                        }
                    }
                }
            }
        }

        // Ask for the permissions and start the location updates if granted
        checkLocationPermission()

        locationClient.lastLocation.addOnFailureListener {
            // Use a default location if the last location is not available or it's null
            // Show a Toast that the last location is not available
            Toast.makeText(
                baseContext,
                "No se puede utilizar las funcionalidades de la aplicación relacionadas con ubicación.",
                Toast.LENGTH_LONG
            ).show()

            // Do a general zoom to the earth
            map.controller.setZoom(5.0)
            map.controller.animateTo(GeoPoint(4.6287662, -74.0636298647595 ))
        }

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
        map.onResume()

        // Check location permission from hardware
        locationSettings()
    }

    // On Pause
    override fun onPause() {
        super.onPause()
        map.onPause()
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
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun manageButtonSearchNearestGym() {
        binding.searchNearestGymButton.setOnClickListener {
            val lowerLeftLatitude = lastLocation!!.latitude - 0.2
            val lowerLeftLongitude = lastLocation!!.longitude - 0.2
            val upperRightLatitude = lastLocation!!.latitude + 0.2
            val upperRightLongitude = lastLocation!!.longitude + 0.2

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


                // Make a List of pairs with the distance between the last location and the nearest gyms
                val listDistancesBetweenTwoPoints = mutableListOf<Pair<Double, GeoPoint>>()
                for (place in placesList){
                    listDistancesBetweenTwoPoints.add(giveDistancesBetweenTwoPoints(lastLocationGeoPoint!!,
                        GeoPoint(place.latitude, place.longitude)))
                }

                // Sort the list of pairs by the distance
                listDistancesBetweenTwoPoints.sortBy { it.first }

                // Draw the route between the last location and the nearest gym
                drawRoute(lastLocationGeoPoint!!, listDistancesBetweenTwoPoints[0].second)

                // Indicate that the user is going to a gym
                isGoingToGym = true
            }
            else{
                Toast.makeText(baseContext,
                    "No se encontraron gimnasios cercanos a tu ubicación",
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun giveDistancesBetweenTwoPoints(
        start: GeoPoint,
        finish: GeoPoint
    ): Pair<Double, GeoPoint> {
        val routePoints = ArrayList<GeoPoint>()
        routePoints.add(start)
        routePoints.add(finish)
        val road = roadManager.getRoad(routePoints)
        return Pair(road.mLength, finish)
    }

    private fun drawRoute (start: GeoPoint, finish: GeoPoint){
        val routePoints = ArrayList<GeoPoint>()
        routePoints.add(start)
        routePoints.add(finish)
        val road = roadManager.getRoad(routePoints)
        Log.i("MapsApp", "Route length: " + road.mLength + " klm")

        // Delete the route if it already exists
        if (roadOverlay != null) {
            map.overlays.remove(roadOverlay)
        }

        // Draw the route
        roadOverlay = RoadManager.buildRoadOverlay(road)
        roadOverlay!!.outlinePaint.color = Color.RED
        roadOverlay!!.outlinePaint.strokeWidth = 10F
        map.overlays.add(roadOverlay)

        //Format the distance to show only 2 decimals
        val formattedDistance = String.format("%.2f", road.mLength)

        // Show a Toast with the distance between the last location and the nearest gym
        Toast.makeText(
            baseContext,
            "Distancia entre localización actual y gimnasio más cercano: $formattedDistance KM",
            Toast.LENGTH_LONG
        ).show()

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

    // Functions related with permissions and this kind of stuff
    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                baseContext, android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_DENIED
        ) {
            if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(
                    baseContext,
                    "You must grant location permission to see the map and receive updates.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            getPermissionLocation.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            // Do actions
            startLocationUpdates()
        }
    }

    private fun locationSettings() {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            checkLocationPermission()
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    val intentSenderRequest =
                        IntentSenderRequest.Builder(exception.resolution).build()
                    locationSettings.launch(intentSenderRequest)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Show a toast if the location settings are not satisfied
                    Toast.makeText(
                        baseContext, "Location settings are not satisfied", Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private val locationSettings = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            checkLocationPermission()
        } else {
            Toast.makeText(baseContext, "Location is required for this app", Toast.LENGTH_LONG)
                .show()
        }
    }
}