package com.example.lifecoach_.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lifecoach_.R
import com.example.lifecoach_.databinding.ActivityFollowFriendBinding
import com.example.lifecoach_.model.Friend
import com.example.lifecoach_.model.User
import com.google.android.gms.maps.GoogleMap
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class FollowFriendActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFollowFriendBinding

    private lateinit var userFriend : User
    private lateinit var db : FirebaseFirestore


    private var googleMap : GoogleMap? = null

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

        binding.userId.text = userFriend.username
        setupFireStore()

    }
}