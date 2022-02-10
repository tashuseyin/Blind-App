package com.example.myeyes.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.myeyes.databinding.SmsCardviewBinding
import com.example.myeyes.model.Sms
import java.text.SimpleDateFormat
import java.util.*

class SmsViewHolder(private val binding: SmsCardviewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(sms: Sms, onItemClickListener: (Sms) -> Unit) {
        binding.body.text = sms.body
        binding.title.text = sms.address
        binding.date.text = convertLongToTime(sms.date.toLong())


        binding.click.setOnClickListener {
            onItemClickListener(sms)
        }
    }

    private fun convertLongToTime(time: Long): String {
        val msgDate = Date(time)
        val format = SimpleDateFormat("dd-MMM-yyyy hh:mm a")
        return format.format(msgDate)
    }
}