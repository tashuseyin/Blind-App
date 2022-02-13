package com.example.myeyes.fragmet

import android.os.AsyncTask
import android.os.Bundle
import android.provider.Telephony
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.myeyes.adapter.SmsAdapter
import com.example.myeyes.app.MyApp
import com.example.myeyes.databinding.FragmentSentBinding
import com.example.myeyes.model.Sms
import com.example.myeyes.util.Utils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class SentFragment : Fragment() {
    private lateinit var smsAdapter: SmsAdapter

    private var _binding: FragmentSentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSentBinding.inflate(inflater, container, false)
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

        smsAdapter = SmsAdapter(ArrayList()){ sms->
            speakMessage(sms)
        }
        binding.recyclerview.adapter = smsAdapter

    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        if (isVisibleToUser) getSent()
    }

    private fun speakMessage(smsData: Sms) {
        ((activity?.applicationContext as MyApp)).textToSpeech?.speak("message to ${smsData.address}",
            TextToSpeech.QUEUE_FLUSH, null)

        ((activity?.applicationContext as MyApp)).textToSpeech?.speak(
            "on ${Utils.convertLongToTime(smsData.date.toLong())}", TextToSpeech.QUEUE_ADD, null)

        ((activity?.applicationContext as MyApp)).textToSpeech?.speak(smsData.body, TextToSpeech.QUEUE_ADD, null)

    }

    private fun getSent() {
        LoadSms().execute()
    }

    inner class LoadSms : AsyncTask<String, String, List<Sms>>() {

        override fun doInBackground(vararg params: String?): List<Sms>? {

            val smsList: MutableList<Sms> = ArrayList()

            val cursor = activity?.contentResolver?.query(
                Telephony.Sms.Sent.CONTENT_URI, null, null, null,
                Telephony.Sms.Inbox.DEFAULT_SORT_ORDER)

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    for (i in 1..cursor.count) {
                        val smsData = Sms(
                            cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS)),
                            cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY)),
                            cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.DATE))
                        )

                        smsList.add(smsData)

                        cursor.moveToNext()
                    }
                }

                cursor.close()
                return smsList;
            }

            return null
        }

        override fun onPostExecute(result: List<Sms>?) {
            when {
                result == null -> {
                    binding.progress.visibility = View.GONE
                    binding.empty.visibility = View.VISIBLE
                    binding.recyclerview.visibility = View.GONE

                    ((activity?.applicationContext as MyApp)).textToSpeech?.speak(
                        "sent messages is empty, you can swipe right to see inbox",
                        TextToSpeech.QUEUE_FLUSH, null)
                }
                result.isNotEmpty() -> {
                    binding.progress.visibility = View.GONE
                    binding.empty.visibility = View.GONE
                    smsAdapter?.addItems(result)
                    binding.recyclerview.visibility = View.VISIBLE

                    ((activity?.applicationContext as MyApp)).textToSpeech?.speak(
                        "sent messages is open, click on any item and it will speak the details",
                        TextToSpeech.QUEUE_FLUSH, null)

                    ((activity?.applicationContext as MyApp)).textToSpeech?.speak(
                        "or swipe right to see inbox",
                        TextToSpeech.QUEUE_ADD, null);
                }
                else -> {
                    binding.progress.visibility = View.GONE
                    binding.empty.visibility = View.VISIBLE
                    binding.recyclerview.visibility = View.GONE

                    ((activity?.applicationContext as MyApp)).textToSpeech?.speak(
                        "sent messages is empty, you can swipe right to see inbox",
                        TextToSpeech.QUEUE_FLUSH, null)
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}