package com.example.dummymvvmproject.util.alarm

import android.media.Ringtone
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.NotificationManager
import android.app.Service
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.net.Uri
import android.os.Vibrator
import android.util.Log
import com.example.dummymvvmproject.R

class RingtonePlayingService : Service() {
    var AlarmId = 0
    private val ringtone: Ringtone? = null
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommandHere$intent")


        Toast.makeText(this, "MyAlarmService.onStart()", Toast.LENGTH_LONG).show()


//        NotificationHelper.ringtone.stop()
//        if (NotificationHelper.ringtone.isPlaying) {
//            Toast.makeText(this, "music is playing", Toast.LENGTH_LONG).show()
//            NotificationHelper.ringtone.stop()
//        }
        if (intent == null) {
            Log.d(TAG, "The intent is null.")
            return START_REDELIVER_INTENT
        }
        val action = intent.action
        AlarmId = intent.getIntExtra(ALARM_ID, 0)
        dismissRingtone()

        /*   if ("YES_ACTION".equals(action)) {
            Toast.makeText(this, "YES CALLED", Toast.LENGTH_SHORT).show();
        }
        if (ACTION_DISMISS.equals(action))
            dismissRingtone();
        else {
            Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if (alarmUri == null) {
                alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            }
            ringtone = RingtoneManager.getRingtone(this, alarmUri);
            ringtone.play();
        }*/return START_STICKY
    }

    fun dismissRingtone() {
        // stop the alarm rigntone
        val i = Intent(this, RingtonePlayingService::class.java)
        stopService(i)

        // also dismiss the alarm to ring again or trigger again
        val aManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(baseContext, AlarmReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(this, AlarmId, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        aManager.cancel(pendingIntent)
      //  NotificationHelper.ringtone.stop()
//        NotificationHelper.v!!.cancel()
        NotificationHelper.mp.stop()

        // Canceling the current notification
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(AlarmId)
    }

    override fun onDestroy() {}
    override fun onUnbind(intent: Intent?): Boolean {

        // TODO Auto-generated method stub
        Toast.makeText(this, "MyAlarmService.onUnbind()", Toast.LENGTH_LONG).show()
        return super.onUnbind(intent)
    }
    companion object {
        private val TAG = RingtonePlayingService::class.java.simpleName
        private val URI_BASE = RingtonePlayingService::class.java.name + "."
        val ACTION_DISMISS = URI_BASE + "ACTION_DISMISS"
        const val ALARM_ID = "Alarm_Id"
    }
}