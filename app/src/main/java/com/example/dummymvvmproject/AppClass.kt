package com.example.dummymvvmproject

import android.app.Activity
import android.app.Application
import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class
 */
@HiltAndroidApp
class AppClass : MultiDexApplication() {
    companion object{
        private var cInstance: AppClass? = null
        private var mCurrentActivity: Activity? = null
        @Synchronized
        fun getInstance(): AppClass {
            return cInstance!!
        }

        fun getCurrentActivity(): Activity? {
            return mCurrentActivity
        }

        fun setCurrentActivity(mCurrentActivity: Activity?) {
            this.mCurrentActivity = mCurrentActivity
        }
    }

    init {
        cInstance = this
    }
}