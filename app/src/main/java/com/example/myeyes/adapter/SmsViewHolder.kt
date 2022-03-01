package com.example.myeyes.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.myeyes.databinding.SmsCardviewBinding
import com.example.myeyes.model.Sms
import com.example.myeyes.util.Utils

class SmsViewHolder(private val binding: SmsCardviewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(sms: Sms, onItemClickListener: (Sms) -> Unit) {
        binding.body.text = sms.body
        binding.title.text = sms.address
        binding.date.text = Utils.convertLongToTime(sms.date.toLong())


        binding.click.setOnClickListener {
            onItemClickListener(sms)
        }
    }
}