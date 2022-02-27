package com.example.myeyes.fragment

import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.BatteryManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.viewbinding.ViewBinding
import com.example.myeyes.R
import com.example.myeyes.app.MyApp
import com.example.myeyes.bindingadapter.BindingFragment
import com.example.myeyes.databinding.FragmentBatteryBinding
import com.example.myeyes.util.Utils

class BatteryFragment : BindingFragment<FragmentBatteryBinding>() {
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentBatteryBinding::inflate


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBattery()
    }

    private fun getBattery() {
        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = activity?.registerReceiver(null, intentFilter)

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

        if (isCharging) {
            binding.status.text = getString(R.string.device_power)
            Utils.textToSpeechFunctionBasic(
                requireActivity(),
                getString(R.string.battery_charging_information, batteryLevel.toString())
            )
        } else {
            binding.status.text = getString(R.string.device_not_power)
            Utils.textToSpeechFunctionBasic(
                requireActivity(),
                getString(R.string.battery_not_charging_information, batteryLevel.toString())
            )
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

    override fun onDestroyView() {
        (activity?.applicationContext as MyApp).textToSpeech?.stop()
        super.onDestroyView()
    }
}

