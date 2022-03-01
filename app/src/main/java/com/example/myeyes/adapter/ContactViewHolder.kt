package com.example.myeyes.adapter

import android.content.Context
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Build
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.myeyes.R
import com.example.myeyes.config.DoubleClick
import com.example.myeyes.config.DoubleClickListener
import com.example.myeyes.databinding.ContactsCardviewBinding
import com.example.myeyes.model.ContactUser

class ContactViewHolder(private val binding: ContactsCardviewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        context: Context,
        user: ContactUser,
        onItemClickListener: (ContactUser, Int, Boolean) -> Unit
    ) {
        binding.userTitle.text = user.user_title
        binding.phoneNumber.text = user.phone_number

        val colors = listOf(
            R.color.colorBlue, R.color.colorGreen, R.color.colorRed, R.color.colorPurple,
            R.color.darkGrey
        )
        binding.avatarText.text = user.user_title.first().toString()
        val drawable = ShapeDrawable(OvalShape())


        drawable.paint.color = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.resources.getColor(colors.random(), null)
        } else {
            context.resources.getColor(colors.random())
        }

        binding.avatarText.background = drawable


        binding.click.setOnClickListener(DoubleClick(object : DoubleClickListener {
            override fun onSingleClick(view: View) {
                onItemClickListener(user, 1, false)
            }

            override fun onDoubleClick(view: View) {
                onItemClickListener(user, 2, false)
            }
        }))

        binding.click.setOnLongClickListener {
            onItemClickListener(user, 0, true)
            true
        }
    }
}