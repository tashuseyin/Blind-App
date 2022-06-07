package com.example.myeyes.util

import android.app.Activity
import android.speech.tts.TextToSpeech
import com.example.myeyes.app.MyApp
import com.example.myeyes.util.Utils.mmToCm
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

    fun CM_TO_MM(cm: Float): Float {
        return cm * 10
    }

    fun CM_TO_MM(cm: Int): Float {
        return cm * 10.0f
    }

    fun Float.mmToCm(): Int{
        return (this/10).toInt()
    }
    fun MM_TO_CM(mm: Float): Int {
        return (mm/10).toInt()
    }

    fun MM_TO_CM(mm: Int): Float {
        return mm / 10.0f
    }
}