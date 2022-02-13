package com.example.myeyes.fragmet

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.myeyes.adapter.SmsAdapter
import com.example.myeyes.app.MyApp
import com.example.myeyes.databinding.FragmentInboxBinding
import com.example.myeyes.model.Sms
import com.example.myeyes.util.Utils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class InboxFragment : Fragment() {

    private lateinit var adapter: SmsAdapter

    private var _binding: FragmentInboxBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInboxBinding.inflate(inflater, container, false)
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
                "Gelen Kutusu",
                TextToSpeech.QUEUE_FLUSH,
                null
            )
        }

        adapter = SmsAdapter(ArrayList()) {
            speakMessage(it)
        }
        binding.recyclerview.adapter = adapter
    }


    private fun speakMessage(smsData: Sms) {
        ((activity?.applicationContext as MyApp)).textToSpeech?.speak(
            "message from ${smsData.address}",
            TextToSpeech.QUEUE_FLUSH, null
        )

        ((activity?.applicationContext as MyApp)).textToSpeech?.speak(
            "on ${Utils?.convertLongToTime(smsData.date.toLong())}", TextToSpeech.QUEUE_ADD, null
        )

        ((activity?.applicationContext as MyApp)).textToSpeech?.speak(
            smsData.body,
            TextToSpeech.QUEUE_ADD,
            null
        )

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun LoadSms(): List<Sms> {
        val uri = Uri.parse("content://sms/inbox")
        val smsList: MutableList<Sms> = ArrayList()

        val cursor = activity?.contentResolver?.query(uri, null, null, null)

        cursor?.let { c ->
            if (c.moveToFirst()) {
                for (i in 1..c.count){
                    val smsData = Sms(

                    )

                }
            }

        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}