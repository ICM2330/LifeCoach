package com.example.lifecoach_.controllers.activities_controllers.activity_main

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.lifecoach_.activities.MainActivity
import com.example.lifecoach_.model.User
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class MainActivityAuthController(val intent: Intent, val context: Context) {
    private lateinit var actionCodeSettings: ActionCodeSettings
    private val gson = Gson()
    private var user: User? = null

    init {
        configureFirebase()
    }

    fun runIfLogged(callback: (user: User?) -> Unit) {
        Log.i("LOGIN", "Checking if logged")
        Log.i("LOGIN", "Loading User Data")
        loadUser()
        Log.i("LOGIN", "User Data Loaded. User=$user")
        Log.i("LOGIN", "Starting Check For User Logged")
        checkIfLogged(callback)
    }

    fun register(user: User, failureListener: () -> Unit) {
        this.user = user
        storeUser()
        Firebase.auth.sendSignInLinkToEmail(user.email,
            actionCodeSettings
        )
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val t = Toast.makeText(context,
                        "Correo de verificación enviado. Por favor, revise el correo",
                        Toast.LENGTH_LONG)
                    t.show()
                }
            }
            .addOnFailureListener {
                val t = Toast.makeText(context,
                    "Error: No se pudo enviar el correo",
                    Toast.LENGTH_LONG)
                t.show()
                failureListener()
            }
    }

    private fun configureFirebase() {
        actionCodeSettings = actionCodeSettings {
            url = "${MainActivity.FIREBASE_URL}/finishSignUp"
            handleCodeInApp = true
            setAndroidPackageName(
                "com.example.lifecoach_",
                true,
                null
            )
        }
    }

    private fun checkIfLogged(callback: (user: User?) -> Unit) {
        val auth = Firebase.auth
        val intent = intent
        val emailLink = intent.data.toString()

        Log.i("LOGIN", "Recuperados datos del intent")

        val successLogin = {
            user?.uid = auth.currentUser?.uid
            callback(user)
        }

        val errorLogin = {
            val t = Toast.makeText(context,
                "Falló el inicio de sesión",
                Toast.LENGTH_LONG)
            t.show()
        }

        val fbUser = auth.currentUser

        if (fbUser == null) {
            if (auth.isSignInWithEmailLink(emailLink) && user != null) {
                Log.i("LOGIN", "Verificando emailLink: $emailLink")

                auth.signInWithEmailLink(user!!.email, emailLink)
                    .addOnCompleteListener { task ->
                        user!!.uid = auth.currentUser?.uid
                        if (task.isSuccessful) {
                            Log.i("LOGIN", "Verificación exitosa")
                            successLogin()
                        } else {
                            Log.i("LOGIN", "Verificación Fallida")
                            errorLogin()
                        }
                    }
            } else {
                Log.i("LOGIN", "No es emailLink o el usuario es nulo. User: $user")
            }
        } else if (user != null){
            Log.i("LOGIN", "Ya se encontraba loggeado")
            successLogin()
        }
    }

    private fun loadUser() {
        val userFile = File(context.filesDir, "user.json")
        Log.i("LOGIN", "Creado manejador de archivo")
        if (userFile.exists()) {
            Log.i("LOGIN", "Se encontró el archivo. Cargando...")
            val reader = FileReader(userFile)
            Log.i("LOGIN", "Creado el lector del archivo")
            user = gson.fromJson(reader, User::class.java)
            Log.i("LOGIN", "Se leyó el siguiente contenido $user")
        } else {
            Log.i("USERLOAD", "No existe el archivo de usuario")
        }
    }

    private fun storeUser() {
        val userFile = File(context.filesDir, "user.json")
        if (userFile.createNewFile()) {
            Log.i("SAVEUSER", "User File Created")
        } else {
            Log.i("SAVEUSER", "User File Already Exists")
            userFile.delete()
            userFile.createNewFile()
        }

        userFile.setWritable(true)
        userFile.setReadable(true)

        val writer = FileWriter(userFile)
        writer.write(gson.toJson(user))
        writer.close()
    }
}