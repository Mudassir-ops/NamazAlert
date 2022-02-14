package com.example.dummymvvmproject.util.alarm

import android.content.ContextWrapper
import android.content.ContentResolver
import androidx.core.app.NotificationCompat
import android.app.NotificationManager
import android.annotation.TargetApi
import android.os.Build
import android.app.NotificationChannel
import android.os.Vibrator
import android.media.Ringtone
import android.content.Intent
import android.app.PendingIntent
import android.content.Context
import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import com.example.dummymvvmproject.R
import java.lang.Exception

class NotificationHelper(base: Context, name: String, Id: Int) : ContextWrapper(base) {
    var soundUri =
        Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + applicationContext.packageName + "/" + R.raw.azaan)
    var actionYes: NotificationCompat.Action
    private var name = ""
    private var id = 0
    private var mManager: NotificationManager? = null
    var sound: Uri? = null

    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val channel =
            NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH)
        manager!!.createNotificationChannel(channel)
        val pattern = longArrayOf(0, 100, 1000, 300, 200, 100, 500, 200, 100)
        try {
//        //    val notification = Uri.parse("android.resource://" + packageName + "/" + R.raw.azaan)
//            val notification = Uri.parse("android.resource://${packageName}/raw/azaan")
//           Uri.parse("android.resource://${packageName}/raw/azaan")
//            Log.d("alarmmusicpath", "createChannel: $notification")
//            v = getSystemService(VIBRATOR_SERVICE) as Vibrator
//            ringtone = RingtoneManager.getRingtone(applicationContext, soundUri)
//            v!!.vibrate(VibrationEffect.createWaveform(pattern, 3))
//            ringtone.play()
//            Log.d("asdasdds", "createChannel: insd")
//              v!!.vibrate(VibrationEffect.createOneShot(3000, VibrationEffect.DEFAULT_AMPLITUDE));


            val kkk = Uri.parse("android.resource://" + packageName + "/" + R.raw.azaan)
            mp = MediaPlayer.create(baseContext, kkk)
            mp.setVolume(100f, 100f)
            mp.start()

            //  mp.setOnCompletionListener { mp -> mp.release() }

            val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(400)

        } catch (e: Exception) {
            Log.d("asdasdds", "createChannel: insd$e")
            e.printStackTrace()
        }
    }

    val manager: NotificationManager?
        get() {
            if (mManager == null) {
                mManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            }
            return mManager
        }
    val channelNotification: NotificationCompat.Builder
        get() {
            val mBuilder = NotificationCompat.Builder(applicationContext, channelID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(name)
//                .setContentText("Alarm")
                .addAction(actionYes)
                .setDeleteIntent(createOnDismissedIntent(baseContext, id))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLights(Color.MAGENTA, 500, 500)

            mBuilder.setVibrate(longArrayOf(500, 500))
            val mNotifyMgr = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            mNotifyMgr.notify(id, mBuilder.build())
            return mBuilder
        }

    companion object {
        const val channelID = "channelID"
        const val channelName = "Channel Name"
        var r: Ringtone? = null

        //        lateinit var ringtone: Ringtone
        lateinit var mp: MediaPlayer
        var v: Vibrator? = null
    }

    init {
        this.name = name
        id = Id
        sound =
            Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + base.packageName + "/" + R.raw.azaan)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
        val yesIntent = Intent(this, RingtonePlayingService::class.java)
        yesIntent.putExtra(RingtonePlayingService.ALARM_ID, id)
        val pendingIntentYes =
            PendingIntent.getService(this, id, yesIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        actionYes = NotificationCompat.Action(
            android.R.drawable.ic_lock_idle_alarm,
            "Dismiss",
            pendingIntentYes
        )
    }

    private fun createOnDismissedIntent(
        context: Context,
        notificationId: Int
    ): PendingIntent? {
        val intent = Intent(context, NotificationDismissedReceiver::class.java)

//        val service = Intent(context, NotificationService::class.java)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            context.startForegroundService(service)
//        } else {
//            context.startService(service)
//        }
        intent.putExtra("com.my.app.notificationId", id)
        return PendingIntent.getBroadcast(
            context.applicationContext,
            notificationId, intent, 0
        )
    }
}