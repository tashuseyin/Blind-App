package com.example.myeyes.fragmet

import android.os.Bundle
import android.provider.Telephony
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.example.myeyes.adapter.SmsAdapter
import com.example.myeyes.app.MyApp
import com.example.myeyes.databinding.FragmentInboxBinding
import com.example.myeyes.model.Sms
import com.example.myeyes.util.Utils

class Inbox : Fragment() {

    private lateinit var adapter: SmsAdapter
    private val smsLiveData: MutableLiveData<ArrayList<Sms>> = MutableLiveData(ArrayList())
    private var _binding: FragmentInboxBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInboxBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        adapter = SmsAdapter { sms ->
            speakMessage(sms)
        }
        setVisible()
        binding.recyclerview.adapter = adapter
    }

    private fun loadSms(): ArrayList<Sms> {
        val dataList = ArrayList<Sms>()

        val cursor = activity?.contentResolver?.query(
            Telephony.Sms.Inbox.CONTENT_URI,
            null,
            null,
            null,
            Telephony.Sms.Inbox.DEFAULT_SORT_ORDER
        )

        cursor?.let { c ->
            if (c.moveToFirst()) {
                for (i in 1..c.count) {
                    val smsData = Sms(
                        c.getString(c.getColumnIndexOrThrow(Telephony.Sms.ADDRESS)),
                        c.getString(c.getColumnIndexOrThrow(Telephony.Sms.BODY)),
                        c.getString(c.getColumnIndexOrThrow(Telephony.Sms.DATE))
                    )
                    dataList.add(smsData)
                    c.moveToNext()
                }
            }
            c.close()
        }
        return dataList
    }

    private fun setVisible() {
        val result = loadSms()
        smsLiveData.value = result
        if (result.isEmpty()) {
            binding.progress.isVisible = false
            binding.empty.isVisible = true
            binding.recyclerview.isVisible = false
        }
        if (result.isNotEmpty()) {
            binding.progress.isVisible = false
            binding.empty.isVisible = false
            smsLiveData.observe(viewLifecycleOwner) {
                adapter.addItems(it)
            }
            binding.recyclerview.isVisible = true
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
            smsData.body.lowercase(),
            TextToSpeech.QUEUE_ADD,
            null
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}