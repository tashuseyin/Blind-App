package com.example.myeyes.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.myeyes.model.Sms

class SmsDiffUtil(private val oldList: List<Sms>, private val newList: List<Sms>) :
    DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].address == newList[newItemPosition].address
                && oldList[oldItemPosition].body == oldList[oldItemPosition].body
                && oldList[oldItemPosition].date == oldList[oldItemPosition].date
    }
}
