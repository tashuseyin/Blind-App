package com.example.myeyes.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import com.example.myeyes.R
import com.example.myeyes.adapter.CallViewPagerAdapter
import com.example.myeyes.bindingadapter.BindingFragment
import com.example.myeyes.databinding.FragmentMainCallBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainCallFragment : BindingFragment<FragmentMainCallBinding>() {
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentMainCallBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = CallViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle)
        binding.viewpager.adapter = adapter

        TabLayoutMediator(binding.tablayout, binding.viewpager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.dial_pad)
                }
                1 -> {
                    tab.text = getText(R.string.contact)
                }
            }
        }.attach()
    }
}