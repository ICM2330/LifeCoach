package com.example.lifecoach_.repositories

import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class PicRepository {
    private val storage: FirebaseStorage = Firebase.storage
    private var imagesRef = storage.reference.child("images")

    fun saveImage(uri: Uri, callback: (String) -> Unit) {
        val nombreArchivo = UUID.randomUUID().toString() + ".jpg"
        val imgRef = imagesRef.child(nombreArchivo)
        imgRef.putFile(uri)
            .addOnSuccessListener {
                callback(imgRef.path)
            }
    }
}