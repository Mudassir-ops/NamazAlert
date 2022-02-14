package com.example.dummymvvmproject.util.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationDismissedReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
      int notificationId = intent.getExtras().getInt("com.my.app.notificationId");
      Log.d("Swipecall", "onReceive: swipe call");
      NotificationHelper.mp.stop();


  }
}