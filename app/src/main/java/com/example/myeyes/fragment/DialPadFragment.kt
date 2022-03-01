package com.example.myeyes.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.example.myeyes.R
import com.example.myeyes.adapter.ContactAdapter
import com.example.myeyes.bindingadapter.BindingFragment
import com.example.myeyes.config.TripleClick
import com.example.myeyes.config.TripleClickListener
import com.example.myeyes.databinding.FragmentDialPadBinding
import com.example.myeyes.model.ContactUser
import com.example.myeyes.util.Utils
import com.example.myeyes.viewmodel.SharedViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DialPadFragment : BindingFragment<FragmentDialPadBinding>() {

    private val sharedViewModel: SharedViewModel by viewModels()
    private lateinit var adapter: ContactAdapter
    private var listSearch: ArrayList<ContactUser> = arrayListOf()


    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentDialPadBinding::inflate


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        welcomeSpeak()
        setListener()
        setLongListener()
        searchEditTextNumber()
        adapter = ContactAdapter(requireContext()) { user, clickNumber, _ ->
            when (clickNumber) {
                1 -> speakCall(user)
                2 -> callUser(user)
            }
        }
        binding.recyclerView.adapter = adapter
    }

    private fun setLongListener() {
        binding.apply {
            one.setOnLongClickListener {
                viewDialog()
                true
            }
            two.setOnLongClickListener {
                viewDialog()
                true
            }
            three.setOnLongClickListener {
                viewDialog()
                true
            }
            four.setOnLongClickListener {
                viewDialog()
                true
            }
            five.setOnLongClickListener {
                viewDialog()
                true
            }
            six.setOnLongClickListener {
                viewDialog()
                true
            }
            seven.setOnLongClickListener {
                viewDialog()
                true
            }
            eight.setOnLongClickListener {
                viewDialog()
                true
            }
            nine.setOnLongClickListener {
                viewDialog()
                true
            }
            ast.setOnLongClickListener {
                viewDialog()
                true
            }
            zero.setOnLongClickListener {
                viewDialog()
                true
            }
            hash.setOnLongClickListener {
                viewDialog()
                true
            }
            gridLayout.setOnLongClickListener {
                viewDialog()
                true
            }
            number.setOnLongClickListener {
                viewDialog()
                true
            }
        }
    }

    private fun viewDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.call_options_dialog)
        dialog.show()
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        val screen = dialog.findViewById<ConstraintLayout>(R.id.dialog_screen)
        val number = binding.number.text.toString()
        var username = ""
        sharedViewModel.contactList?.observe(viewLifecycleOwner) { userList ->
            for (i in userList) {
                if (number == i.phone_number) {
                    username = i.user_title
                    break
                }
            }
        }

        screen.setOnClickListener(TripleClick(object : TripleClickListener {
            override fun onSingleClick(view: View) {
                if (username.isNotEmpty()) {
                    Utils.textToSpeechFunctionBasic(requireActivity(), username)
                } else {
                    Utils.textToSpeechFunctionBasic(requireActivity(), number)
                }
            }

            override fun onDoubleClick(view: View) {
                if (username.isNotEmpty()) {
                    Utils.textToSpeechFunctionBasic(
                        requireActivity(),
                        getString(R.string.call_user, username)
                    )
                } else {
                    Utils.textToSpeechFunctionBasic(requireActivity(), getString(R.string.calling))
                }
                lifecycleScope.launch {
                    delay(2000)
                    val callIntent = Intent(Intent.ACTION_CALL)
                    callIntent.data = Uri.parse("tel:$number")
                    ContextCompat.startActivity(requireContext(), callIntent, null)

                }
            }

            override fun onTripleClick(view: View) {
                dialog.dismiss()
            }
        }))
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
                Utils.textToSpeechFunctionBasic(
                    requireActivity(),
                    getString(R.string.digit_deleted, number.text.last())
                )

                if (number.length() == 1) {
                    number.text = null
                    Utils.textToSpeechFunctionBasic(
                        requireActivity(),
                        getString(R.string.phone_number_empty)
                    )
                    listSearch.clear()
                    adapter.addItems(listSearch)
                } else {
                    val newNumber = number.text.subSequence(0, number.length() - 1)
                    number.setText(newNumber)
                }
            }
            backButton.setOnLongClickListener {
                number.text = null
                Utils.textToSpeechFunctionBasic(
                    requireActivity(),
                    getString(R.string.phone_number_deleted)
                )
                true
            }
        }
    }

    private fun speakCall(contactUser: ContactUser) {
        Utils.textToSpeechFunctionBasic(
            requireActivity(),
            getString(R.string.user_call_speak, contactUser.user_title)
        )
    }

    private fun callUser(user: ContactUser) {
        Utils.textToSpeechFunctionBasic(
            requireActivity(),
            getString(R.string.call_user, user.user_title)
        )
        lifecycleScope.launch {
            delay(1000)
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:" + user.phone_number)
            ContextCompat.startActivity(requireContext(), callIntent, null)

        }
    }

    @SuppressLint("SetTextI18n")
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

    private fun searchEditTextNumber() {
        binding.number.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0 == null) {
                    binding.backButton.isVisible = false
                } else {
                    binding.backButton.isVisible = !(p0.isEmpty() || p0.isBlank())
                    listSearch.clear()
                    val searchNumber = binding.number.text.toString()
                    if (searchNumber.isNotEmpty()) {
                        sharedViewModel.searchPhoneNumberDatabase(searchNumber)
                            ?.observe(viewLifecycleOwner) { response ->
                                if (response.isNotEmpty()) {
                                    response.forEach { user ->
                                        listSearch.add(user)
                                    }
                                    adapter.addItems(listSearch)
                                } else {
                                    listSearch.clear()
                                    adapter.addItems(listSearch)
                                }
                            }
                    } else {
                        listSearch.clear()
                        adapter.addItems(emptyList())
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

}