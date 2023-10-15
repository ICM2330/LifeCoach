package com.example.lifecoach_.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lifecoach_.R
import com.example.lifecoach_.databinding.ActivityStepsReviewBinding

class StepsReviewActivity : AppCompatActivity() {
    private lateinit var binding : ActivityStepsReviewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStepsReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}