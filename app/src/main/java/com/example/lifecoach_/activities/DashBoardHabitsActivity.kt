package com.example.lifecoach_.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.lifecoach_.R
import com.example.lifecoach_.activities.friends.ChatMenuActivity
import com.example.lifecoach_.activities.habits.CreateHabitsActivity
import com.example.lifecoach_.activities.habits.view.GenericHabitViewActivity
import com.example.lifecoach_.activities.habits.view.MuscularHabitViewActivity
import com.example.lifecoach_.activities.habits.view.RunningHabitViewActivity
import com.example.lifecoach_.activities.habits.view.StepHabitViewActivity
import com.example.lifecoach_.activities.habits.view.TimeHabitViewActivity
import com.example.lifecoach_.adapters.HabitListViewAdapter
import com.example.lifecoach_.controllers.activities_controllers.activity_dashboard.DashBoardHabitsDataController
import com.example.lifecoach_.controllers.activities_controllers.activity_dashboard.DashBoardUserController
import com.example.lifecoach_.databinding.ActivityDashBoardHabitsBinding
import com.example.lifecoach_.model.User
import com.example.lifecoach_.model.habits.Habit
import com.example.lifecoach_.model.habits.RunningHabit
import com.example.lifecoach_.model.habits.StepsHabit
import com.example.lifecoach_.model.habits.StrengthHabit
import com.example.lifecoach_.model.habits.TimeControlHabit
import com.example.lifecoach_.controllers.sensor_controllers.MotionController
import com.example.lifecoach_.controllers.sensor_controllers.ThemeController
import com.example.lifecoach_.notifications.HabitsNotificationService

class DashBoardHabitsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashBoardHabitsBinding
    private lateinit var userTest: User
    private var todayHabits = mutableListOf<Habit>()
    private var otherHabits = mutableListOf<Habit>()
    private var showToday = true

    private lateinit var themeController: ThemeController
    private var selectedHabit = 0

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val habit = intent?.getSerializableExtra("habit") as Habit
                // Crea el nuevo hábito en la base de datos
                dataController.addHabit(habit, userTest) {
                    // Agrega el hábito con el ID generado por Firebase
                    // userTest.habits.add(it)
                    updateHabits()
                }
            }
        }

    private val startForUpdate =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val habit = intent?.getSerializableExtra("habit") as Habit
                // Actualiza el hábito con el ID del objeto
                dataController.updateHabit(habit, userTest) {
                    // Reemplaza con el hábito actualizado
                    userTest.habits[selectedHabit] = habit
                    updateHabits()
                }
            }
        }

    private val dataController: DashBoardHabitsDataController =
        DashBoardHabitsDataController()
    private lateinit var userController: DashBoardUserController

    override fun onCreate(savedInstanceState: Bundle?) {
        //Fill the info. with the login activity
        userTest = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("user") as User
        } else {
            intent.getSerializableExtra("user", User::class.java)!!
        }

        super.onCreate(savedInstanceState)
        binding = ActivityDashBoardHabitsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Fill the info. with the login activity
        userTest = intent.getSerializableExtra("user") as User

        // Grant URI permissions
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

        userController = DashBoardUserController(baseContext, filesDir)

        userTest.uid?.let {uid ->
            userController.updatePictureListener(uid) {
                newUser ->
                val userHabits = userTest.habits
                if (newUser != null) {
                    userTest = newUser
                    // Update photo
                    updatePhoto()
                }
                userTest.habits = userHabits
            }
        }

        // Set Firebase Data Update Listeners
        userTest.uid?.let { uid -> dataController.updatesListener(uid) {
            habits ->
                userTest.habits = habits
                updateHabits()
            }
        }

        // Set click listeners
        manageButtons(userTest)
    }

    private lateinit var enableDarkMode: () -> Unit
    private lateinit var disableDarkMode: () -> Unit

    private fun configureThemeController() {
        enableDarkMode = {
            userTest.dark_mode = 1
            recreate()
        }
        disableDarkMode = {
            userTest.dark_mode = 0
            recreate()
        }

        themeController = ThemeController.getThemeController()
        themeController.configureLightSensor(baseContext)
        themeController.registerThemeModeListeners(enableDarkMode, disableDarkMode)
    }

    override fun onResume() {
        super.onResume()
        configureThemeController()
    }

    override fun onPause() {
        super.onPause()
        themeController.unregisterThemeModeListeners(enableDarkMode, disableDarkMode)
    }

    override fun getTheme(): Resources.Theme {
        val theme = super.getTheme()

        if (userTest.dark_mode == 1) {
            theme.applyStyle(R.style.Base_Theme_LifeCoachDarkTheme, true)
        }

        return theme
    }

    private fun updatePhoto(){
        if (userTest.picture.isNotEmpty()) {
            // Update the photo
            val uri = Uri.parse(userTest.picture)
            val imageStream = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(imageStream)
            binding.dashProfPic.setImageBitmap(bitmap)

            Log.i("Uri DashBoard", uri.toString())
        }
        else{
            // Set default photo
            binding.dashProfPic.setImageDrawable(getDrawable(R.drawable.usuario))
        }
    }

    private fun updateHabits() {
        val updateIntent = Intent(this, HabitsNotificationService::class.java)
        updateIntent.action = "com.example.lifecoach_.notifications.UPDATE_NOTIFICATIONS"
        sendBroadcast(updateIntent)

        todayHabits.clear()
        otherHabits.clear()

        for (habit in userTest.habits) {
            Log.i("HABIT", "${habit.id} --- ${habit.accomplishment}")
            if (habit.shouldDoToday())
                todayHabits.add(habit)
            else
                otherHabits.add(habit)
        }
        if (showToday)
            binding.todayHabits.adapter = HabitListViewAdapter(this, todayHabits)
        else
            binding.todayHabits.adapter = HabitListViewAdapter(this, otherHabits)
    }


    fun manageButtons(user: User) {
        // CREATE HABIT
        binding.createHabitButton.setOnClickListener {
            intent = Intent(baseContext, CreateHabitsActivity::class.java)
            startForResult.launch(intent)
        }

        // ON HABIT CLICK
        binding.todayHabits.setOnItemClickListener { _, _, position, _ ->
            onHabitClick(position)
        }

        // SHOW TODAY HABITS
        binding.showToday.setOnClickListener {
            binding.showOthers.backgroundTintList = ColorStateList.valueOf(getColor(R.color.dark_gray))
            binding.showOthers.setTextColor(getColor(R.color.white))

            binding.showToday.backgroundTintList = ColorStateList.valueOf(getColor(R.color.light_gray))
            binding.showToday.setTextColor(getColor(R.color.dark_gray))

            showToday = true
            updateHabits()
        }

        binding.showOthers.setOnClickListener {
            binding.showToday.backgroundTintList = ColorStateList.valueOf(getColor(R.color.dark_gray))
            binding.showToday.setTextColor(getColor(R.color.white))

            binding.showOthers.backgroundTintList = ColorStateList.valueOf(getColor(R.color.light_gray))
            binding.showOthers.setTextColor(getColor(R.color.dark_gray))

            showToday = false
            updateHabits()
        }


        bottomNavigationBarManagement(user)
    }

    private fun onHabitClick(position: Int) {
        val intent: Intent
        val habits: MutableList<Habit>
        if (showToday) habits = todayHabits
        else habits = otherHabits

        when (habits[position]) {
            is StrengthHabit -> {
                intent = Intent(baseContext, MuscularHabitViewActivity::class.java)
            }

            is RunningHabit -> {
                intent = Intent(baseContext, RunningHabitViewActivity::class.java)
            }

            is StepsHabit -> {
                intent = Intent(baseContext, StepHabitViewActivity::class.java)
            }

            is TimeControlHabit -> {
                intent = Intent(baseContext, TimeHabitViewActivity::class.java)
            }

            else -> {
                intent = Intent(baseContext, GenericHabitViewActivity::class.java)
            }
        }
        selectedHabit = userTest.habits.indexOf(habits[position])
        intent.putExtra("habit", habits[position])
        startForUpdate.launch(intent)
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
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    startActivity(intent)
                    false
                }

                else -> false
            }
        }
    }
}