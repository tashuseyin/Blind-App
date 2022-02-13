package com.example.myeyes.activites

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myeyes.R
import com.example.myeyes.app.MyApp
import com.example.myeyes.databinding.ActivityMainBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        (applicationContext as MyApp).textToSpeech = TextToSpeech(applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val locale = Locale("tr", "TR")
                if ((applicationContext as MyApp).textToSpeech?.isLanguageAvailable(locale) == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                    (applicationContext as MyApp).textToSpeech?.language = locale
                    (applicationContext as MyApp).textToSpeech?.speak(
                        "My Eyes uygulamasına hoş geldiniz, ayrıntıları öğrenmek için ekranın farklı boyutuna tıklayın", // Welcome to my eyes app, click on the different size of the screen to know details
                        TextToSpeech.QUEUE_FLUSH, null
                    )
                }
            }
        }
        setListener()
        appPermitted()
    }

    private fun setListener() {
        binding.apply {

            battery.setOnClickListener {
                (applicationContext as MyApp).textToSpeech?.speak(
                    "pil özelliğini tıkladınız", // you clicked battery, double click again to confirm
                    TextToSpeech.QUEUE_FLUSH,
                    null
                )
                lifecycleScope.launch {
                    delay(200)
                    startActivity(Intent(this@MainActivity, BatteryActivity::class.java))
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
                }
            }

            message.setOnClickListener {
                if (appPermitted()) {
                    (applicationContext as MyApp).textToSpeech?.speak(
                        "mesaj özelliğini tıkladınız", //  you clicked message, double click again to confirm
                        TextToSpeech.QUEUE_FLUSH,
                        null
                    )
                    lifecycleScope.launch {
                        delay(200)
                        openSms()
                    }
                } else {
                    (applicationContext as MyApp).textToSpeech?.speak(
                        "Bu özelliği kullanmak için gerekli izni verin", // Give the necessary permission to use this feature
                        TextToSpeech.QUEUE_FLUSH,
                        null
                    )
                }

            }
        }
    }


    private fun openSms() {
        startActivity(Intent(this@MainActivity, MessageActivity::class.java))
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    private fun openCall() {
        startActivity(Intent(this@MainActivity, MessageActivity::class.java))
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    private fun appPermitted(): Boolean {
        var permitted = false
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.READ_SMS,
                Manifest.permission.SEND_SMS,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.READ_CONTACTS
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {
                        permitted = true
                    }
                    if (report.isAnyPermissionPermanentlyDenied) {
                        (applicationContext as MyApp).textToSpeech?.speak(
                            "Bu uygulamanın bu özelliği kullanması için izne ihtiyacı var. Bunları uygulama ayarlarından verebilirsiniz.", // This app needs permission to use this feature. You can grant them in app settings.
                            TextToSpeech.QUEUE_FLUSH,
                            null
                        )
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token!!.continuePermissionRequest()
                }
            })
            .onSameThread()
            .check()
        return permitted
    }


    override fun onDestroy() {
        (applicationContext as MyApp).textToSpeech?.shutdown()
        super.onDestroy()
    }
}