package com.example.myeyes.activites

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myeyes.R
import com.example.myeyes.app.MyApp
import com.example.myeyes.databinding.ActivityMainBinding
import com.example.myeyes.util.Utils

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Utils.textToSpeechFunctionMain(this, getString(R.string.my_eyes_login))
    }

    override fun onStop() {
        (applicationContext as MyApp).textToSpeech?.stop()
        super.onStop()
    }
}