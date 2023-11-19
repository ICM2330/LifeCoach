package com.example.lifecoach_.repositories

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MsgRepository {
    private val db = Firebase.firestore
    private val msgRef = db.collection("msgs")

    fun sendMessage(from: String, to: String, msg: String, callback: () -> Unit) {
        msgRef.add(hashMapOf(
            "from" to from,
            "to" to to,
            "msg" to msg
        )).addOnSuccessListener {
            callback()
        }
    }
}