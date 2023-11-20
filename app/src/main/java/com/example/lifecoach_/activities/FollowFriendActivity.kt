package com.example.lifecoach_.activities

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lifecoach_.R
import com.example.lifecoach_.databinding.ActivityFollowFriendBinding
import com.example.lifecoach_.model.Friend
import com.example.lifecoach_.model.User
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode

class FollowFriendActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding : ActivityFollowFriendBinding

    // DB Management
    private lateinit var userFriend : User
    private lateinit var db : FirebaseFirestore

    // Map attributes
    private lateinit var mMap : GoogleMap

    // Location attributes
    private lateinit var locationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private var lastLocationMarker: Marker? = null
    private var friendLastLatLng: LatLng? = null
    private var friendLocationMarker: Marker? = null

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

        val listener = { value : QuerySnapshot ->
            if (value.documents.size >= 1){
                val doc = value.documents[0]
                val latitude = doc["latitude"] as Double
                val longitude = doc["longitude"] as Double
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

        setupFireStore()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFollow) as SupportMapFragment
        mapFragment.getMapAsync (this)

        // Location features
        locationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
            .setWaitForAccurateLocation(true).setMinUpdateIntervalMillis(5000).build()

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    private var polyLines = mutableListOf<Polyline>()
    private fun drawRouteBetweenTwoLocations(
        origin : LatLng,
        destination : LatLng
    ) : Double {
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
}