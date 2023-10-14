package com.example.lifecoach_.activities

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.lifecoach_.R
import com.example.lifecoach_.activities.friends.ChatMenuActivity
import com.example.lifecoach_.activities.habits.CreateHabitsActivity
import com.example.lifecoach_.activities.habits.view.GenericHabitViewActivity
import com.example.lifecoach_.activities.habits.view.MuscularHabitViewActivity
import com.example.lifecoach_.activities.habits.view.RunningHabitViewActivity
import com.example.lifecoach_.activities.habits.view.StepHabitViewActivity
import com.example.lifecoach_.activities.habits.view.TimeHabitViewActivity
import com.example.lifecoach_.adapters.HabitListViewAdapter
import com.example.lifecoach_.databinding.ActivityDashBoardHabitsBinding
import com.example.lifecoach_.model.User
import com.example.lifecoach_.model.habits.Frequency
import com.example.lifecoach_.model.habits.Habit
import com.example.lifecoach_.model.habits.RunningHabit
import com.example.lifecoach_.model.habits.StepsHabit
import com.example.lifecoach_.model.habits.StrengthHabit
import com.example.lifecoach_.model.habits.TimeControlHabit
import java.time.Duration
import java.time.format.DateTimeFormatter
import java.util.Date

class DashBoardHabitsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashBoardHabitsBinding
    private lateinit var uriImage: Uri

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashBoardHabitsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Fill the info. with the login activity
        val userTest = intent.getSerializableExtra("user") as User
        manageButtons(userTest)
        // userTest.habits = createHabits()
        val adapter = HabitListViewAdapter(this, userTest.habits)
        binding.habitsListView.adapter = adapter
        binding.habitsListView.setOnItemClickListener { parent, view, position, id ->
            when (userTest.habits[position]) {
                is StrengthHabit -> {
                    val intent = Intent(baseContext, MuscularHabitViewActivity::class.java)
                    startActivity(intent)
                }

                is RunningHabit -> {
                    val intent = Intent(baseContext, RunningHabitViewActivity::class.java)
                    startActivity(intent)
                }

                is StepsHabit -> {
                    val intent = Intent(baseContext, StepHabitViewActivity::class.java)
                    startActivity(intent)
                }

                is TimeControlHabit -> {
                    val intent = Intent(baseContext, TimeHabitViewActivity::class.java)
                    startActivity(intent)
                }

                else -> {
                    val intent = Intent(baseContext, GenericHabitViewActivity::class.java)
                    startActivity(intent)
                }
            }

        }
    }

    //Create habits for show
    @RequiresApi(Build.VERSION_CODES.O)
    fun createHabits(): MutableList<Habit> {
        val habits: MutableList<Habit> = mutableListOf()

        //Insert Habits to list

        //Return the habits list
        return habits
    }

    fun manageButtons(user: User) {
        CreateHabit(user)
        bottomNavigationBarManagement(user)
    }

    fun CreateHabit(user: User) {
        binding.createHabitButton.setOnClickListener {
            intent = Intent(baseContext, CreateHabitsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun bottomNavigationBarManagement(user: User) {
        binding.bottomNavigationViewCreate.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuProfile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    intent.putExtra("user", user)
                    startActivity(intent)
                    finish()
                    false
                }

                R.id.menuChat -> {
                    // Do an intent with the chat activity
                    val intent = Intent(this, ChatMenuActivity::class.java)
                    startActivity(intent)
                    false
                }

                else -> false
            }
        }
    }
}