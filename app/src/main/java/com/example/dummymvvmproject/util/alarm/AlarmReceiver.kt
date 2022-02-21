package com.example.dummymvvmproject.util.alarm

import android.content.BroadcastReceiver
import android.content.Intent

import android.content.Context
import android.media.MediaPlayer

import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.dummymvvmproject.R
import android.os.Build





class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent != null) {
            Log.e("ALARMValues", "ALARM RUNNING" + intent.flags)
//             val mediaPlayer: MediaPlayer = MediaPlayer.create(context, R.raw.azaan)
//             mediaPlayer.start()

            val name = intent.getStringExtra("CITYNAME")
            val id = intent.getIntExtra("EVENTID", 0)
            val temp = intent.getStringExtra("TEMP")
            val notificationHelper = NotificationHelper(context, name!!,id)
            val nb: NotificationCompat.Builder = notificationHelper.channelNotification
            notificationHelper.manager!!.notify(id, nb.build())
        }
    }
}
