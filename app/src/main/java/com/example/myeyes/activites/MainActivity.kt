package com.example.myeyes.activites

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.myeyes.R
import com.example.myeyes.app.MyApp
import com.example.myeyes.config.DoubleClick
import com.example.myeyes.config.DoubleClickListener
import com.example.myeyes.databinding.ActivityMainBinding
import com.example.myeyes.util.Utils
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Utils.textToSpeechFunctionMain(this, getString(R.string.my_eyes_login))
        setListener()
        appPermitted()
    }

    private fun setListener() {
        binding.apply {
            battery.setOnClickListener(DoubleClick(object : DoubleClickListener {
                override fun onSingleClick(view: View) {
                    Utils.textToSpeechFunctionBasic(
                        this@MainActivity,
                        getString(R.string.battery_login)
                    )
                }

                override fun onDoubleClick(view: View) {
                    (applicationContext as MyApp).textToSpeech?.stop()
                    startActivity(Intent(this@MainActivity, BatteryActivity::class.java))
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
                }
            }))

            message.setOnClickListener(DoubleClick(object : DoubleClickListener {
                override fun onSingleClick(view: View) {
                    Utils.textToSpeechFunctionBasic(
                        this@MainActivity,
                        getString(R.string.message_login)
                    )
                }

                override fun onDoubleClick(view: View) {
                    if (appPermitted()) {
                        openSms()
                    } else {
                        Utils.textToSpeechFunctionBasic(
                            this@MainActivity,
                            getString(R.string.permission_error_message)
                        )
                    }
                }
            }))

            call.setOnClickListener(DoubleClick(object : DoubleClickListener {
                override fun onSingleClick(view: View) {
                    Utils.textToSpeechFunctionBasic(
                        this@MainActivity,
                        getString(R.string.call_login)
                    )
                }

                override fun onDoubleClick(view: View) {
                    if (appPermitted()) {
                        openCall()
                    } else {
                        Utils.textToSpeechFunctionBasic(
                            this@MainActivity,
                            getString(R.string.permission_error_message)
                        )
                    }
                }
            }))
            objectDetection.setOnClickListener(DoubleClick(object : DoubleClickListener {
                override fun onSingleClick(view: View) {
                    Utils.textToSpeechFunctionBasic(
                        this@MainActivity,
                        getString(R.string.object_detection_login)
                    )
                }

                override fun onDoubleClick(view: View) {
                    (applicationContext as MyApp).textToSpeech?.stop()
                    startActivity(Intent(this@MainActivity, ObjectDetectionActivity::class.java))
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
                }
            }))
        }
    }


    private fun openSms() {
        (applicationContext as MyApp).textToSpeech?.stop()
        startActivity(Intent(this@MainActivity, MessageActivity::class.java))
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    private fun openCall() {
        (applicationContext as MyApp).textToSpeech?.stop()
        startActivity(Intent(this@MainActivity, CallActivity::class.java))
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    private fun appPermitted(): Boolean {
        var permitted = false
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.READ_SMS,
                Manifest.permission.SEND_SMS,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_CONTACTS,
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {
                        permitted = true
                    }
                    if (report.isAnyPermissionPermanentlyDenied) {
                        Utils.textToSpeechFunctionBasic(
                            this@MainActivity,
                            getString(R.string.permission_settings_message)
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