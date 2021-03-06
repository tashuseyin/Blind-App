package com.example.myeyes.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import com.example.myeyes.R
import com.example.myeyes.adapter.SmsAdapter
import com.example.myeyes.app.MyApp
import com.example.myeyes.bindingadapter.BindingFragment
import com.example.myeyes.databinding.FragmentInboxContactSentBinding
import com.example.myeyes.model.Sms
import com.example.myeyes.util.Utils
import com.example.myeyes.util.Utils.textToSpeechFunctionBasic
import com.example.myeyes.viewmodel.SharedViewModel

class Inbox : BindingFragment<FragmentInboxContactSentBinding>() {

    private lateinit var adapter: SmsAdapter
    private val sharedViewModel: SharedViewModel by viewModels()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentInboxContactSentBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SmsAdapter { sms ->
            speakMessage(sms)
        }
        binding.recyclerview.adapter = adapter

        observeViewModel()
    }

    private fun observeViewModel() {
        sharedViewModel.apply {
            loadSms()
            isEmptyImage.observe(viewLifecycleOwner) {
                binding.empty.isVisible = it
                if (it) {
                    textToSpeechFunctionBasic(
                        requireActivity(),
                        getString(R.string.inbox_start_text)
                    )
                } else {
                    textToSpeechFunctionBasic(
                        requireActivity(),
                        getString(R.string.message_listen_inbox)
                    )
                }
            }
            isRecyclerView.observe(viewLifecycleOwner) {
                binding.recyclerview.isVisible = it
            }
            smsList.observe(viewLifecycleOwner) {
                adapter.addItems(it)
            }

            isRefresh.observe(viewLifecycleOwner) {
                binding.refresh.isRefreshing = it
            }

            binding.refresh.setOnRefreshListener {
                sharedViewModel.smsRefreshData()
            }
        }
    }

    private fun speakMessage(smsData: Sms) {
        textToSpeechFunctionBasic(
            requireActivity(),
            "message from ${smsData.address.lowercase()} on ${Utils.convertLongToTime(smsData.date.toLong())} ${smsData.body.lowercase()}"
        )
    }

    override fun onDestroyView() {
        (activity?.applicationContext as MyApp).textToSpeech?.stop()
        super.onDestroyView()
    }
}