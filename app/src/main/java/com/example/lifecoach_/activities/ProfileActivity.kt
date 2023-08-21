package com.example.lifecoach_.activities

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
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

    fun fillInformation (binding: ActivityProfileBinding, user : User){
        binding.emailProfile.setText(user.email)
        binding.nameProfile.setText(user.name)
        binding.userProfile.setText(user.username)

        var stringPhone = user.phone.toString()
        binding.phoneProfile.setText(stringPhone)
    }



    fun manageButtons(binding: ActivityProfileBinding, user : User){
        bottomNavigationBarManagement(binding)
        uploadInfo(binding, user)
        logOut(binding)
        uploadPhotoProfile(binding)
    }

    fun uploadPhotoProfile (binding: ActivityProfileBinding){
        binding.uploadPhotoProfile.setOnClickListener {
            getContentGallery.launch("image/*")
        }
    }

    fun logOut (binding : ActivityProfileBinding){
        binding.logOutButtonProfile.setOnClickListener {
            intent = Intent(baseContext, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun uploadInfo (binding : ActivityProfileBinding, user : User) {
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

    fun blankSpaces () : Boolean{
        if (binding.emailProfile.toString().isNullOrBlank() || binding.nameProfile.text.toString().isNullOrBlank() ||
            binding.userProfile.text.toString().isNullOrBlank() || binding.phoneProfile.text.toString().isNullOrBlank()
        )
            return true
        else
            return false
    }



    fun bottomNavigationBarManagement(binding: ActivityProfileBinding) {
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuProfile -> {
                    //Do an intent with the profile activity
                    val intent = Intent(baseContext, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menuChat -> {
                    // Do an intent with the chat activity
                    val intent = Intent(baseContext, ChatMenuActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menuHabits -> {
                    // Do an intent with the dashboard of habits activity
                    val intent = Intent(baseContext, DashBoardHabitsActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
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
        val imageStream = getContentResolver().openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(imageStream)
        binding.profileimage.setImageBitmap(bitmap)
    }
}