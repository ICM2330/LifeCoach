package com.example.lifecoach_.activities.habits.auxiliar

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.lifecoach_.R
import com.example.lifecoach_.databinding.ActivitySearchGymForHabitBinding
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.JsonParser
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt


class SearchGymForHabitActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivitySearchGymForHabitBinding
    private lateinit var mMap: GoogleMap

    private lateinit var locationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private var lastLocation: Location? = null
    private var lastLocationMarker: Marker? = null
    private var gymMarkers: MutableList<Marker> = mutableListOf()

    private val listGym = mutableListOf<Gym>()

    // Light Sensor variables
    private lateinit var sensorManager: SensorManager
    private lateinit var lightSensor: Sensor
    private lateinit var lightEventListener: SensorEventListener

    // DB Management
    private lateinit var db : FirebaseFirestore

    // Auth attribute
    private lateinit var auth : FirebaseAuth

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

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize Firebase Database
        db = FirebaseFirestore.getInstance()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapsFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Location features
        locationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
            .setWaitForAccurateLocation(true).setMinUpdateIntervalMillis(5000).build()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                if (locationResult.lastLocation != null) {
                    val currentLocation = locationResult.lastLocation!!

                    if (lastLocation == null) {
                        // If it's the first location, save it
                        lastLocation = currentLocation
                        updateLocationOnMap()
                        val usersRef = db.collection("users")
                        val query = usersRef.whereEqualTo("uid", auth.currentUser?.uid)
                        query.get()
                            .addOnSuccessListener {
                                if (it.documents.size >= 1) {
                                    val doc = it.documents[0]
                                    doc.reference.update(
                                        mapOf(
                                            "latitude" to currentLocation!!.latitude,
                                            "longitude" to currentLocation.longitude
                                        )
                                    )
                                }
                            }
                        consumeRestVolley()
                    } else {
                        if(distance(locationResult.lastLocation!!, lastLocation!!) > 30) {
                            // If the location is different from the last one, update it
                            lastLocation = locationResult.lastLocation
                            // Update the location on the map
                            updateLocationOnMap()
                            // Quit the lines of the route
                            for (polyline in currentPolylines) {
                                polyline.remove()
                            }

                            // Update the location on the DB
                            val usersRef = db.collection("users")
                            val query = usersRef.whereEqualTo("uid", auth.currentUser?.uid)
                            query.get()
                                .addOnSuccessListener {
                                    if (it.documents.size >= 1) {
                                        val doc = it.documents[0]
                                        doc.reference.update(
                                            mapOf(
                                                "latitude" to currentLocation!!.latitude,
                                                "longitude" to currentLocation.longitude
                                            )
                                        )
                                    }
                                }

                            // Here quit the markers of the gyms
                            consumeRestVolley()
                        }
                    }
                }
            }
        }

        // Management of the sensor
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)!!
        lightEventListener = createLightSensorListener()
    }

    override fun onMapReady(gMap: GoogleMap) {
        mMap = gMap
        mMap.uiSettings.setAllGesturesEnabled(true)

        Places.initialize(this, getString(R.string.google_maps_key))

        //Set the default style
        mMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                baseContext, R.raw.lightmodemap
            )
        )

        manageButtons()
    }

    private fun updateLocationOnMap() {
        lastLocation?.let {
            val latLng = LatLng(it.latitude, it.longitude)
            if (::mMap.isInitialized) {
                if (lastLocationMarker == null) {
                    lastLocationMarker = mMap.addMarker(
                        MarkerOptions()
                            .position(latLng)
                            .title("Last Location")
                            .icon(bitmapDescriptorFromVector(baseContext, R.drawable.userpin))
                    )
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                } else {
                    lastLocationMarker?.position = latLng
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                }
            }
        }
    }

    private fun manageButtons() {
        drawRouteClickedMarker()
    }

    private val currentPolylines = mutableListOf<Polyline>()
    private fun drawRouteClickedMarker() {
        mMap.setOnMarkerClickListener { clickedMarker ->
            for (gymMarker in gymMarkers) {
                if (clickedMarker.position == gymMarker.position) {
                    // Delete the Polyline if it exists
                    for (polyline in currentPolylines) {
                        polyline.remove()
                    }

                    // Show the snippet
                    clickedMarker.showInfoWindow()

                    // ----------- DRAW ROUTE -------------//
                    // Draw the roue between the two points using the Google Directions API
                    val apiKey = getString(R.string.google_maps_key)
                    val geoContext = GeoApiContext.Builder()
                        .apiKey(apiKey)
                        .build()

                    val directionsResult: DirectionsResult = DirectionsApi.newRequest(geoContext)
                        .origin("${lastLocationMarker!!.position.latitude},${lastLocationMarker!!.position.longitude}")
                        .destination("${clickedMarker.position.latitude},${clickedMarker.position.longitude}")
                        .mode(TravelMode.WALKING)
                        .await()

                    // Draw the polyline
                    val polylineOptions = PolylineOptions()

                    if (directionsResult.routes.isNotEmpty()) {
                        val route = directionsResult.routes[0].overviewPolyline.decodePath()
                        for (point in route) {
                            polylineOptions.add(LatLng(point.lat, point.lng))
                        }
                        val polyline = mMap.addPolyline(polylineOptions)
                        polyline.color = Color.GREEN
                        currentPolylines.add(polyline)

                        // Get the distance and convert it to kilometers
                        val distance = directionsResult.routes[0].legs[0].distance.inMeters

                        // Show a Toast with the distance
                        Toast.makeText(
                            baseContext,
                            "Distancia hacía ${gymMarker.title}: $distance mts",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
            true
        }
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

    private val locationRequestCode = 1001

    private fun showLocationPermissionDialog() {
        if (!isLocationEnabled()) {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("Permiso de Ubicación")
            alertDialog.setMessage("Localización no encendida, enciendela para usar la aplicación con sus funcionalidades")
            alertDialog.setPositiveButton("OK") { _, _ ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivityForResult(intent, locationRequestCode)
            }
            alertDialog.setNegativeButton("Cancelar") { dialog, _ ->
                dialog.cancel()
                Toast.makeText(this, "Busqueda de gimnasios no habilitada.", Toast.LENGTH_LONG).show()
            }
            val alert = alertDialog.create()
            alert.show()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == locationRequestCode) {
            if (isLocationEnabled()) {
                showLocationPermissionDialog()
            } else {
                Toast.makeText(this, "Location is still disabled.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun locationSettings() {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener {
            checkLocationPermission()
        }
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                // Show the user a dialog to change location settings
                try {
                    showLocationPermissionDialog()
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error
                }
            }
        }
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor {
        val vectorDrawable: Drawable = ContextCompat.getDrawable(context, vectorResId)!!
        vectorDrawable.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun onResume() {
        super.onResume()
        locationSettings()
        sensorManager.registerListener(
            lightEventListener, lightSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
        sensorManager.unregisterListener(lightEventListener)
    }

    private fun consumeRestVolley() {
        val queue = Volley.newRequestQueue(this)
        val url =
            "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=${lastLocation!!.latitude},${lastLocation!!.longitude}&radius=3000&type=gym&key=${
                getString(R.string.google_maps_key)
            }"
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                responseToGymList(response.toString())
                for (marker in gymMarkers)
                    marker.remove()
                gymMarkers.clear()
                for (gym in listGym)
                    gymMarkers.add(putGymMarker(gym))
            },
            { Log.i("Volley", "Doesn't works") })

        queue.add(stringRequest)
    }

    private fun putGymMarker(gym: Gym): Marker {
        // Add a marker in default location
        val location = LatLng(gym.lat, gym.lon)
        return mMap.addMarker(
            MarkerOptions().position(location)
                .title(gym.name)
                .alpha(1F)
                .icon(bitmapDescriptorFromVector(baseContext, R.drawable.pesoicon))
        )!!
    }

    private fun responseToGymList(response: String) {
        val jsonObject = JsonParser.parseString(response).asJsonObject
        val results = jsonObject.getAsJsonArray("results")
        listGym.clear()
        for (result in results) {
            val geometry = result.asJsonObject.getAsJsonObject("geometry")
            val location = geometry.getAsJsonObject("location")
            listGym.add(
                Gym(
                    result.asJsonObject.get("name").asString,
                    location.get("lat").asDouble,
                    location.get("lng").asDouble
                )
            )
        }
    }

    // Method for managing the sensor listener
    private fun createLightSensorListener(): SensorEventListener {
        val ret: SensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event != null && ::mMap.isInitialized) {
                    if (event.values[0] < 5000) {
                        mMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                baseContext, R.raw.darkmodemap
                            )
                        )
                    } else {
                        mMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                baseContext, R.raw.lightmodemap
                            )
                        )
                    }
                }
            }

            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
            }
        }
        return ret
    }

    private fun distance(location1: Location, location2: Location): Double {
        val lat1 = location1.latitude
        val lat2 = location2.latitude
        val long1 = location1.longitude
        val long2 = location2.longitude
        val radius = 6371
        val latDistance = Math.toRadians(lat1 - lat2)
        val lngDistance = Math.toRadians(long1 - long2)
        val a = sin(latDistance / 2) * sin(latDistance / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(lngDistance / 2) * sin(lngDistance / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        val result = radius * c
        return ((result * 100.0).roundToInt() / 100.0) * 1000.0
    }

}

class Gym(val name: String, val lat: Double, val lon: Double) {
    override fun toString(): String {
        return "Gym(name='$name', lat=$lat, lon=$lon)"
    }
}
