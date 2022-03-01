package com.example.myeyes.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "ContactUser")
data class ContactUser(
    @PrimaryKey(autoGenerate = false)
    val uid: String,
    val user_title: String,
    val phone_number: String
) : Parcelable