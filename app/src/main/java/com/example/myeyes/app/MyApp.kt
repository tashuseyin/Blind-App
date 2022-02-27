package com.example.myeyes.app

import android.app.Application
import android.speech.tts.TextToSpeech
import com.example.myeyes.db.ContactDatabase

class MyApp : Application() {
    var textToSpeech: TextToSpeech? = null

    override fun onCreate() {
        super.onCreate()
        ContactDatabase.initializeDatabase(applicationContext)
    }
}