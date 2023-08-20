package com.example.lifecoach_

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lifecoach_.Model.User
import com.example.lifecoach_.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Fill the info. with the login activity
        val userProof = intent.getSerializableExtra("user") as User
        fullInformation(binding, userProof)

        //Instructions of buttons
        manageButtons(binding)
    }

    fun fullInformation (binding: ActivityProfileBinding, user : User){
        binding.emailProfile.setText(user.email)
        binding.nameProfile.setText(user.name)
        binding.userProfile.setText(user.username)

        var stringPhone = user.phone.toString()
        binding.phoneProfile.setText(stringPhone)
    }



    fun manageButtons(binding: ActivityProfileBinding){
        bottomNavigationBarManagement(binding)
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
}