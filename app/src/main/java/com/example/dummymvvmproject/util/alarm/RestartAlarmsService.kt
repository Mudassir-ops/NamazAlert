package com.example.dummymvvmproject.util.alarm

import android.app.IntentService
import android.content.Intent

class RestartAlarmsService : IntentService("RestartAlarmsService") {


    override fun onHandleIntent(intent: Intent?) {


        // Restart your alarms here.
        // open database, iterate through every alarm and set them again
    }
}