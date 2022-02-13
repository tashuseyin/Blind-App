package com.example.myeyes.util

import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun convertLongToTime(time: Long): String {
        val msgDate = Date(time)
        val format = SimpleDateFormat("dd-MMM-yyyy hh:mm a")
        return format.format(msgDate)
    }
}