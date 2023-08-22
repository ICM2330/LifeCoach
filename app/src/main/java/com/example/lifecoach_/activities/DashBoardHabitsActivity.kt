package com.example.lifecoach_.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lifecoach_.R
import com.example.lifecoach_.activities.friends.ChatMenuActivity
import com.example.lifecoach_.activities.habits.CreateHabitsActivity
import com.example.lifecoach_.databinding.ActivityDashBoardHabitsBinding
import com.example.lifecoach_.databinding.ActivityProfileBinding
import com.example.lifecoach_.model.User

class DashBoardHabitsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDashBoardHabitsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashBoardHabitsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Fill the info. with the login activity
        val userProof = intent.getSerializableExtra("user") as User
        manageButtons(userProof)
    }

    fun manageButtons(user : User){
        CreateHabit(user)
        bottomNavigationBarManagement(user)
        viewSteps(user)
        viewFriends(user)
    }

    fun viewSteps (user : User){
        binding.buttonStepsDashboardButton.setOnClickListener{

        }
    }

    fun viewFriends(user : User){
        binding.buttonFriendsDashboard.setOnClickListener {
            val intent = Intent(baseContext, ChatMenuActivity::class.java)
            startActivity(intent)
        }
    }
    fun CreateHabit(user : User){
        binding.createHabitButton.setOnClickListener {
            intent = Intent(baseContext, CreateHabitsActivity::class.java)
            startActivity(intent)
        }
    }

    fun bottomNavigationBarManagement(user : User) {
        binding.bottomNavigationViewCreate.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuProfile -> {
                    //Do an intent with the profile activity
                    val intent = Intent(this, ProfileActivity::class.java)
                    intent.putExtra("user", user)
                    startActivity(intent)
                    true
                }

                R.id.menuChat -> {
                    // Do an intent with the chat activity
                    val intent = Intent(this, ChatMenuActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.menuHabits -> {
                    // Do an intent with the dashboard of habits activity
                    val intent = Intent(this, DashBoardHabitsActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
    }
}