package com.example.myeyes.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContactUser(
    val id: String,
    val user_title: String,
    val phone_number: String
): Parcelable
