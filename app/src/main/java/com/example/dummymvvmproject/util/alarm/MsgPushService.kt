package com.example.dummymvvmproject.util.alarm

import android.app.Service
import android.os.Build
import android.widget.Toast
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.IBinder
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.Observer
import com.example.dummymvvmproject.R
import com.example.dummymvvmproject.data.local.datastore.AppPrefsStorage
import com.example.dummymvvmproject.data.local.room.CacheMapperPrayer
import com.example.dummymvvmproject.data.local.room.OldCahcerToNewModelMappper
import com.example.dummymvvmproject.data.local.room.PrayerDao
import com.example.dummymvvmproject.data.remote.retrofit.PrayerRetrofit
import com.example.dummymvvmproject.repository.MainRepository
import com.example.dummymvvmproject.ui.fragments.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.io.File


class MsgPushService : LifecycleService() {

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mediaPlayer = MediaPlayer.create(this, R.raw.azaan)
            mediaPlayer.start()

            Toast.makeText(this, "Service Started ocreate", Toast.LENGTH_LONG).show()
        }
    }

    override fun bindService(service: Intent?, conn: ServiceConnection, flags: Int): Boolean {
        return super.bindService(service, conn, flags)
    }

    override fun getDatabasePath(name: String?): File {
        return super.getDatabasePath(name)
    }

}