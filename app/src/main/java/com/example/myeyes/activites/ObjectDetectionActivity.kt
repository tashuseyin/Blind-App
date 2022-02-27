package com.example.myeyes.activites

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myeyes.databinding.ActivityObjectDetectionBinding

class ObjectDetectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityObjectDetectionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityObjectDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}
