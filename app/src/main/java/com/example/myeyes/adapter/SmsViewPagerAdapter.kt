package com.example.myeyes.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myeyes.fragment.Inbox
import com.example.myeyes.fragment.NewMessageFragment
import com.example.myeyes.fragment.SentFragment

class SmsViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {
    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> Inbox()
            1 -> SentFragment()
            2 -> NewMessageFragment()
            else -> Fragment()
        }
    }
}