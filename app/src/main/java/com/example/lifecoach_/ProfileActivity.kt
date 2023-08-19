package com.example.lifecoach_

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lifecoach_.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        manageButtons(binding)
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
                R.id.menuFriends -> {
                    // Do an intent with the friends activity
                    val intent = Intent(baseContext, FriendActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }
}