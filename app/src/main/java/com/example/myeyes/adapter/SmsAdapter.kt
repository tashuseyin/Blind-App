package com.example.myeyes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myeyes.databinding.SmsCardviewBinding
import com.example.myeyes.model.Sms

class SmsAdapter(private val onItemClickListener: (Sms) -> Unit) :
    RecyclerView.Adapter<SmsViewHolder>() {

    private var dataList = ArrayList<Sms>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmsViewHolder {
        val binding = SmsCardviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SmsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SmsViewHolder, position: Int) {
        holder.bind(dataList[position], onItemClickListener)
    }


    override fun getItemCount() = dataList.size

    fun addItems(smsDat: ArrayList<Sms>) {
        dataList = smsDat
        notifyDataSetChanged()
    }
}