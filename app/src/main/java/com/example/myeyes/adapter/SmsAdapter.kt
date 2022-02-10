package com.example.myeyes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myeyes.databinding.SmsCardviewBinding
import com.example.myeyes.model.Sms

class SmsAdapter(private val onItemClickListener: (Sms) -> Unit) :
    RecyclerView.Adapter<SmsViewHolder>() {

    var datalist = emptyList<Sms>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmsViewHolder {
        val binding = SmsCardviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SmsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SmsViewHolder, position: Int) {
        holder.bind(datalist[position], onItemClickListener)
    }

    override fun getItemCount() = datalist.size

    fun setData(smsData: List<Sms>) {
        val smsDiffUtil = SmsDiffUtil(datalist, smsData)
        val smsDiffUtilResult = DiffUtil.calculateDiff(smsDiffUtil)
        datalist = smsData
        smsDiffUtilResult.dispatchUpdatesTo(this)
    }
}