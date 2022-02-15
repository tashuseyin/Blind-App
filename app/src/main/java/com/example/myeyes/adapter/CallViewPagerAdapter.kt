package com.example.myeyes.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myeyes.fragmet.CallLogsFragment
import com.example.myeyes.fragmet.ContactFragment
import com.example.myeyes.fragmet.DialPadFragment

class CallViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {
    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DialPadFragment()
            1 -> CallLogsFragment()
            2 -> ContactFragment()
            else -> Fragment()
        }
    }
}