package com.example.dummymvvmproject.ui.fragments


import android.app.AlarmManager
import android.app.AlarmManager.AlarmClockInfo
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.net.ParseException
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnTouchListener
import android.widget.CompoundButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.codingwithmitch.daggerhiltplayground.util.DataState
import com.example.dummymvvmproject.AppClass
import com.example.dummymvvmproject.R
import com.example.dummymvvmproject.databinding.AlarmFragmentBinding
import com.example.dummymvvmproject.model.PrayerData
import com.example.dummymvvmproject.util.MyDialog
import com.example.dummymvvmproject.util.alarm.AlarmReceiver
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class AlarmFragment : Fragment(R.layout.alarm_fragment) {

    private lateinit var binding: AlarmFragmentBinding
    private val viewModel: AlarmViewModel by viewModels()
    private lateinit var myDialog: MyDialog

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AlarmFragmentBinding.bind(view)
        myDialog = MyDialog(requireContext())

        subscribeObservers()
        viewModel.getCurrentDayPrayersData(getCurrentDate())
        viewModel.getPrayersDataForAlarm()

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, { dataState ->
            when (dataState) {
                is DataState.Success<PrayerData> -> {
                    displayProgressBar(false)
                    initViewData(dataState.data)
                }
                is DataState.Error -> {
                    displayProgressBar(false)
                    showError(dataState.exception.message!!)
                }
                is DataState.Loading -> {
                    displayProgressBar(true)
                }
                else -> {}
            }
        })

        viewModel.dataState1.observe(viewLifecycleOwner, { dataState ->
            when (dataState) {
                is DataState.Success<List<PrayerData>> -> {
                    displayProgressBar(false)
                }
                is DataState.Error -> {
                    displayProgressBar(false)
                    showError(dataState.exception.message!!)
                }
                is DataState.Loading -> {
                    displayProgressBar(true)
                }
                else -> {}
            }
        })


    }

    private fun showError(msg: String) {
        Snackbar.make(binding.vParent, msg, Snackbar.LENGTH_INDEFINITE).setAction("DISMISS") {
        }.show()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initViewData(
        prayerData: PrayerData
    ) {
        binding.tvTime.text = prayerData.grogreanDate
        binding.tvPlace.text = prayerData.timezone

        "Fajr".also { binding.fajrLayout.tvPrayerName.text = it }
        "Duhr".also { binding.duhrLayout.tvPrayerName.text = it }
        "Assr".also { binding.assrLayout.tvPrayerName.text = it }
        "Mghrib".also { binding.mghribLayout.tvPrayerName.text = it }
        "Isha".also { binding.ishaLayout.tvPrayerName.text = it }


        if (binding.fajrLayout.tvPrayerName.text.equals("Fajr")) {
            binding.fajrLayout.parentLayout.setBackgroundResource(R.drawable.round_background_stroke)
        }

        convert24To12(
            prayerData.Fajr?.substring(0, 5)!!.trim()
        ).also { binding.fajrLayout.tvPrayerTime.text = it }
        convert24To12(
            prayerData.Dhuhr?.substring(0, 5)!!.trim()
        ).also { binding.duhrLayout.tvPrayerTime.text = it }
        convert24To12(
            prayerData.Asr?.substring(0, 5)!!.trim()
        ).also { binding.assrLayout.tvPrayerTime.text = it }
        convert24To12(
            prayerData.Maghrib?.substring(0, 5)!!.trim()
        ).also { binding.mghribLayout.tvPrayerTime.text = it }
        convert24To12(
            prayerData.Isha?.substring(0, 5)!!.trim()
        ).also { binding.ishaLayout.tvPrayerTime.text = it }

        binding.fajrLayout.switchBtnAlarm.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isPressed) {
                if (isChecked) {
                    viewModel.updateIsAlarm(
                        isAlarmSetForFajar = isChecked,
                        isAlarmSetForDhur = prayerData.isAlarmSetForDhur!!,
                        isAlarmSetForAsr = prayerData.isAlarmSetForAsr!!,
                        isAlarmSetForMaghrib = prayerData.isAlarmSetForMaghrib!!,
                        isAlarmSetForIsha = prayerData.isAlarmSetForIsha!!,
                        timestamp = prayerData.timestamp!!
                    )

                    setAlarm(
                        "Fajr",
                        convertMinutsHoursToTimeStamp(
                            returnHourValue(prayerData.Fajr!!).toInt(),
                            returnMinutValue(prayerData.Fajr!!).toInt(),
                            prayerData.day!!
                        ),
                    )

                } else {
                    viewModel.updateIsAlarm(
                        isAlarmSetForFajar = isChecked,
                        isAlarmSetForDhur = prayerData.isAlarmSetForDhur!!,
                        isAlarmSetForAsr = prayerData.isAlarmSetForAsr!!,
                        isAlarmSetForMaghrib = prayerData.isAlarmSetForMaghrib!!,
                        isAlarmSetForIsha = prayerData.isAlarmSetForIsha!!,
                        timestamp = prayerData.timestamp!!
                    )
                    cancelAlarm(
                        "Fajr",
                        convertMinutsHoursToTimeStamp(
                            returnHourValue(prayerData.Fajr!!).toInt(),
                            returnMinutValue(prayerData.Fajr!!).toInt(),
                            prayerData.day!!
                        ),
                    )
                }
            }

        })
        binding.duhrLayout.switchBtnAlarm.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isPressed) {
                if (isChecked) {
                    viewModel.updateIsAlarm(
                        isAlarmSetForFajar = prayerData.isAlarmSetForFajar!!,
                        isAlarmSetForDhur = isChecked,
                        isAlarmSetForAsr = prayerData.isAlarmSetForAsr!!,
                        isAlarmSetForMaghrib = prayerData.isAlarmSetForMaghrib!!,
                        isAlarmSetForIsha = prayerData.isAlarmSetForIsha!!,
                        timestamp = prayerData.timestamp!!
                    )
                    setAlarm(
                        "Duhr",
                        convertMinutsHoursToTimeStamp(
                            returnHourValue(prayerData.Dhuhr!!).toInt(),
                            returnMinutValue(prayerData.Dhuhr!!).toInt(),
                            prayerData.day!!
                        ),
                    )
                } else {
                    viewModel.updateIsAlarm(
                        isAlarmSetForFajar = prayerData.isAlarmSetForFajar!!,
                        isAlarmSetForDhur = isChecked,
                        isAlarmSetForAsr = prayerData.isAlarmSetForAsr!!,
                        isAlarmSetForMaghrib = prayerData.isAlarmSetForMaghrib!!,
                        isAlarmSetForIsha = prayerData.isAlarmSetForIsha!!,
                        timestamp = prayerData.timestamp!!
                    )
                    cancelAlarm(
                        "Duhr",
                        convertMinutsHoursToTimeStamp(
                            returnHourValue(prayerData.Dhuhr!!).toInt(),
                            returnMinutValue(prayerData.Dhuhr!!).toInt(),
                            prayerData.day!!
                        ),
                    )
                }
            }

        })
        binding.assrLayout.switchBtnAlarm.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isPressed) {
                if (isChecked) {
                    viewModel.updateIsAlarm(
                        isAlarmSetForFajar = prayerData.isAlarmSetForFajar!!,
                        isAlarmSetForDhur = prayerData.isAlarmSetForDhur!!,
                        isAlarmSetForAsr = isChecked,
                        isAlarmSetForMaghrib = prayerData.isAlarmSetForMaghrib!!,
                        isAlarmSetForIsha = prayerData.isAlarmSetForIsha!!,
                        timestamp = prayerData.timestamp!!
                    )
                    setAlarm(
                        "Asr",
                        convertMinutsHoursToTimeStamp(
                            returnHourValue(prayerData.Asr!!).toInt(),
                            returnMinutValue(prayerData.Asr!!).toInt(),
                            prayerData.day!!
                        ),
                    )

                } else {
                    viewModel.updateIsAlarm(
                        isAlarmSetForFajar = prayerData.isAlarmSetForFajar!!,
                        isAlarmSetForDhur = prayerData.isAlarmSetForDhur!!,
                        isAlarmSetForAsr = isChecked,
                        isAlarmSetForMaghrib = prayerData.isAlarmSetForMaghrib!!,
                        isAlarmSetForIsha = prayerData.isAlarmSetForIsha!!,
                        timestamp = prayerData.timestamp!!
                    )
                    cancelAlarm(
                        "Asr",
                        convertMinutsHoursToTimeStamp(
                            returnHourValue(prayerData.Asr!!).toInt(),
                            returnMinutValue(prayerData.Asr!!).toInt(),
                            prayerData.day!!
                        ),
                    )
                }
            }
        })
        binding.mghribLayout.switchBtnAlarm.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isPressed) {
                if (isChecked) {
                    viewModel.updateIsAlarm(
                        isAlarmSetForFajar = prayerData.isAlarmSetForFajar!!,
                        isAlarmSetForDhur = prayerData.isAlarmSetForDhur!!,
                        isAlarmSetForAsr = prayerData.isAlarmSetForAsr!!,
                        isAlarmSetForMaghrib = isChecked,
                        isAlarmSetForIsha = prayerData.isAlarmSetForIsha!!,
                        timestamp = prayerData.timestamp!!
                    )

                    setAlarm(
                        "Maghrib",
                        convertMinutsHoursToTimeStamp(
                            returnHourValue(prayerData.Maghrib!!).toInt(),
                            returnMinutValue(prayerData.Maghrib!!).toInt(),
                            prayerData.day!!
                        ),
                    )
                } else {
                    viewModel.updateIsAlarm(
                        isAlarmSetForFajar = prayerData.isAlarmSetForFajar!!,
                        isAlarmSetForDhur = prayerData.isAlarmSetForDhur!!,
                        isAlarmSetForAsr = prayerData.isAlarmSetForAsr!!,
                        isAlarmSetForMaghrib = isChecked,
                        isAlarmSetForIsha = prayerData.isAlarmSetForIsha!!,
                        timestamp = prayerData.timestamp!!
                    )

                    cancelAlarm(
                        "Maghrib",
                        convertMinutsHoursToTimeStamp(
                            returnHourValue(prayerData.Maghrib!!).toInt(),
                            returnMinutValue(prayerData.Maghrib!!).toInt(),
                            prayerData.day!!
                        ),
                    )
                }
            }

        })
        binding.ishaLayout.switchBtnAlarm.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isPressed) {
                if (isChecked) {
                    viewModel.updateIsAlarm(
                        isAlarmSetForFajar = prayerData.isAlarmSetForFajar!!,
                        isAlarmSetForDhur = prayerData.isAlarmSetForDhur!!,
                        isAlarmSetForAsr = prayerData.isAlarmSetForAsr!!,
                        isAlarmSetForMaghrib = prayerData.isAlarmSetForMaghrib!!,
                        isAlarmSetForIsha = isChecked,
                        timestamp = prayerData.timestamp!!
                    )
                    setAlarm(
                        "Isha",
                        convertMinutsHoursToTimeStamp(
                            returnHourValue(prayerData.Isha!!).toInt(),
                            returnMinutValue(prayerData.Isha!!).toInt(),
                            prayerData.day!!
                        ),
                    )
                } else {
                    viewModel.updateIsAlarm(
                        isAlarmSetForFajar = prayerData.isAlarmSetForFajar!!,
                        isAlarmSetForDhur = prayerData.isAlarmSetForDhur!!,
                        isAlarmSetForAsr = prayerData.isAlarmSetForAsr!!,
                        isAlarmSetForMaghrib = prayerData.isAlarmSetForMaghrib!!,
                        isAlarmSetForIsha = isChecked,
                        timestamp = prayerData.timestamp!!
                    )

                    cancelAlarm(
                        "Isha",
                        convertMinutsHoursToTimeStamp(
                            returnHourValue(prayerData.Isha!!).toInt(),
                            returnMinutValue(prayerData.Isha!!).toInt(),
                            prayerData.day!!
                        ),
                    )
                }
            }

        })


        binding.fajrLayout.switchBtnAlarm.isChecked = prayerData.isAlarmSetForFajar!!
        binding.duhrLayout.switchBtnAlarm.isChecked = prayerData.isAlarmSetForDhur!!
        binding.assrLayout.switchBtnAlarm.isChecked = prayerData.isAlarmSetForAsr!!
        binding.mghribLayout.switchBtnAlarm.isChecked = prayerData.isAlarmSetForMaghrib!!
        binding.ishaLayout.switchBtnAlarm.isChecked = prayerData.isAlarmSetForIsha!!


    }

    private fun displayProgressBar(isDisplayed: Boolean) {
        if (isDisplayed) myDialog.show() else myDialog.dismiss()
    }

    fun convert24To12(time: String): String {
        try {
            val sdf = SimpleDateFormat("H:mm")
            val dateObj = sdf.parse(time)
            return SimpleDateFormat("K:mm a").format(dateObj)

        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return "00:00"
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDate(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return current.format(formatter)
    }

    private fun cancelAlarm(
        prayerName: String,
        timestamp: Long,
    ): Int {
        val timeNow: Calendar? = Calendar.getInstance()
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.timeInMillis = (timestamp)

        Log.d(TAG, "cancelll: $timestamp :: $prayerName")
        if (calendar.time > Date()) calendar.add(Calendar.HOUR_OF_DAY, 0)
        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        intent.putExtra("CITYNAME", prayerName)
        intent.putExtra("EVENTID", "0")
        intent.putExtra("TEMP", "Satti")
        val alarmManager =
            requireActivity().getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        val alarmId = TimeUnit.MILLISECONDS.toSeconds(timestamp).toInt()
        Log.d(TAG, "ALLsetAlarmID: $alarmId  $prayerName")
        val displayIntent = PendingIntent.getBroadcast(
            requireContext(),
            alarmId,
            intent,
            FLAG_UPDATE_CURRENT
        )

        if (displayIntent != null) {
            alarmManager.cancel(displayIntent);
            displayIntent.cancel();
        }

        return 0
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
            val intent = Intent(requireContext(), AlarmReceiver::class.java)
            intent.putExtra("CITYNAME", prayerName)
            intent.putExtra("EVENTID", "0")
            intent.putExtra("TEMP", "Satti")
            val alarmManager =
                requireActivity().getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager

            val alarmId = TimeUnit.MILLISECONDS.toSeconds(timestamp).toInt()
            Log.d(TAG, "setAlarmHere: $alarmId")
            val pendingIntent = PendingIntent.getBroadcast(
                requireContext(),
                alarmId,
                intent,
                 FLAG_UPDATE_CURRENT
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
            }
        }
        return 0
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

    private fun convertMinutsHoursToTimeStamp(hour: Int, minute: Int, currentDayOfMOnth: Int): Long {

        val calendar = Calendar.getInstance()
        //  calendar.timeInMillis = System.currentTimeMillis()
        //    calendar[Calendar.MONTH] = Ca ////tareekh  (0...11)
        calendar[Calendar.DAY_OF_MONTH] = currentDayOfMOnth  ////din
        calendar[Calendar.YEAR] = calendar[Calendar.YEAR]
        calendar[Calendar.HOUR_OF_DAY] = hour
        calendar[Calendar.MINUTE] = minute
        calendar[Calendar.SECOND] = 0

        return calendar.timeInMillis
    }

    private val TAG = AlarmFragment::class.java.simpleName
}

