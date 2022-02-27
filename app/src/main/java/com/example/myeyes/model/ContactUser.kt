package com.example.myeyes.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ContactUser")
data class ContactUser(
    @PrimaryKey(autoGenerate = false)
    val uid: String,
    val user_title: String,
    val phone_number: String
)