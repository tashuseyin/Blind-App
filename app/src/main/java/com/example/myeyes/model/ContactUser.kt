package com.example.myeyes.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ContactUser(
    val user_title: String,
    @PrimaryKey
    val uid: String,
    val phone_number: String
)