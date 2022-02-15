package com.example.myeyes.activites

import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import com.example.myeyes.R
import com.example.myeyes.app.MyApp
import com.example.myeyes.databinding.ActivityBatteryBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class BatteryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBatteryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBatteryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val locale = Locale("tr", "TR")
        (applicationContext as MyApp).textToSpeech = TextToSpeech(applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                if ((applicationContext as MyApp).textToSpeech?.isLanguageAvailable(locale) == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                    (applicationContext as MyApp).textToSpeech?.language = locale
                }
            }
        }
        getBattery()
    }

    private fun getBattery() {
        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = registerReceiver(null, intentFilter)

        val batteryPct: Float? = batteryStatus?.let { intent ->
            val level: Int = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale: Int = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            level / scale.toFloat()
        }

        val batteryLevel = batteryPct?.times(100)?.toInt()
        binding.percent.text = String.format("%s%s", batteryLevel.toString(), "%")

        val statusBattery: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        val isCharging: Boolean = statusBattery == BatteryManager.BATTERY_STATUS_CHARGING
                || statusBattery == BatteryManager.BATTERY_STATUS_FULL


        lifecycleScope.launch {
            delay(100)
            (applicationContext as MyApp).textToSpeech?.speak(
                "pil seviyeniz yüzde ${batteryLevel.toString()}",
                TextToSpeech.QUEUE_FLUSH,
                null
            )

            if (isCharging) {
                binding.status.text = getString(R.string.device_power)
                (applicationContext as MyApp).textToSpeech?.speak(
                    "ve cihazınız şarj oluyor",
                    TextToSpeech.QUEUE_ADD,
                    null // and your device is charging
                )
            } else {
                binding.status.text = getString(R.string.device_not_power)
                (applicationContext as MyApp).textToSpeech?.speak(
                    "ve cihazınınz sarj omuyor.",
                    TextToSpeech.QUEUE_ADD,
                    null // and your device is not charging
                )
            }
        }

        if (isCharging) {
            when (batteryLevel) {
                in 0..20 -> {
                    binding.batteryIcon.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.battery_charging_20,
                            null
                        )
                    )
                }
                in 21..40 -> {
                    binding.batteryIcon.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.battery_charging_30,
                            null
                        )
                    )
                }
                in 41..50 -> {
                    binding.batteryIcon.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.battery_charging_50,
                            null
                        )
                    )
                }
                in 51..60 -> {
                    binding.batteryIcon.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.battery_charging_60,
                            null
                        )
                    )
                }
                in 61..80 -> {
                    binding.batteryIcon.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.battery_charging_80,
                            null
                        )
                    )
                }
                in 81..90 -> {
                    binding.batteryIcon.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.battery_charging_90,
                            null
                        )
                    )
                }
                in 91..99 -> {
                    binding.batteryIcon.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.battery_charging_full,
                            null
                        )
                    )
                }
                100 -> {
                    binding.batteryIcon.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.battery_full,
                            null
                        )
                    )
                }
            }
        } else {

            when (batteryLevel) {
                in 0..20 -> {
                    binding.batteryIcon.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.battery_20,
                            null
                        )
                    )
                }
                in 21..40 -> {
                    binding.batteryIcon.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.battery_30,
                            null
                        )
                    )
                }
                in 41..50 -> {
                    binding.batteryIcon.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.battery_50,
                            null
                        )
                    )
                }
                in 51..60 -> {
                    binding.batteryIcon.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.battery_60,
                            null
                        )
                    )
                }
                in 61..80 -> {
                    binding.batteryIcon.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.battery_80,
                            null
                        )
                    )
                }
                in 81..90 -> {
                    binding.batteryIcon.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.battery_90,
                            null
                        )
                    )
                }
                in 91..100 -> {
                    binding.batteryIcon.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.battery_full,
                            null
                        )
                    )
                }
            }

        }
    }

    override fun onDestroy() {
        (applicationContext as MyApp).textToSpeech?.stop()
        super.onDestroy()
    }
}