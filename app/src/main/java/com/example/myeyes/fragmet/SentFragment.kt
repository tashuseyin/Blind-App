package com.example.myeyes.fragmet

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.myeyes.app.MyApp
import com.example.myeyes.databinding.FragmentInboxContactSentBinding
import com.example.myeyes.model.Sms
import com.example.myeyes.util.Utils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


class SentFragment : Fragment() {


    private var _binding: FragmentInboxContactSentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInboxContactSentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val locale = Locale("tr", "TR")
        (activity?.applicationContext as MyApp).textToSpeech =
            TextToSpeech(activity?.applicationContext) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    if ((activity?.applicationContext as MyApp).textToSpeech?.isLanguageAvailable(
                            locale
                        ) == TextToSpeech.LANG_COUNTRY_AVAILABLE
                    ) {
                        (activity?.applicationContext as MyApp).textToSpeech?.language = locale
                    }
                }
            }
        lifecycleScope.launch {
            delay(100)
            (activity?.applicationContext as MyApp).textToSpeech?.speak(
                "Gönderilen Kutusu",
                TextToSpeech.QUEUE_FLUSH,
                null
            )
        }


    }

    private fun speakMessage(smsData: Sms) {
        ((activity?.applicationContext as MyApp)).textToSpeech?.speak(
            "message to ${smsData.address}",
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
        _binding = null
    }

}