package com.example.myeyes.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.net.Uri
import android.os.Build
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.myeyes.R
import com.example.myeyes.config.DoubleClick
import com.example.myeyes.config.DoubleClickListener
import com.example.myeyes.databinding.ContactsCardviewBinding
import com.example.myeyes.model.ContactUser

class ContactViewHolder(private val binding: ContactsCardviewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(context: Context, user: ContactUser, onItemClickListener: (ContactUser) -> Unit) {
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
                onItemClickListener(user)
            }

            override fun onDoubleClick(view: View) {
                val callIntent = Intent(Intent.ACTION_CALL)
                callIntent.data = Uri.parse("tel:" + user.phone_number)
                startActivity(context, callIntent, null)
            }
        }))
    }


}