package com.example.myeyes.fragmet

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.myeyes.adapter.SmsAdapter
import com.example.myeyes.app.MyApp
import com.example.myeyes.databinding.FragmentInboxContactSentBinding
import com.example.myeyes.model.Sms
import com.example.myeyes.util.Utils
import com.example.myeyes.viewmodel.InboxViewModel
import java.util.*

class Inbox : Fragment() {

    private lateinit var adapter: SmsAdapter
    private val inboxViewModel: InboxViewModel by viewModels()
    private var _binding: FragmentInboxContactSentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInboxContactSentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity?.applicationContext as MyApp).textToSpeech =
            TextToSpeech(activity?.applicationContext) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    val locale = Locale("tr", "TR")
                    if ((activity?.applicationContext as MyApp).textToSpeech?.isLanguageAvailable(
                            locale
                        ) == TextToSpeech.LANG_COUNTRY_AVAILABLE
                    ) {
                        (activity?.applicationContext as MyApp).textToSpeech?.language = locale
                        (activity?.applicationContext as MyApp).textToSpeech?.speak(
                            "Son gelen mesajarı görebilmek için sayfayı yenileyin.",
                            TextToSpeech.QUEUE_FLUSH, null
                        )
                    }
                }
            }

        adapter = SmsAdapter { sms ->
            speakMessage(sms)
        }
        binding.recyclerview.adapter = adapter

        inboxViewModel.apply {
            loadSms()
            isEmptyImage.observe(viewLifecycleOwner) {
                binding.empty.isVisible = it
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
                inboxViewModel.refreshData()
            }
        }
    }

    private fun speakMessage(smsData: Sms) {
        ((activity?.applicationContext as MyApp)).textToSpeech?.speak(
            "message from ${smsData.address.lowercase()}",
            TextToSpeech.QUEUE_FLUSH, null
        )

        ((activity?.applicationContext as MyApp)).textToSpeech?.speak(
            "on ${Utils.convertLongToTime(smsData.date.toLong())}", TextToSpeech.QUEUE_ADD, null
        )
        ((activity?.applicationContext as MyApp)).textToSpeech?.speak(
            smsData.body,
            TextToSpeech.QUEUE_ADD,
            null
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity?.applicationContext as MyApp).textToSpeech?.stop()
        _binding = null
    }
}