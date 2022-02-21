package com.example.dummymvvmproject.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.dummymvvmproject.util.alarm.AlarmRebootService
import com.example.dummymvvmproject.util.alarm.ForegroundService


class BootCompleteReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "onReceive: bootcolte")

        if ("android.intent.action.BOOT_COMPLETED" == intent.action) {
            val alrmIntent=Intent(context,AlarmRebootService::class.java)
               if(Build.VERSION.SDK_INT>Build.VERSION_CODES.O){
                   context.startForegroundService(alrmIntent)
               }else{
                   context.startService(alrmIntent)
               }
        }

//        Intent service = new Intent(context, MsgPushService.class);
//        context.startService(service);
    }

    companion object {
        private const val TAG = "BootCompleteReceiverTAG"
    }
}