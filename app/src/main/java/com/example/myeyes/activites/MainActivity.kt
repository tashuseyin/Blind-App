package com.example.myeyes.activites

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.myeyes.R
import com.example.myeyes.config.DoubleClick
import com.example.myeyes.config.DoubleClickListener
import com.example.myeyes.databinding.ActivityMainBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
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
                val locale = Locale("tr", "TR")
                if (textToSpeech?.isLanguageAvailable(locale) == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                    textToSpeech?.language = locale
                    textToSpeech?.speak(
                        "My Eyes uygulamasına hoş geldiniz, ayrıntıları öğrenmek için ekranın farklı boyutuna tıklayın", // Welcome to my eyes app, click on the different size of the screen to know details
                        TextToSpeech.QUEUE_FLUSH, null
                    )
                }
            }
        }
        setListener()
        smsPermitted()
        callPermitted()
    }

    private fun setListener() {
        binding.apply {

            battery.setOnClickListener(DoubleClick(object : DoubleClickListener {
                override fun onSingleClick(view: View) {
                    textToSpeech?.speak(
                        "pil özelliğini tıkladınız, onaylamak için tekrar çift tıklayın", // you clicked battery, double click again to confirm
                        TextToSpeech.QUEUE_FLUSH,
                        null
                    )
                }

                override fun onDoubleClick(view: View) {
                    startActivity(Intent(this@MainActivity, BatteryActivity::class.java))
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
                }

                override fun onTripleClick(view: View) {
                    TODO("Not yet implemented")
                }
            }))

            message.setOnClickListener(DoubleClick(object : DoubleClickListener {
                override fun onSingleClick(view: View) {
                    if (smsPermitted()) {
                        textToSpeech?.speak(
                            "mesaj özelliğini tıkladınız, onaylamak için tekrar çift tıklayın", //  you clicked message, double click again to confirm
                            TextToSpeech.QUEUE_FLUSH,
                            null
                        )
                    } else {
                        textToSpeech?.speak(
                            "Bu özelliği kullanmak için gerekli izni verin", // Give the necessary permission to use this feature
                            TextToSpeech.QUEUE_FLUSH,
                            null
                        )
                    }
                }
                override fun onDoubleClick(view: View) {
                    openSms()
                }

                override fun onTripleClick(view: View) {
                    TODO("Not yet implemented")
                }
            }))

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

    private fun smsPermitted(): Boolean {
        var permitted = false
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.READ_SMS,
                Manifest.permission.SEND_SMS
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {
                        permitted = true
                    }
                    if (report.isAnyPermissionPermanentlyDenied) {
                        textToSpeech!!.speak(
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

    private fun callPermitted(): Boolean {
        var permitted = false
        Dexter.withContext(this)
            .withPermissions(
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
                        textToSpeech!!.speak(
                            "Bu uygulamanın bu özelliği kullanması için izne ihtiyacı var. Bunları uygulama ayarlarından verebilirsiniz",
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
        textToSpeech?.shutdown()
        super.onDestroy()
    }
}