package com.example.lifecoach_.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.lifecoach_.R
import com.example.lifecoach_.activities.friends.ChatMenuActivity
import com.example.lifecoach_.activities.habits.CreateHabitsActivity
import com.example.lifecoach_.adapters.FriendChatAdapter
import com.example.lifecoach_.adapters.HabitListViewAdapter
import com.example.lifecoach_.databinding.ActivityDashBoardHabitsBinding
import com.example.lifecoach_.databinding.ActivityProfileBinding
import com.example.lifecoach_.model.Friend
import com.example.lifecoach_.model.User
import com.example.lifecoach_.model.habits.Frecuency
import com.example.lifecoach_.model.habits.Habit
import com.example.lifecoach_.model.habits.RunningHabit
import com.example.lifecoach_.model.habits.StepsHabit
import com.example.lifecoach_.model.habits.TimeControlHabit
import java.time.Duration
import java.time.format.DateTimeFormatter
import java.util.Date

class DashBoardHabitsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDashBoardHabitsBinding
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashBoardHabitsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Fill the info. with the login activity
        val userProof = intent.getSerializableExtra("user") as User
        manageButtons(userProof)

        userProof.habits = createHabits()

        val adapter = HabitListViewAdapter(this, userProof.habits)
        binding.habitsListView.adapter = adapter

        
    }


    //Create habits for show
    @RequiresApi(Build.VERSION_CODES.O)
    fun createHabits () : MutableList<Habit>{
        var habits: MutableList<Habit> = mutableListOf()

        //Create habit of steps
        var listHistory : MutableList<DateTimeFormatter> = mutableListOf()
        val duration : Duration = Duration.ofDays(1)
        var frecuency = Frecuency(Date(), duration)
        var accomplishment : MutableList<Int> = mutableListOf()
        var habitSteps = StepsHabit("Pasos", listHistory, frecuency,  30, accomplishment)

        //Create habit of frecuency
        var durationReading : Duration = Duration.ofDays(2)
        var accomplishmentReading : MutableList<Duration> = mutableListOf()
        var habitTimeControl = TimeControlHabit("Leer", listHistory, frecuency, durationReading, accomplishmentReading)

        //Create habit of running
        var durationRunning : Duration = Duration.ofMinutes(55)
        var accomplishmentRunning : MutableList<Duration> = mutableListOf()
        var habitRunning = RunningHabit("Correr", listHistory, frecuency, null,null, durationRunning, accomplishmentRunning, 12000)


        //Insert Habits to list
        habits.add(habitSteps)
        habits.add(habitTimeControl)
        habits.add(habitRunning)
        habits.add(habitSteps)
        habits.add(habitTimeControl)
        habits.add(habitRunning)

        //Return the habits list
        return habits
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