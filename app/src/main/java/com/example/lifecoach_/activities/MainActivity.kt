package com.example.lifecoach_.activities

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lifecoach_.model.User
import com.example.lifecoach_.databinding.ActivityMainBinding
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var uriImage : Uri

    private lateinit var actionCodeSettings: ActionCodeSettings

    companion object {
        const val FIREBASE_URL = "https://lifecoach-9f291.firebaseapp.com"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureFirebase()
        buttonsManager()
    }

    private fun configureFirebase() {
        actionCodeSettings = actionCodeSettings {
            url = "$FIREBASE_URL/finishSignUp"
            handleCodeInApp = true
            setAndroidPackageName(
                "com.example.lifecoach_",
                true,
                null
            )
        }
    }

    private fun buttonsManager (){
        //Button of attach photo from the registering proccess
        binding.headercameraButton.setOnClickListener {
            getContentGallery.launch("image/*")
        }

        //Button of Registering
        binding.registerButton.setOnClickListener {
            if (!blankSpaces()) {
                //If there is not any blank or nut spaces, register and verify the user
                val userTest = getUserTest()
                Firebase.auth.sendSignInLinkToEmail(userTest.email, actionCodeSettings)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val t = Toast.makeText(baseContext,
                                "Correo de verificación enviado. Por favor, revise el correo",
                                Toast.LENGTH_LONG)
                            t.show()
                        }
                    }
                    .addOnFailureListener {
                        val t = Toast.makeText(baseContext,
                            "Error: No se pudo enviar el correo",
                            Toast.LENGTH_LONG)
                        t.show()
                    }
                // intent = Intent(this, DashBoardHabitsActivity::class.java)
                // intent.putExtra("user", userTest)
                // startActivity(intent)
                // finish()
            }
            else{
                //Say that is not possible to do the register
                Toast.makeText(this, "No puedes dejar campos de registro vacíos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getUserTest(): User {
        return User(
            binding.nameRegister.text.toString(), binding.userRegister.text.toString(),
            binding.emailRegister.text.toString(), binding.phoneRegister.text.toString().toLong()
        )
    }

    private fun blankSpaces () : Boolean{
        return binding.nameRegister.text.toString().isBlank() || binding.userRegister.text.toString().isBlank() ||
                binding.emailRegister.text.toString().isBlank() || binding.phoneRegister.text.toString().isBlank()
    }

    private val getContentGallery = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        if (it != null) {
            loadImage(it)
        }
    }

    private fun loadImage (uri : Uri){
        uriImage = uri
        val imageStream = contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(imageStream)
        binding.photoload.setImageBitmap(bitmap)
    }
}