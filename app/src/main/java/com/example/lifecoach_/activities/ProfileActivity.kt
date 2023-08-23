package com.example.lifecoach_.activities

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lifecoach_.R
import com.example.lifecoach_.model.User
import com.example.lifecoach_.databinding.ActivityProfileBinding
import com.example.lifecoach_.activities.friends.ChatMenuActivity

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Fill the info. with the login activity
        val userProof = intent.getSerializableExtra("user") as User
        fillInformation(binding, userProof)

        //Instructions of buttons
        manageButtons(binding, userProof)
    }

    private fun fillInformation(binding: ActivityProfileBinding, user: User) {
        binding.emailProfile.setText(user.email)
        binding.nameProfile.setText(user.name)
        binding.userProfile.setText(user.username)

        val stringPhone = user.phone.toString()
        binding.phoneProfile.setText(stringPhone)
    }


    private fun manageButtons(binding: ActivityProfileBinding, user: User) {
        bottomNavigationBarManagement(user)
        uploadInfo(binding, user)
        logOut(binding)
        uploadPhotoProfile(binding)
    }

    private fun uploadPhotoProfile(binding: ActivityProfileBinding) {
        binding.uploadPhotoProfile.setOnClickListener {
            getContentGallery.launch("image/*")
        }
    }

    private fun logOut(binding: ActivityProfileBinding) {
        binding.logOutButtonProfile.setOnClickListener {
            intent = Intent(baseContext, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun uploadInfo(binding: ActivityProfileBinding, user: User) {
        binding.uploadInfoProfileButton.setOnClickListener {
            if (blankSpaces()) {
                Toast.makeText(
                    baseContext,
                    "No es posible actualizar información con campos vacíos",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                user.name = binding.nameProfile.text.toString()
                user.username = binding.userProfile.text.toString()
                user.email = binding.emailProfile.text.toString()
                user.phone = binding.phoneProfile.text.toString().toLong()

                Toast.makeText(baseContext, "Datos cambiados con éxito", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun blankSpaces(): Boolean {
        return binding.emailProfile.toString().isBlank() || binding.nameProfile.text.toString()
            .isBlank() ||
                binding.userProfile.text.toString()
                    .isBlank() || binding.phoneProfile.text.toString().isBlank()
    }


    private fun bottomNavigationBarManagement(user: User) {
        binding.bottomNavigationViewCreate.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuChat -> {
                    // Do an intent with the chat activity
                    val intent = Intent(this, ChatMenuActivity::class.java)
                    startActivity(intent)
                    false
                }

                R.id.menuHabits -> {
                    // Do an intent with the dashboard of habits activity
                    val intent = Intent(this, DashBoardHabitsActivity::class.java)
                    intent.putExtra("user", user)
                    startActivity(intent)
                    finish()
                    false
                }

                else -> false
            }
        }
    }

    private val getContentGallery = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        if (it != null) {
            loadImage(it)
        }
    }

    private fun loadImage(uri: Uri) {
        val imageStream = contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(imageStream)
        binding.dbprofileimage.setImageBitmap(bitmap)
    }
}