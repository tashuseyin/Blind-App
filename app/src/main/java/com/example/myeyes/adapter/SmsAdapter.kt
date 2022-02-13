package com.example.myeyes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myeyes.databinding.SmsCardviewBinding
import com.example.myeyes.model.Sms

class SmsAdapter(var dataList: List<Sms>, private val onItemClickListener: (Sms) -> Unit) :
    RecyclerView.Adapter<SmsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmsViewHolder {
        val binding = SmsCardviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SmsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SmsViewHolder, position: Int) {
        holder.bind(dataList[position], onItemClickListener)
    }


    override fun getItemCount() = dataList.size

    fun addItems(smsDat: List<Sms>) {
        dataList = smsDat
        notifyDataSetChanged()
    }
}