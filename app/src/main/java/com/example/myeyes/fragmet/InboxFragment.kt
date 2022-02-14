package com.example.myeyes.fragmet

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myeyes.adapter.SmsAdapter
import com.example.myeyes.databinding.FragmentInboxBinding
import com.example.myeyes.model.Sms
import java.util.*


class InboxFragment : Fragment() {

    private lateinit var smsAdapter: SmsAdapter
    private var textToSpeech: TextToSpeech? = null


    private var _binding: FragmentInboxBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentInboxBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val locale = Locale("tr", "TR")



    }

    private fun speakMessage(sms: Sms) {

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}