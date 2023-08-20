package com.example.lifecoach_

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lifecoach_.model.User
import com.example.lifecoach_.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var uriImage : Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        buttonsManager()
    }

    fun buttonsManager (){
        //Button of attach photo from the registering proccess
        binding.headercameraButton.setOnClickListener {
            getContentGallery.launch("image/*")
        }

        //Button of Registering
        binding.registerButton.setOnClickListener {
            var userProof = getUserProof()
            intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("user", userProof)
            startActivity(intent)
        }
    }

    fun getUserProof () : User{
        return User(binding.nameRegister.text.toString(), binding.userRegister.text.toString(),
            binding.emailRegister.text.toString(), binding.phoneRegister.text.toString().toLong())
    }

    val getContentGallery = registerForActivityResult(
        ActivityResultContracts.GetContent(),
        ActivityResultCallback {
            if (it != null) {
                loadImage(it)
            }
        }
    )

    fun loadImage (uri : Uri){
        uriImage = uri
        val imageStream = getContentResolver().openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(imageStream)
        binding.photoload.setImageBitmap(bitmap)
    }
}