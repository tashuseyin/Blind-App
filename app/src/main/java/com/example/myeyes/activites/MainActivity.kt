package com.example.myeyes.activites

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.appcompat.app.AppCompatActivity
import com.example.myeyes.R
import com.example.myeyes.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private var textToSpeech: TextToSpeech? = null

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        textToSpeech = TextToSpeech(applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                if (textToSpeech?.isLanguageAvailable(Locale.US) == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                    textToSpeech?.language = Locale.US
                    textToSpeech?.speak(
                        "Welcome to my eyes app, click on the different size of the screen to know details",
                        TextToSpeech.QUEUE_FLUSH, null
                    )
                }
            }
        }
        setListener()
    }

    private fun setListener() {
        binding.apply {

            battery.setOnClickListener {
                textToSpeech?.speak(
                    "you clicked battery, click again to confirm",
                    TextToSpeech.QUEUE_FLUSH,
                    null
                )
                startActivity(Intent(this@MainActivity, BatteryActivity::class.java))
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        textToSpeech?.speak(
            "Welcome to my eyes app, click on the different size of the screen to know details",
            TextToSpeech.QUEUE_FLUSH, null
        )
    }

    override fun onDestroy() {
        textToSpeech?.shutdown()
        super.onDestroy()
    }
}