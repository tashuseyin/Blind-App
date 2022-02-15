package com.example.myeyes.activites

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myeyes.R
import com.example.myeyes.app.MyApp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        lifecycleScope.launchWhenStarted {
            (applicationContext as MyApp).textToSpeech = TextToSpeech(applicationContext) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    val locale = Locale("tr", "TR")
                    if ((applicationContext as MyApp).textToSpeech?.isLanguageAvailable(locale) == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                        (applicationContext as MyApp).textToSpeech?.language = locale
                        (applicationContext as MyApp).textToSpeech?.speak(
                            "My Eyes uygulamasına hoş geldiniz, ayrıntıları öğrenmek için ekranın farklı boyutuna tıklayın",
                            TextToSpeech.QUEUE_FLUSH, null
                        )
                    }
                }
            }
        }

        lifecycleScope.launch {
            delay(1500)
            startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
            finish()
        }
    }
}