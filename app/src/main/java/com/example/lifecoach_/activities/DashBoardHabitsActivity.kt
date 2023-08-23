package com.example.lifecoach_.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.lifecoach_.R
import com.example.lifecoach_.activities.friends.ChatActivity
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
import com.example.lifecoach_.model.habits.StrengthHabit
import com.example.lifecoach_.model.habits.TimeControlHabit
import java.time.Duration
import java.time.format.DateTimeFormatter
import java.util.Date

class DashBoardHabitsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashBoardHabitsBinding

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

        binding.habitsListView.setOnItemClickListener { parent, view, position, id ->
            when (userProof.habits[position]) {
                is RunningHabit -> {
                    //TODO : INCLUDE THE OTHER TYPES OF HABITS
                    /*
                    val intent = Intent(baseContext, ChatActivity::class.java)
                    intent.putExtra("habits", userProof.habits[position])
                    startActivity(intent)
                     */
                }
                is StrengthHabit -> {
                    //TODO : INCLUDE THE OTHER TYPES OF HABITS
                    /*
                    val intent = Intent(baseContext, ChatActivity::class.java)
                    intent.putExtra("habits", userProof.habits[position])
                    startActivity(intent)
                     */
                }
            }

        }
    }


    //Create habits for show
    @RequiresApi(Build.VERSION_CODES.O)
    fun createHabits(): MutableList<Habit> {
        val habits: MutableList<Habit> = mutableListOf()

        val listHistory: MutableList<DateTimeFormatter> = mutableListOf()
        val duration: Duration = Duration.ofDays(1)
        val frecuency = Frecuency(Date(), duration)

        //Insert Habits to list
        habits.add(StepsHabit("Pasos", listHistory, frecuency, 30, mutableListOf()))
        habits.add(
            TimeControlHabit(
                "Leer", listHistory, frecuency, Duration.ofDays(2), mutableListOf()
            )
        )
        habits.add(
            RunningHabit(
                "Correr",
                listHistory,
                frecuency,
                null,
                null,
                Duration.ofMinutes(55),
                mutableListOf(),
                12000
            )
        )
        habits.add(Habit("Hacer tarea", listHistory, frecuency))
        habits.add(StrengthHabit("Brazo", listHistory, frecuency, "Brazo", mutableListOf()))
        habits.add(
            RunningHabit(
                "Correr",
                listHistory,
                frecuency,
                null,
                null,
                Duration.ofMinutes(55),
                mutableListOf(),
                12000
            )
        )

        //Return the habits list
        return habits
    }

    fun manageButtons(user: User) {
        CreateHabit(user)
        bottomNavigationBarManagement(user)
        viewSteps(user)
        viewFriends(user)
    }

    fun viewSteps(user: User) {
        binding.buttonStepsDashboardButton.setOnClickListener {

        }
    }

    fun viewFriends(user: User) {
        binding.buttonFriendsDashboard.setOnClickListener {
            val intent = Intent(baseContext, ChatMenuActivity::class.java)
            startActivity(intent)
        }
    }

    fun CreateHabit(user: User) {
        binding.createHabitButton.setOnClickListener {
            intent = Intent(baseContext, CreateHabitsActivity::class.java)
            startActivity(intent)
        }
    }

    fun bottomNavigationBarManagement(user: User) {
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