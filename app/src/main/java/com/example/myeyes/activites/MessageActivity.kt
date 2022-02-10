package com.example.myeyes.activites

import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myeyes.R
import com.example.myeyes.databinding.ActivityMessageBinding

class MessageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMessageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }




}