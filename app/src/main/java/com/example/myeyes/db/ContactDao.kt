package com.example.myeyes.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myeyes.model.ContactUser

@Dao
interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contactUser: ContactUser)

    @Query("SELECT * FROM ContactUser")
    fun getContactUser(): LiveData<List<ContactUser>>

    @Query("SELECT * FROM ContactUser WHERE phone_number LIKE :searchNumber || '%'")
    fun searchNumber(searchNumber: String): LiveData<List<ContactUser>>
}