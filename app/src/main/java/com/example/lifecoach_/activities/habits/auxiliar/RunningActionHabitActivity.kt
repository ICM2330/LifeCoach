package com.example.lifecoach_.activities.habits.auxiliar

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.os.SystemClock
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.lifecoach_.R
import com.example.lifecoach_.databinding.ActivityRunningActionHabitBinding
import com.example.lifecoach_.sensor_controllers.MotionController
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
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.libraries.places.api.Places
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode

class RunningActionHabitActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityRunningActionHabitBinding

    private lateinit var mMap: GoogleMap

    private lateinit var locationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private var lastLocation: Location? = null
    private var lastLocationMarker: Marker? = null

    private var metersRan = 0

    private val currentPolylines = mutableListOf<Polyline>()

    // Sensor variables
    private lateinit var sensorManager: SensorManager
    private lateinit var lightSensor: Sensor
    private lateinit var lightEventListener: SensorEventListener

    private val getPermissionLocation =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startLocationUpdates()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRunningActionHabitBinding.inflate(layoutInflater)
        setContentView(binding.root) // Obtain the SupportMapFragment and get notified when the map is ready to be used.

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
                    } else {
                        // If it's not the first location, calculate the distance and draw the route
                        val metersRoad = drawRouteBetweenTwoLocations(lastLocation!!, currentLocation)
                        metersRan += metersRoad.toInt()

                        // Update the location
                        lastLocation = currentLocation

                        // Update the text
                        binding.distanceRanModifyText.text = metersRan.toString() + " mts"

                        updateLocationOnMap()
                    }
                }
            }
        }

        // Management of the sensor
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)!!
        lightEventListener = createLightSensorListener()

        configureMotionController()
    }

    private lateinit var motionController: MotionController
    private fun configureMotionController() {
        motionController = MotionController.getMotionController()
        motionController.configureAccelerometer(baseContext)
        motionController.registerMotionListener({
            binding.motionText.text = "Corriendo ..."
        }, {
            binding.motionText.text = "No te detengas. Sigue moviendote!"
        })
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

    // Draw route between two points (Locations)
    private fun drawRouteBetweenTwoLocations(
        originLocation: Location,
        destinationLocation: Location
    ) : Double{
        val apiKey = getString(R.string.google_maps_key)
        val geoContext = GeoApiContext.Builder()
            .apiKey(apiKey)
            .build()

        val directionsResult: DirectionsResult = DirectionsApi.newRequest(geoContext)
            .origin("${originLocation.latitude},${originLocation.longitude}")
            .destination("${destinationLocation.latitude},${destinationLocation.longitude}")
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
            polyline.color = getColor(R.color.green1)
            currentPolylines.add(polyline)

            // Get the distance between the two points
            val distance = directionsResult.routes[0].legs[0].distance.inMeters
            return distance.toDouble()
        }

        return 0.0
    }

    private fun manageButtons() {
        binding.actionsRunningButton.setOnClickListener {
            if (binding.actionsRunningButton.text == "Iniciar") {
                binding.chronometerRunning.base = SystemClock.elapsedRealtime()
                binding.chronometerRunning.start()

                // Change button text
                binding.actionsRunningButton.text = "Detener"
            } else {
                // Stop Running
                binding.chronometerRunning.stop()
                val minutesRanByChrono =
                    (SystemClock.elapsedRealtime() - binding.chronometerRunning.base) / 60000
                val intent = Intent().apply {
                    putExtra("minutesRan", minutesRanByChrono.toInt())
                }
                setResult(RESULT_OK, intent)
                finish()
            }
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

    private val locationSettings = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            checkLocationPermission()
        } else {
            Toast.makeText(baseContext, "Location is required for this app", Toast.LENGTH_LONG)
                .show()
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
            lightEventListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
        sensorManager.unregisterListener(lightEventListener)
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

}