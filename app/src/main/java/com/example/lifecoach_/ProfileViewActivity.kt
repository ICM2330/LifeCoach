package com.example.lifecoach_

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowId
import com.example.lifecoach_.databinding.ActivityProfileViewBinding

class ProfileViewActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProfileViewBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityProfileViewBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    fun manageBottomNavBar(){
        binding.bottomNavigationView.setOnClickListener{
            when (it.id){

            }
        }
    }

}