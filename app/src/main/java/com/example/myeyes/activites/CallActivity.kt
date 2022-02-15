package com.example.myeyes.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myeyes.R
import com.example.myeyes.adapter.CallViewPagerAdapter
import com.example.myeyes.adapter.SmsViewPagerAdapter
import com.example.myeyes.app.MyApp
import com.example.myeyes.databinding.ActivityCallBinding
import com.google.android.material.tabs.TabLayoutMediator

class CallActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCallBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCallBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = CallViewPagerAdapter(supportFragmentManager, lifecycle)
        binding.callViewpager.adapter = adapter

        TabLayoutMediator(binding.tablayout, binding.callViewpager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Tuş Takımı"
                }
                1 -> {
                    tab.text = "Rehber"
                }
            }
        }.attach()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
    }

    override fun onDestroy() {
        (applicationContext as MyApp).textToSpeech?.stop()
        super.onDestroy()
    }
}