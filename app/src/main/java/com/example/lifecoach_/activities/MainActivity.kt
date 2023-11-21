package com.example.lifecoach_.activities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.lifecoach_.controllers.activities_controllers.activity_main.MainActivityAuthController
import com.example.lifecoach_.controllers.activities_controllers.activity_main.MainActivityRegisterController
import com.example.lifecoach_.databinding.ActivityMainBinding
import java.io.File
import com.example.lifecoach_.model.User
import com.example.lifecoach_.notifications.HabitsNotificationService

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var authController: MainActivityAuthController
    private val registerController: MainActivityRegisterController =
        MainActivityRegisterController()

    private var uriImage: Uri? = null

    private var user: User? = null

    companion object {
        const val FIREBASE_URL = "https://lifecoach-9f291.firebaseapp.com"
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotificationChannel()

        val updateIntent = Intent(this, HabitsNotificationService::class.java)
        updateIntent.action = "com.example.lifecoach_.notifications.UPDATE_NOTIFICATIONS"
        sendBroadcast(updateIntent)

        buttonsManager()
        authController = MainActivityAuthController(intent, baseContext)
    }

    override fun onResume() {
        super.onResume()
        authController.runIfLogged {user1: User?, already: Boolean ->

            if (user1 != null) {
                Log.i("LOGIN", "Loaded Image from URI: ${user1.picture}")
                if (!user1.picture.isNullOrEmpty() && !already) {
                    uriImage = Uri.parse(user1.picture)
                }
                registerController.registerUser(user1, uriImage) { user2: User? ->
                    val t = Toast.makeText(
                        baseContext,
                        "Se ha iniciado sesión correctamente",
                        Toast.LENGTH_LONG
                    )
                    t.show()
                    val i = Intent(baseContext, DashBoardHabitsActivity::class.java)
                    i.putExtra("user", user2)
                    startActivity(i)
                    finish()
                }
            }
        }
    }

    private fun buttonsManager() {
        //Button of attach photo from the registering proccess
        binding.photoFromGallery.setOnClickListener {
            getContentGallery.launch("image/*")
        }

        // Button of attach photo from camera ah the registering proccess
        binding.photoFromCamera.setOnClickListener {
            val file = File(filesDir, "picFromCamera")
            cameraUri = FileProvider.getUriForFile(
                baseContext,
                baseContext.packageName + ".fileprovider",
                file
            )
            getContentCamera.launch(cameraUri)
        }

        //Button of Registering
        binding.registerButton.setOnClickListener {
            if (!blankSpaces()) {
                //If there is not any blank or nut spaces, register and verify the user
                user = getUserTest()
                authController.register(user!!) {
                    val i = Intent(baseContext, DashBoardHabitsActivity::class.java)
                    i.putExtra("user", user)
                    startActivity(i)
                    finish()
                }
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
            null,
            binding.nameRegister.text.toString(), binding.userRegister.text.toString(),
            binding.emailRegister.text.toString(), binding.phoneRegister.text.toString().toLong(),
            360.0, 360.0
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

    private val getContentGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                // Load Image implemented
                val imageStream = contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(imageStream)
                binding.photoload.setImageBitmap(bitmap)

                // Call this function to save the image to the gallery
                saveImageToGallery(bitmap)
            }
        }

    private fun saveImageToGallery(bitmap: Bitmap) {
        val displayName = "MyImage.jpg"
        val mimeType = "image/jpeg"
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
            put(MediaStore.Images.Media.MIME_TYPE, mimeType)
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
        }

        val contentResolver = contentResolver
        val imageUri: Uri? =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        uriImage = imageUri

        imageUri?.let {
            contentResolver.openOutputStream(it).use { outputStream ->
                if (outputStream != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                }
            }
        }
    }

    // Attributes of the camera
    private lateinit var cameraUri: Uri
    private val getContentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) {
        if (it) {
            loadImage(cameraUri)
        }
    }

    private fun loadImage(uri: Uri) {
        val imageStream = contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(imageStream)
        binding.photoload.setImageBitmap(bitmap)

        uriImage = uri
    }


    // Create notification channel
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Habits"
            val description = "Notifications to inform when is time for a habit"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("Habits", name, importance)
            channel.description = description
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}