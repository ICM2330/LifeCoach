package com.example.lifecoach_.activities

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.lifecoach_.R
import com.example.lifecoach_.activities.friends.ChatMenuActivity
import com.example.lifecoach_.controllers.activities_controllers.activity_profile.ProfileActivityUploadController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.lifecoach_.databinding.ActivityProfileBinding
import com.example.lifecoach_.model.User
import com.example.lifecoach_.services.firebase.UsersService
import java.io.File

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private var user: User? = null

    private lateinit var uploadController:
            ProfileActivityUploadController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Fill the info with the login activity
        val userProof = intent.getSerializableExtra("user") as User
        user = userProof

        //Grant URI permissions
        fillInformation(binding, userProof)

        //Instructions of buttons
        manageButtons(binding, userProof)

        uploadController = ProfileActivityUploadController(baseContext)
    }

    private fun fillInformation(binding: ActivityProfileBinding, user: User) {
        binding.emailProfile.setText(user.email)
        binding.nameProfile.setText(user.name)
        binding.userProfile.setText(user.username)
        val stringPhone = user.phone.toString()
        binding.phoneProfile.setText(stringPhone)
        Log.i("Uri PROFILE", user.picture)

        chargePhoto()
    }

    private fun chargePhoto() {
        if (user!!.picture.isNotEmpty()) {
            val uri = Uri.parse(user!!.picture)
            val imageStream = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(imageStream)
            binding.profProfPic.setImageBitmap(bitmap)

        } else {
            binding.profProfPic.setImageDrawable(getDrawable(R.drawable.usuario))
        }
    }



    private fun manageButtons(binding: ActivityProfileBinding, user: User) {
        bottomNavigationBarManagement(user)
        uploadInfo(binding, user)
        logOut(binding)
        uploadPhotoProfile()
    }

    private fun uploadPhotoProfile() {
        binding.uploadFromGalleryButton.setOnClickListener {
            getContentGallery.launch("image/*")
        }

        binding.uploadFromCameraButton.setOnClickListener {
            val file = File(filesDir, "picFromCamera")
            cameraUri = FileProvider.getUriForFile(baseContext,baseContext.packageName + ".fileprovider", file)
            getContentCamera.launch(cameraUri)
        }
    }

    private fun logOut(binding: ActivityProfileBinding) {
        binding.logOutButtonProfile.setOnClickListener {
            Firebase.auth.signOut()
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
                if ((user.longitude == 360.0 || user.latitude == 360.0) || (user.longitude == null || user.latitude == null)) {
                    user.latitude = 360.0
                    user.longitude = 360.0
                }

                uploadController.updateUser(user)
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

    // Attributes of the gallery
    private val  getContentGallery = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            val imageStream = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(imageStream)
            binding.profProfPic.setImageBitmap(bitmap)

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
        val imageUri: Uri? = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        user?.picture = imageUri.toString()

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
    private val getContentCamera = registerForActivityResult(ActivityResultContracts.TakePicture()
    ) {
        if (it) {
            loadImage(cameraUri)
        }
    }

    private fun loadImage(uri: Uri) {
        val imageStream = contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(imageStream)
        binding.profProfPic.setImageBitmap(bitmap)

        // Asign the image to the user
        val user = intent.getSerializableExtra("user") as User
        val uriString = uri.toString()
        user.picture = uriString
    }
}