package com.example.myeyes.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myeyes.model.ContactUser

@Database(
    entities = [ContactUser::class],
    version = 1
)
abstract class ContactDatabase : RoomDatabase() {

    abstract fun contactDao(): ContactDao

    companion object {
        @Volatile
        private var INSTANCE: ContactDatabase? = null

        fun initializeDatabase(context: Context) {
            synchronized(this) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    ContactDatabase::class.java,
                    "users"
                ).build()
            }
        }

        fun getDatabase() = INSTANCE
    }
}