package com.example.lifecoach_.activities.habits.auxiliar

import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.lifecoach_.R
import com.example.lifecoach_.databinding.ActivitySearchGymForHabitBinding
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest


class SearchGymForHabitActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivitySearchGymForHabitBinding
    private lateinit var mMap: GoogleMap

    private lateinit var locationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private var lastLocation: Location? = null
    private var lastLocationMarker: Marker? = null

    private val getPermissionLocation =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startLocationUpdates()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchGymForHabitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.googleMapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
            .setWaitForAccurateLocation(true).setMinUpdateIntervalMillis(5000).build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                if (locationResult.lastLocation != null) {
                    lastLocation = locationResult.lastLocation
                    updateLocationOnMap()
                    putGymsWithinRangeMarkers()
                }
            }
        }

        checkLocationPermission()
        manageButtons()
    }

    override fun onMapReady(gMap: GoogleMap) {
        mMap = gMap
        mMap.uiSettings.setAllGesturesEnabled(true)
        Places.initialize(this, getString(R.string.google_maps_key))
    }

    private fun updateLocationOnMap() {
        lastLocation?.let {
            val latLng = LatLng(it.latitude, it.longitude)
            if (lastLocationMarker == null) {
                lastLocationMarker = mMap.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title("Last Location")
                        .icon(bitmapDescriptorFromVector(baseContext, R.drawable.userpin))
                )
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f))
            } else {
                lastLocationMarker?.position = latLng
            }
        }
    }

    private fun manageButtons() {
        binding.searchNearestGymButton.setOnClickListener {

        }
    }

    private fun putGymsWithinRangeMarkers() {
        // Initialize the Places SDK if you haven't already
        Places.initialize(applicationContext, getString(R.string.google_maps_key))

        // Specify the fields you want to retrieve for each place
        val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)

        // Define the maximum distance in meters (e.g., 3000 meters for a 3 km radius)
        val maxDistance = 3000

        // Use the builder to create a FindCurrentPlaceRequest.
        val placesClient = Places.createClient(this)

        // Create a FindCurrentPlaceRequest.
        val request = FindCurrentPlaceRequest.newInstance(
            placeFields
        )

        checkLocationPermission()
        // Call findCurrentPlace and handle the response (first check that the user has granted permission).
        placesClient.findCurrentPlace(request).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val response = task.result
                var anyMarkerPut = false
                if (response != null) {
                    val places = response.placeLikelihoods

                    // Add markers for the places (gyms) within the specified distance
                    for (likelihood in places) {
                        val place = likelihood.place
                        val latLng = place.latLng
                        val placeName = place.name

                        Log.i("Places", "Place found: ${place.name}")

                        if (latLng != null && placeName != null) {
                            val distance = calculateDistance(lastLocation?.latitude ?: 0.0, lastLocation?.longitude ?: 0.0, latLng.latitude, latLng.longitude)

                            if (distance <= maxDistance &&
                                (placeName.contains("Gym" , ignoreCase = true)
                                        || placeName.contains("Gimnasio", ignoreCase = true)
                                        || placeName.contains("BodyTech", ignoreCase = true)
                                        || placeName.contains("Fit", ignoreCase = true))){
                                mMap.addMarker(
                                    MarkerOptions()
                                        .position(latLng)
                                        .title(placeName)
                                        .icon(bitmapDescriptorFromVector(baseContext, R.drawable.pesoicon))
                                )
                                anyMarkerPut = true
                            }
                        }
                    }

                    if (!anyMarkerPut){
                        Toast.makeText(baseContext, "No hay gimnasios cercanos", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                // Handle errors here
            }
        }
    }


    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lon1, lat2, lon2, results)
        return results[0]
    }


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

    private fun stopLocationUpdates() {
        locationClient.removeLocationUpdates(locationCallback)
    }

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
            startLocationUpdates()
        }
    }

    // Handle location settings
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

    private fun bitmapDescriptorFromVector(context : Context, vectorResId : Int) : BitmapDescriptor {
        val vectorDrawable : Drawable = ContextCompat.getDrawable(context, vectorResId)!!
        vectorDrawable.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun onResume() {
        super.onResume()
        locationSettings()
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }
}

