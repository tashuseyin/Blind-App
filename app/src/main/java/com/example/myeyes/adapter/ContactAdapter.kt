package com.example.myeyes.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myeyes.databinding.ContactsCardviewBinding
import com.example.myeyes.model.ContactUser

class ContactAdapter(
    private val context: Context,
    private val onItemClickListener: (ContactUser, Int) -> Unit
) : RecyclerView.Adapter<ContactViewHolder>() {

    var contactList: List<ContactUser> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding =
            ContactsCardviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(context, contactList[position], onItemClickListener)
    }

    override fun getItemCount() = contactList.size

    fun addItems(smsDat: List<ContactUser>) {
        contactList = smsDat
        notifyDataSetChanged()
    }
}