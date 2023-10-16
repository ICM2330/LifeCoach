package com.example.lifecoach_.activities

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lifecoach_.model.User
import com.example.lifecoach_.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var uriImage: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        buttonsManager()
    }

    private fun buttonsManager() {
        //Button of attach photo from the registering proccess
        binding.headercameraButton.setOnClickListener {
            getContentGallery.launch("image/*")
        }

        //Button of Registering
        binding.registerButton.setOnClickListener {
            if (!blankSpaces()) {
                //If there is not any blank or nut spaces, register and verify the user
                val userTest = getUserTest()
                intent = Intent(this, DashBoardHabitsActivity::class.java)
                intent.putExtra("user", userTest)
                startActivity(intent)
                finish()
            } else {
                //Say that is not possible to do the register
                Toast.makeText(
                    this,
                    "No puedes dejar campos de registro vacíos",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getUserTest(): User {
        val user = User(
            binding.nameRegister.text.toString(), binding.userRegister.text.toString(),
            binding.emailRegister.text.toString(), binding.phoneRegister.text.toString().toLong()
        )

        if (uriImage != null) {
            user.picture = uriImage.toString()
        }
        return user
    }

    private fun blankSpaces(): Boolean {
        return binding.nameRegister.text.toString()
            .isBlank() || binding.userRegister.text.toString().isBlank() ||
                binding.emailRegister.text.toString()
                    .isBlank() || binding.phoneRegister.text.toString().isBlank()
    }

    private val getContentGallery = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        if (it != null) {
            loadImage(it)
        }
    }

    private fun loadImage(uri: Uri) {
        uriImage = uri
        val imageStream = contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(imageStream)
        binding.photoload.setImageBitmap(bitmap)
    }
}