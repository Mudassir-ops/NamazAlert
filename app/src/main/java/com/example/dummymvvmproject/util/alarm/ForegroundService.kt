package com.example.dummymvvmproject.util.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.LifecycleService
import com.example.dummymvvmproject.ui.fragments.HomeViewModel
import javax.inject.Inject
import com.example.dummymvvmproject.repository.MainRepository
import com.codingwithmitch.daggerhiltplayground.util.DataState
import com.example.dummymvvmproject.model.PrayerData
import android.util.Log
import android.content.Intent
import android.net.ParseException
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class ForegroundService : LifecycleService() {
    private var tvShowViewModel: HomeViewModel? = null

    @JvmField
    @Inject
    var tvShowDataRepo: MainRepository? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate() {
        super.onCreate()
        tvShowViewModel = HomeViewModel(tvShowDataRepo!!)
        tvShowViewModel!!.setStateEvent1("", "")
        tvShowViewModel!!.dataState.observe(this, { listDataState ->
            Log.d(TAG, "onChanged: $$listDataState")
            when (listDataState) {
                is DataState.Success<List<PrayerData>> -> {
                    inintAlarm(listDataState.data)
                }
                else -> {}
            }

        })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        tvShowViewModel!!.dataState.observe(
            this,
            { listDataState -> Log.d(TAG, "onChanged: $$listDataState")
                Log.d(TAG, "onChanged: $$listDataState")
                when (listDataState) {
                    is DataState.Success<List<PrayerData>> -> {
                        inintAlarm(listDataState.data)
                    }
                    else -> {}
                }
            })
        return super.onStartCommand(intent, flags, startId)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun inintAlarm(prayerData: List<PrayerData>) {

        for (prayer: PrayerData in prayerData) {
            Log.d(TAG, "inintAlarm: ${prayer.Isha}")

            if (prayer.isAlarmSetForFajar!!) {
                setAlarm(
                    "Fajr",
                    convertMinutsHoursToTimeStamp(
                        returnHourValue(prayer.Fajr!!).toInt(),
                        returnMinutValue(prayer.Fajr!!).toInt(),
                        prayer.day!!
                    ),
                )
            }
            if (prayer.isAlarmSetForDhur!!) {
                setAlarm(
                    "Dhur",
                    convertMinutsHoursToTimeStamp(
                        returnHourValue(prayer.Dhuhr!!).toInt(),
                        returnMinutValue(prayer.Dhuhr!!).toInt(),
                        prayer.day!!
                    ),
                )
            }
            if (prayer.isAlarmSetForAsr!!) {
                setAlarm(
                    "Asr",
                    convertMinutsHoursToTimeStamp(
                        returnHourValue(prayer.Asr!!).toInt(),
                        returnMinutValue(prayer.Asr!!).toInt(),
                        prayer.day!!
                    ),
                )
            }
            if (prayer.isAlarmSetForMaghrib!!) {
                setAlarm(
                    "Maghrib",
                    convertMinutsHoursToTimeStamp(
                        returnHourValue(prayer.Maghrib!!).toInt(),
                        returnMinutValue(prayer.Maghrib!!).toInt(),
                        prayer.day!!
                    ),
                )
            }
            if (prayer.isAlarmSetForIsha!!) {
                setAlarm(
                    "Isha",
                    convertMinutsHoursToTimeStamp(
                        returnHourValue(prayer.Isha!!).toInt(),
                        returnMinutValue(prayer.Isha!!).toInt(),
                        prayer.day!!
                    ),
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setAlarm(
        prayerName: String,
        timestamp: Long,
    ): Int {
        val timeNow: Calendar? = Calendar.getInstance()
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.timeInMillis = (timestamp)
        if (calendar.before(timeNow)) {
            Log.d(TAG, "added 1 day as time past ")
            calendar.add(Calendar.DATE, 1);

        } else {
            Log.d(TAG, "AlarmHasSet: $timestamp :: $prayerName")
            if (calendar.time > Date()) calendar.add(Calendar.HOUR_OF_DAY, 0)
            val intent = Intent(this, AlarmReceiver::class.java)
            intent.putExtra("CITYNAME", prayerName)
            intent.putExtra("EVENTID", "0")
            intent.putExtra("TEMP", "Satti")
            val alarmManager =
                this.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
            val alarmId = TimeUnit.MILLISECONDS.toSeconds(timestamp).toInt()
            Log.d(TAG, "ALLsetAlarmID:$alarmId $prayerName $timestamp")
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                alarmId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            if (Build.VERSION.SDK_INT < 23) {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )

//                alarmManager.setAlarmClock(
//                    AlarmManager.AlarmClockInfo(
//                        calendar.timeInMillis,
//                        pendingIntent
//                    ), pendingIntent
//                )
            }
        }
        return 0
    }

    private fun convertMinutsHoursToTimeStamp(
        hour: Int,
        minute: Int,
        currentDayOfMOnth: Int
    ): Long {

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        //    calendar[Calendar.MONTH] = Ca ////tareekh  (0...11)
        calendar[Calendar.DAY_OF_MONTH] = currentDayOfMOnth  ////din
        calendar[Calendar.YEAR] = calendar[Calendar.YEAR]
        calendar[Calendar.HOUR_OF_DAY] = hour
        calendar[Calendar.MINUTE] = minute
        calendar[Calendar.SECOND] = 0

        return calendar.timeInMillis
    }

    fun returnHourValue(time: String): String {
        try {
            val sdf = SimpleDateFormat("H:mm")
            val dateObj = sdf.parse(time)
            return SimpleDateFormat("H").format(dateObj)

        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return "00:00"
    }

    fun returnMinutValue(time: String): String {
        //---K is used to return hours in 12 hours format
        try {
            val sdf = SimpleDateFormat("H:mm")
            val dateObj = sdf.parse(time)
            return SimpleDateFormat("mm").format(dateObj)

        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return "00:00"
    }

    companion object {
        private const val TAG = "ForegroundService"
    }
}