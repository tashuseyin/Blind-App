package com.example.myeyes.fragment

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import com.example.myeyes.bindingadapter.BindingFragment
import com.example.myeyes.databinding.FragmentNewMessageBinding


class NewMessageFragment : BindingFragment<FragmentNewMessageBinding>() {
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentNewMessageBinding::inflate

}