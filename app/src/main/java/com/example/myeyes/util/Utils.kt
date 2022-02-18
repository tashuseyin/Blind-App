package com.example.myeyes.util

import android.app.Activity
import android.speech.tts.TextToSpeech
import com.example.myeyes.app.MyApp
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun convertLongToTime(time: Long): String {
        val msgDate = Date(time)
        val format = SimpleDateFormat("dd-MMM-yyyy hh:mm a")
        return format.format(msgDate)
    }

    fun textToSpeechFunctionMain(activity: Activity, message: String) {
        (activity.applicationContext as MyApp).textToSpeech =
            TextToSpeech(activity.applicationContext) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    val locale = Locale("tr", "TR")
                    if ((activity.applicationContext as MyApp).textToSpeech?.isLanguageAvailable(
                            locale
                        ) == TextToSpeech.LANG_COUNTRY_AVAILABLE
                    ) {
                        (activity.applicationContext as MyApp).textToSpeech?.language = locale
                        (activity.applicationContext as MyApp).textToSpeech?.speak(
                            message,
                            TextToSpeech.QUEUE_FLUSH, null
                        )
                    }
                }
            }
    }

    fun textToSpeechFunctionBasic(activity: Activity, message: String) {
        (activity.applicationContext as MyApp).textToSpeech?.speak(
            message,
            TextToSpeech.QUEUE_FLUSH, null
        )
    }

}