package com.example.myeyes.db

import com.example.myeyes.model.ContactUser

object ContactRepository {

    private val contactDao by lazy {
        ContactDatabase.getDatabase()?.contactDao()
    }

    suspend fun insert(user: ContactUser) {
        contactDao?.insert(user)
    }

    fun getContactUser() = contactDao?.getContactUser()

    fun searchPhoneNumber(searchNumber: String) = contactDao?.searchNumber(searchNumber)
}