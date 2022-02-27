package com.example.myeyes.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import com.example.myeyes.R
import com.example.myeyes.adapter.SmsViewPagerAdapter
import com.example.myeyes.app.MyApp
import com.example.myeyes.bindingadapter.BindingFragment
import com.example.myeyes.databinding.FragmentMainMessageBinding
import com.google.android.material.tabs.TabLayoutMediator


class MainMessageFragment : BindingFragment<FragmentMainMessageBinding>() {
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentMainMessageBinding::inflate


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = SmsViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle)
        binding.viewpager.adapter = adapter
        TabLayoutMediator(binding.tablayout, binding.viewpager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.inbox)
                }
                1 -> {
                    tab.text = getString(R.string.outbox)
                }
                2 -> {
                    tab.text = getString(R.string.new_message)
                }
            }
        }.attach()

    }

    override fun onDestroyView() {
        (activity?.applicationContext as MyApp).textToSpeech?.stop()
        super.onDestroyView()
    }
}