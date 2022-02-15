package com.example.myeyes.activites

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myeyes.R
import com.example.myeyes.adapter.SmsViewPagerAdapter
import com.example.myeyes.app.MyApp
import com.example.myeyes.databinding.ActivityMessageBinding
import com.google.android.material.tabs.TabLayoutMediator

class MessageActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMessageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = SmsViewPagerAdapter(supportFragmentManager, lifecycle)
        binding.viewpager.adapter = adapter

        TabLayoutMediator(binding.tablayout, binding.viewpager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Gelen Kutusu"
                }
                1 -> {
                    tab.text = "Giden Kutusu"
                }
                2 -> {
                    tab.text = "Yeni Mesaj"
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