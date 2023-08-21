package com.example.lifecoach_.activities

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lifecoach_.model.User
import com.example.lifecoach_.databinding.ActivityMainBinding
import com.example.lifecoach_.activities.friends.ChatMenuActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var uriImage : Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        buttonsManager()
        /*intent = Intent(this, ChatMenuActivity::class.java)
        startActivity(intent)*/
    }

    fun buttonsManager (){
        //Button of attach photo from the registering proccess
        binding.headercameraButton.setOnClickListener {
            getContentGallery.launch("image/*")
        }

        //Button of Registering
        binding.registerButton.setOnClickListener {
            if (!blankSpaces()) {
                //If there is not any blank or nut spaces, register and verify the user
                var userProof = getUserProof()
                intent = Intent(this, ProfileActivity::class.java)
                intent.putExtra("user", userProof)
                startActivity(intent)
            }
            else{
                //Say that is not possible to do the register
                Toast.makeText(this, "No puedes dejar campos de registro vacíos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getUserProof () : User{
        var user = User (binding.nameRegister.text.toString(), binding.userRegister.text.toString(),
            binding.emailRegister.text.toString(), binding.phoneRegister.text.toString().toLong())
        return user
    }

    fun blankSpaces () : Boolean{
        if (binding.nameRegister.text.toString().isNullOrBlank() || binding.userRegister.text.toString().isNullOrBlank() ||
            binding.emailRegister.text.toString().isNullOrBlank() || binding.phoneRegister.text.toString().isNullOrBlank()
        )
            return true
        else
            return false
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