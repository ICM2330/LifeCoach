package com.example.lifecoach_.activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.lifecoach_.R
import com.example.lifecoach_.databinding.ActivityFollowFriendBinding
import com.example.lifecoach_.model.Friend
import com.example.lifecoach_.model.User
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
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
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode

class FollowFriendActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityFollowFriendBinding

    // DB Management
    private lateinit var userFriend: User
    private lateinit var db: FirebaseFirestore

    // Map attributes
    private lateinit var mMap: GoogleMap

    // Location attributes
    private lateinit var locationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private var lastLocation: Location? = null
    private var lastLocationMarker: Marker? = null
    private var friendLastLatLng: LatLng? = null
    private var friendLocationMarker: Marker? = null

    // Auth / Current user attributes
    private lateinit var auth : FirebaseAuth

    private val getPermissionLocation =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startLocationUpdates()
            }
        }

    // Management of FireStore
    private fun setupFireStore() {
        db = FirebaseFirestore.getInstance()
        val usersRef = db.collection("users")
        val query = usersRef.whereEqualTo("username", userFriend.username)

        val listener = { value: QuerySnapshot ->
            if (value.documents.size >= 1) {
                val doc = value.documents[0]
                val latitude = doc["latitude"] as Double
                val longitude = doc["longitude"] as Double
                Log.i("FRIEND", "Lat: $latitude, Long: $longitude")

                if (latitude != 360.0 && longitude != 360.0) {
                    friendLastLatLng = LatLng(latitude, longitude)
                    updateFriendLocationOnMap()
                    if (lastLocation != null) {
                        val distance = drawRouteBetweenTwoLocations(
                            LatLng(lastLocation!!.latitude, lastLocation!!.longitude),
                            LatLng(latitude, longitude)
                        )
                        binding.nameUserFollowing.text = "${userFriend.username}"
                        binding.distanceFollowing.text = "$distance mts"
                    }
                } else {
                    binding.nameUserFollowing.text = "${userFriend.username}"
                    binding.distanceFollowing.text = "0 mts"
                }
            }
        }
        query.get()
            .addOnSuccessListener {
                listener(it)
            }

        query.addSnapshotListener { value, _ ->
            if (value != null) {
                listener(value)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFollowFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the friend's from the intent
        val friend = intent.getSerializableExtra("friend") as Friend
        userFriend = friend.user

        auth = FirebaseAuth.getInstance()

        setupFireStore()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFollow) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Location features
        locationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
            .setWaitForAccurateLocation(true).setMinUpdateIntervalMillis(5000).build()

        // TODO : Callback
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: com.google.android.gms.location.LocationResult) {
                super.onLocationResult(locationResult)
                if (locationResult.lastLocation != null) {
                    val currentLocation = locationResult.lastLocation

                    if (lastLocation == null) {
                        lastLocation = currentLocation
                        updateLocationOnMap()

                        // TODO : Update the location on the DB
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
                                    // Draw the route
                                    val distance = drawRouteBetweenTwoLocations(
                                        LatLng(currentLocation.latitude, currentLocation.longitude),
                                        LatLng(friendLastLatLng!!.latitude, friendLastLatLng!!.longitude)
                                    )
                                    binding.nameUserFollowing.text = "${userFriend.username}"
                                    binding.distanceFollowing.text = "$distance mts"
                                }
                            }
                    }
                    else{
                        if (locationResult.lastLocation!!.distanceTo(lastLocation!!) > 30){
                            lastLocation = locationResult.lastLocation

                            updateLocationOnMap()

                            // TODO : Update the location on the DB
                            val usersRef = db.collection("users")
                            val query = usersRef.whereEqualTo("uid", auth.currentUser?.uid)
                            query.get()
                                .addOnSuccessListener {
                                    if (it.documents.size >= 1) {
                                        val doc = it.documents[0]
                                        doc.reference.update(
                                            mapOf(
                                                "latitude" to currentLocation!!.latitude,
                                                "longitude" to currentLocation!!.longitude
                                            )
                                        )
                                        // Draw the route
                                        val distance = drawRouteBetweenTwoLocations(
                                            LatLng(currentLocation.latitude, currentLocation.longitude),
                                            LatLng(friendLastLatLng!!.latitude, friendLastLatLng!!.longitude)
                                        )
                                        binding.follow.text = "Following: ${userFriend.username}"
                                        binding.distanceFollowing.text = "Distance: $distance mts"
                                    }
                                }
                        }
                    }
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    private var polyLines = mutableListOf<Polyline>()
    private fun drawRouteBetweenTwoLocations(
        origin: LatLng,
        destination: LatLng
    ): Double {
        val apiKey = getString(R.string.google_maps_key)
        val geoContext = GeoApiContext.Builder()
            .apiKey(apiKey)
            .build()

        val directionsResult: DirectionsResult = DirectionsApi.newRequest(geoContext)
            .origin(origin.latitude.toString() + "," + origin.longitude.toString())
            .destination(destination.latitude.toString() + "," + destination.longitude.toString())
            .mode(TravelMode.WALKING)
            .await()

        // Clear the list of polyline
        for (polyline in polyLines) {
            polyline.remove()
        }

        // Draw the polyline
        val polylineOptions = PolylineOptions()

        if (directionsResult.routes.isNotEmpty()) {
            val route = directionsResult.routes[0].overviewPolyline.decodePath()
            for (point in route) {
                polylineOptions.add(LatLng(point.lat, point.lng))
            }

            val polyline = mMap.addPolyline(polylineOptions)
            polyline.color = getColor(R.color.green2)
            polyLines.add(polyline)

            // Get the distance between the two points
            val distance = directionsResult.routes[0].legs[0].distance.inMeters
            return distance.toDouble()
        }
        return 0.0
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

    private fun updateFriendLocationOnMap() {
        friendLastLatLng?.let {
            if (::mMap.isInitialized) {
                if (friendLocationMarker == null) {
                    friendLocationMarker = mMap.addMarker(
                        MarkerOptions()
                            .position(it)
                            .title("Friend Location")
                            .icon(bitmapDescriptorFromVector(baseContext, R.drawable.friendpin))
                    )

                } else {
                    friendLocationMarker?.position = it
                }
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

    private val locationRequestCode = 1001

    private fun showLocationPermissionDialog() {
        if (!isLocationEnabled()) {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("Permiso de Ubicación")
            alertDialog.setMessage("Localización no encendida, enciendela para usar la aplicación con sus funcionalidades")
            alertDialog.setPositiveButton("Configuración") { _, _ ->
                val intent = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivityForResult(intent, locationRequestCode)
            }
            alertDialog.setNegativeButton("Cancelar") { dialog, _ ->
                dialog.cancel()
            }
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
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }
}