package com.example.myeyes.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import com.example.myeyes.bindingadapter.BindingFragment
import com.example.myeyes.databinding.FragmentNewMessageBinding


class NewMessageFragment : BindingFragment<FragmentNewMessageBinding>() {
    private val args: NewMessageFragmentArgs by navArgs()
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentNewMessageBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (args.user != null){
            binding.phoneNumber.setText(args.user!!.user_title)
        }
    }


}