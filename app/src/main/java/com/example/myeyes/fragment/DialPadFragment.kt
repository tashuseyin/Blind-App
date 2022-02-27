package com.example.myeyes.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import com.example.myeyes.R
import com.example.myeyes.bindingadapter.BindingFragment
import com.example.myeyes.databinding.FragmentDialPadBinding
import com.example.myeyes.util.Utils

class DialPadFragment : BindingFragment<FragmentDialPadBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentDialPadBinding::inflate


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        welcomeSpeak()
        binding.number.addTextChangedListener(numberWatcher)
        setListener()
    }


    private fun setListener() {
        binding.apply {
            one.setOnClickListener {
                speak("1")
                displayNumber("1")
            }
            two.setOnClickListener {
                speak("2")
                displayNumber("2")
            }
            three.setOnClickListener {
                speak("3")
                displayNumber("3")
            }
            four.setOnClickListener {
                speak("4")
                displayNumber("4")
            }
            five.setOnClickListener {
                speak("5")
                displayNumber("5")
            }
            six.setOnClickListener {
                speak("6")
                displayNumber("6")
            }
            seven.setOnClickListener {
                speak("7")
                displayNumber("7")
            }
            eight.setOnClickListener {
                speak("8")
                displayNumber("8")
            }
            nine.setOnClickListener {
                speak("9")
                displayNumber("9")
            }
            ast.setOnClickListener {
                speak("*")
                displayNumber("*")
            }
            zero.setOnClickListener {
                speak("0")
                displayNumber("0")
            }
            hash.setOnClickListener {
                speak("#")
                displayNumber("#")
            }
            backButton.setOnClickListener {
                Utils.textToSpeechFunctionBasic(requireActivity(), "${number.text.last()} silindi.")

                if (number.length() == 1) {
                    number.text = null
                    Utils.textToSpeechFunctionBasic(requireActivity(), "Telefon numarası boş")
                } else {
                    val newNumber = number.text.subSequence(0, number.length() - 1)
                    number.setText(newNumber)
                }
            }
            backButton.setOnLongClickListener {
                number.text = null
                Utils.textToSpeechFunctionBasic(
                    requireActivity(),
                    "Telefon numarası tamamen silindi"
                )
                true
            }
        }
    }


    private fun displayNumber(no: String) {
        val str = binding.number.text
        binding.number.setText("$str$no")
    }

    private fun welcomeSpeak() {
        Utils.textToSpeechFunctionBasic(
            requireActivity(),
            getString(R.string.dial_pad_welcome_speak)
        )
    }

    private fun speak(digit: String) {
        Utils.textToSpeechFunctionBasic(requireActivity(), getString(R.string.digit, digit))
    }

    private var numberWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (p0 == null) {
                binding.backButton.isVisible = false
            } else {
                binding.backButton.isVisible = !(p0.isEmpty() || p0.isBlank())
            }
        }

        override fun afterTextChanged(p0: Editable?) {
        }
    }

}