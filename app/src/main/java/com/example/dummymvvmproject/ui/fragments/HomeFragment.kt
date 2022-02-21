package com.example.dummymvvmproject.ui.fragments

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.ParseException
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Context.ALARM_SERVICE
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels

import androidx.lifecycle.Observer
import com.codingwithmitch.daggerhiltplayground.util.DataState
import com.example.dummymvvmproject.R
import com.example.dummymvvmproject.databinding.HomeFragmentBinding
import com.example.dummymvvmproject.model.PrayerData
import com.example.dummymvvmproject.ui.activities.MainStateEvent
import com.example.dummymvvmproject.util.alarm.AlarmReceiver
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.Locale
import java.util.concurrent.TimeUnit
import android.content.IntentSender

import android.location.LocationManager
import android.net.Uri
import android.provider.Settings

import com.example.dummymvvmproject.BuildConfig
import com.example.dummymvvmproject.util.*
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import java.text.DateFormat
import java.util.regex.Matcher
import java.util.regex.Pattern
import android.view.animation.OvershootInterpolator

import android.view.animation.ScaleAnimation
import android.location.Geocoder


@AndroidEntryPoint
@ExperimentalCoroutinesApi
class HomeFragment : Fragment(R.layout.home_fragment) {
    private var lat = ""
    private var lng = ""
    private val TAG = HomeFragment::class.java.simpleName

    val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: HomeFragmentBinding
    private lateinit var myDialog: MyDialog
    private lateinit var locationManager: LocationManager

    private var mCurrentLocation: Location? = null
    private var mRequestingLocationUpdates: Boolean? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var i = 0

    companion object {

        private const val REQUEST_FINE_LOCATION_PERMISSIONS_REQUEST_CODE = 34
        const val REQUEST_CHECK_SETTINGS = 100
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onAttach: ")
        locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val permissionApproved =
            requireContext().hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissionApproved) {
            if (isGpsEnabled()) {
                if (mCurrentLocation == null) {
                    setUpLocationListener()
                }

            } else {
                itWillEnableGps()
            }

        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_FINE_LOCATION_PERMISSIONS_REQUEST_CODE
            )
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = HomeFragmentBinding.bind(view)
        myDialog = MyDialog(requireContext())
        val scale = ScaleAnimation(
            0F,
            1F,
            0F,
            1F,
            ScaleAnimation.RELATIVE_TO_SELF,
            .5f,
            ScaleAnimation.RELATIVE_TO_SELF,
            .5f
        )
        scale.duration = 300
        scale.interpolator = OvershootInterpolator()
        subscribeObservers()
        binding.MarqueeText.isSelected = true

         //----Allah Hu Akabr
        binding.apply {
            var count = 0
            Log.d(TAG, "onViewCreated: clcikekdk")
            countLayout.setOnClickListener {
                countLayout.startAnimation(scale)
                count++
                if (count >= 31) {
                    countLayout.isEnabled = false
                }
                tvTasbeehCount.text = "${count}/31"
            }

            //----Subhan Allah
            var count1 = 0
            Log.d(TAG, "onViewCreated: clcikekdk")
            countLayout1.setOnClickListener {
                countLayout1.startAnimation(scale)
                count1++
                if (count1 >= 31) {
                    countLayout1.isEnabled = false
                }
                tvTasbeehCount1.text = "${count1}/31"
            }

            //----Al HumduLillah
            var count2 = 0
            Log.d(TAG, "onViewCreated: clcikekdk")
            countLayout2.setOnClickListener {
                countLayout2.startAnimation(scale)
                count2++
                if (count2 >= 31) {
                    countLayout2.isEnabled = false
                }
                tvTasbeehCount2.text = "${count2}/31"
            }


        }


    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<List<PrayerData>> -> {
                    displayProgressBar(false)

                    val dateFormat: DateFormat = SimpleDateFormat("d")
                    val date = Date()
                    Log.d("Month", dateFormat.format(date))
                    val day = dateFormat.format(date).toInt() - 1
                    appendBlogTitles(dataState.data[day])
                    //   Toast.makeText(requireContext(), "Sucess means Db", Toast.LENGTH_SHORT).show()
//                    CoroutineScope(Dispatchers.IO).launch {
//                        inintAlarm(dataState.data)
//                    }
                }
                is DataState.Error -> {
                    displayProgressBar(false)
                    showError(dataState.exception.message!!)
                }
                is DataState.Loading -> {
                    Toast.makeText(requireContext(), "datestaeloading", Toast.LENGTH_SHORT).show()
                    displayProgressBar(true)
                }
                is DataState.General -> {
                    displayProgressBar(false)
                    Toast.makeText(
                        requireContext(),
                        "General means 1st Network Call",
                        Toast.LENGTH_SHORT
                    ).show()

                    Log.d(TAG, "subscribeObservers: ${dataState.data.size}")
                    val dateFormat: DateFormat = SimpleDateFormat("d")
                    val date = Date()
                    Log.d("Month", dateFormat.format(date))
                    val day = dateFormat.format(date).toInt() - 1
                    appendBlogTitles(dataState.data[day])
                    inintAlarm(dataState.data)

                }
            }
        })
    }


    private fun showError(msg: String) {
        Snackbar.make(binding.vParent, msg, Snackbar.LENGTH_INDEFINITE).setAction("DISMISS") {
        }.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun appendBlogTitles(prayerData: PrayerData) {
        when {
            isTimeBetweenTwoTime(
                getExactTime(prayerData.Fajr.toString()),
                getExactTime(prayerData.Dhuhr.toString()),
                getCurrentTime()
            )
            -> {
                initViewData(
                    prayerData.Fajr!!,
                    prayerData.Dhuhr!!,
                    prayerData.timezone!!,
                    "Fajr",
                    "Dhur",
                    prayerData.longitude,
                    prayerData.latitude
                )
            }
            isTimeBetweenTwoTime(
                getExactTime(prayerData.Dhuhr.toString()),
                getExactTime(prayerData.Asr.toString()),
                getCurrentTime()
            )
            -> {
                initViewData(
                    prayerData.Dhuhr!!,
                    prayerData.Asr!!,
                    prayerData.timezone!!,
                    "Dhur",
                    "Asr",
                    prayerData.longitude,
                    prayerData.latitude
                )
            }
            isTimeBetweenTwoTime(
                getExactTime(prayerData.Asr.toString()),
                getExactTime(prayerData.Maghrib.toString()),
                getCurrentTime()
            )
            -> {
                initViewData(
                    prayerData.Asr!!,
                    prayerData.Maghrib!!,
                    prayerData.timezone!!,
                    "Asr",
                    "Maghrib",
                    prayerData.longitude,
                    prayerData.latitude
                )
            }
            isTimeBetweenTwoTime(
                getExactTime(prayerData.Maghrib.toString()),
                getExactTime(prayerData.Isha.toString()),
                getCurrentTime()
            )
            -> {
                initViewData(
                    prayerData.Maghrib!!,
                    prayerData.Isha!!,
                    prayerData.timezone!!,
                    "Maghrib",
                    "Isha",
                    prayerData.longitude,
                    prayerData.latitude
                )
            }
            isTimeBetweenTwoTime(
                getExactTime(prayerData.Isha.toString()),
                getExactTime(prayerData.Fajr.toString()),
                getCurrentTime()
            )
            -> {
                initViewData(
                    prayerData.Isha!!,
                    prayerData.Fajr!!,
                    prayerData.timezone!!,
                    "Isha",
                    "Fajr",
                    prayerData.longitude,
                    prayerData.latitude
                )
            }
        }

    }

    private fun displayProgressBar(isDisplayed: Boolean) {
        if (isDisplayed) myDialog.show() else myDialog.dismiss()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val states = LocationSettingsStates.fromIntent(data!!)
        Log.d(TAG, "onActivityResult: fragment")
        when (requestCode) {
            REQUEST_CHECK_SETTINGS -> when (resultCode) {
                AppCompatActivity.RESULT_OK -> {
                    Toast.makeText(
                        requireContext(),
                        "User has clicked on OK - So GPS is on",
                        Toast.LENGTH_SHORT
                    ).show()
                    setUpLocationListener()

                }                 // All required changes were successfully made

                AppCompatActivity.RESULT_CANCELED ->                         // The user was asked to change settings, but chose not to
                    Toast.makeText(
                        requireContext(),
                        "User has clicked on NO, THANKS - So GPS is still off.",
                        Toast.LENGTH_SHORT
                    ).show()
                else -> {}
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(TAG, "onRequestPermissionResult()")

        if (requestCode == REQUEST_FINE_LOCATION_PERMISSIONS_REQUEST_CODE) {
            when {
                grantResults.isEmpty() ->
                    Log.d(TAG, "User interaction was cancelled.")

                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    if (isGpsEnabled()) {
                        setUpLocationListener()
                    } else {
                        itWillEnableGps()
                    }
                }

                else -> {
                    Snackbar.make(
                        binding.vParent,
                        R.string.fine_permission_denied_explanation,
                        Snackbar.LENGTH_LONG
                    )
                        .setAction(R.string.settings) {
                            // Build intent that displays the App settings screen.
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts(
                                "package",
                                BuildConfig.APPLICATION_ID,
                                null
                            )
                            intent.data = uri
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                        .show()
                }
            }
        }
    }

    private fun setUpLocationListener() {
        Log.d(TAG, "setUpLocationListener: ")
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        if (mCurrentLocation == null) {
            val locationRequest = LocationRequest()
            locationRequest.interval = 30000
            locationRequest.fastestInterval = 10000
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.smallestDisplacement = 30.0f
            val builder = LocationSettingsRequest.Builder()
            builder.addLocationRequest(locationRequest).build()

            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        super.onLocationResult(locationResult)
                        mCurrentLocation = locationResult.lastLocation
                        mRequestingLocationUpdates = true
                        updateLocationUI()
                        Log.d(TAG, "onLocationResult: $mCurrentLocation ")

                    }
                },
                Looper.myLooper()!!
            )
        }

    }

    private fun updateLocationUI() {
        if (mCurrentLocation != null) {
            viewModel.setStateEvent(
                MainStateEvent.GetBlogsEvent,
                mCurrentLocation!!.latitude.toString(),
                mCurrentLocation!!.longitude.toString()
            )

        }

    }

    fun getExactTime(time: String): String {
        val spacePos = time.indexOf(" ")
        if (spacePos > 0) {
            return "${time.substring(0, spacePos)}:00"
        }
        return ""
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentTime(): String {
        return SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
    }

    fun getNextTime(time: String): String {

        Log.d(TAG, "getNextTime: $time")
        val format = SimpleDateFormat("hh:mm a EEE", Locale.ENGLISH)
        val calendar = Calendar.getInstance()
        val date = calendar.time
        val day = SimpleDateFormat("EE", Locale.ENGLISH).format(date.time)
        Log.d(TAG, "getNextTime: $day")
        var amOrPm = ""
        try {
            val sdf = SimpleDateFormat("H:mm")
            val dateObj = sdf.parse(time)
            amOrPm = SimpleDateFormat("K:mm a").format(dateObj)

        } catch (e: ParseException) {
            e.printStackTrace()
        }
        calendar.timeInMillis = format.parse("$amOrPm $day").time
        SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        val now = Calendar.getInstance()
        val target = Calendar.getInstance()

        if (calendar[Calendar.DAY_OF_WEEK] > now[Calendar.DAY_OF_WEEK]) {
            target[Calendar.HOUR_OF_DAY] = calendar[Calendar.HOUR_OF_DAY]
            target[Calendar.MINUTE] = calendar[Calendar.MINUTE]
            target.add(Calendar.DATE, calendar[Calendar.DAY_OF_WEEK] - now[Calendar.DAY_OF_WEEK])
        } else if (calendar[Calendar.DAY_OF_WEEK] < now[Calendar.DAY_OF_WEEK]) {
            target[Calendar.HOUR_OF_DAY] = calendar[Calendar.HOUR_OF_DAY]
            target[Calendar.MINUTE] = calendar[Calendar.MINUTE]
            target.add(
                Calendar.DATE,
                7 - (now[Calendar.DAY_OF_WEEK] - calendar[Calendar.DAY_OF_WEEK])
            )
        } else {
            if (calendar[Calendar.HOUR_OF_DAY] > now[Calendar.HOUR_OF_DAY]) {
                target[Calendar.HOUR_OF_DAY] = calendar[Calendar.HOUR_OF_DAY]
                target[Calendar.MINUTE] = calendar[Calendar.MINUTE]
            } else if (calendar[Calendar.HOUR_OF_DAY] < now[Calendar.HOUR_OF_DAY]) {
                target[Calendar.HOUR_OF_DAY] = calendar[Calendar.HOUR_OF_DAY]
                target[Calendar.MINUTE] = calendar[Calendar.MINUTE]
                target.add(Calendar.DATE, 7)
            } else {
                if (calendar[Calendar.MINUTE] > now[Calendar.MINUTE]) {
                    target[Calendar.HOUR_OF_DAY] = calendar[Calendar.HOUR_OF_DAY]
                    target[Calendar.MINUTE] = calendar[Calendar.MINUTE]
                } else if (calendar[Calendar.MINUTE] < now[Calendar.MINUTE]) {
                    target[Calendar.HOUR_OF_DAY] = calendar[Calendar.HOUR_OF_DAY]
                    target[Calendar.MINUTE] = calendar[Calendar.MINUTE]
                    target.add(Calendar.DATE, 7)
                } else {
                    return String.format("%02d:%02d", "00", "00")
                    // target time is now!
                }
            }
        }

        val remainingTime = target.timeInMillis - now.timeInMillis
        val hr: Long = TimeUnit.MILLISECONDS.toHours(remainingTime)
        val min: Long = TimeUnit.MILLISECONDS.toMinutes(remainingTime - TimeUnit.HOURS.toMillis(hr))
        return String.format("%02d:%02d", hr, min)

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

    fun initViewData(
        firstNumaz: String,
        secondNumaz: String,
        timezone: String,
        firstNumazName: String,
        secondNumazName: String,
        longitude: Double?,
        latitude: Double?
    ) {
        binding.tvTime.text = convert24To12(firstNumaz.substring(0, 5).trim())
        binding.tvTimeOfPrayer.text = "Now $firstNumazName"
        binding.tvPrayerLabel.text = getString(
            R.string.next_numaz_string,
            secondNumazName,
            convert24To12(secondNumaz.substring(0, 5).trim())
        )
        binding.tvTimeRemainigPrayer.text =
            getString(R.string.first_numaz_string, test(secondNumaz.substring(0, 5).trim()))
        binding.tvPlace.text = timezone
        binding.tvCity.isSelected = true
        binding.tvCity.text = getCityName(latitude!!, longitude!!)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Throws(ParseException::class)
    fun isTimeBetweenTwoTime(
        argStartTime: String,
        argEndTime: String, argCurrentTime: String
    ): Boolean {

        val reg = Regex("^([0-1][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$")
        //
        return if (argStartTime.matches(reg) && argEndTime.matches(reg)
            && argCurrentTime.matches(reg)
        ) {
            var valid = false

            // Start Time
            var startTime: Date = SimpleDateFormat("HH:mm:ss").parse(argStartTime)
            val startCalendar: Calendar = Calendar.getInstance()
            startCalendar.setTime(startTime)


            // Current Time
            var currentTime: Date = SimpleDateFormat("HH:mm:ss").parse(argCurrentTime)
            val currentCalendar: Calendar = Calendar.getInstance()
            currentCalendar.setTime(currentTime)

            // End Time
            var endTime: Date = SimpleDateFormat("HH:mm:ss").parse(argEndTime)
            val endCalendar: Calendar = Calendar.getInstance()
            endCalendar.setTime(endTime)

            //
            if (currentTime.compareTo(endTime) < 0) {
                currentCalendar.add(Calendar.DATE, 1)
                currentTime = currentCalendar.getTime()
            }
            if (startTime.compareTo(endTime) < 0) {
                startCalendar.add(Calendar.DATE, 1)
                startTime = startCalendar.getTime()
            }
            //
            if (currentTime.before(startTime)) {
                println(" Time is Lesser ")
                valid = false
            } else {
                if (currentTime.after(endTime)) {
                    endCalendar.add(Calendar.DATE, 1)
                    endTime = endCalendar.getTime()
                }
                println("Comparing , Start Time /n $startTime")
                println("Comparing , End Time /n $endTime")
                println("Comparing , Current Time /n $currentTime")
                if (currentTime.before(endTime)) {
                    println("RESULT, Time lies b/w")
                    valid = true
                } else {
                    valid = false
                    println("RESULT, Time does not lies b/w")
                }
            }
            valid
        } else {
            throw IllegalArgumentException(
                "Not a valid time, expecting HH:MM:SS format"
            )
        }
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
            val intent = Intent(requireContext(), AlarmReceiver::class.java)
            intent.putExtra("CITYNAME", prayerName)
            intent.putExtra("EVENTID", "0")
            intent.putExtra("TEMP", "Satti")
            val alarmManager =
                requireActivity().getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
            val alarmId = TimeUnit.MILLISECONDS.toSeconds(timestamp).toInt()
            Log.d(TAG, "ALLsetAlarmID:$alarmId $prayerName $timestamp")
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

    private fun test(endTime: String): String {
        val cal = Calendar.getInstance()
        val nowHour = cal[Calendar.HOUR_OF_DAY]
        val nowMin = cal[Calendar.MINUTE]

        val m: Matcher = Pattern.compile("(\\d{2}):(\\d{2})").matcher(endTime)
        require(m.matches()) { "Invalid time format: $endTime" }
        val endHour: Int = m.group(1).toInt()
        val endMin: Int = m.group(2).toInt()
        require(!(endHour >= 24 || endMin >= 60)) { "Invalid time format: $endTime" }
        var minutesLeft = endHour * 60 + endMin - (nowHour * 60 + nowMin)
        if (minutesLeft < 0) minutesLeft += 24 * 60 // Time passed, so time until 'end' tomorrow
        val hours = minutesLeft / 60
        val minutes = minutesLeft - hours * 60
        //  println(hours.toString() + "h " + minutes + "m until " + endTime)
        return hours.toString() + "h " + minutes + "m"
    }

    fun itWillEnableGps() {
        val locationRequest = LocationRequest.create()
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        builder.setAlwaysShow(true) //this displays dialog box like Google Maps with two buttons - OK and NO,THANKS

        // task = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())
        val task = LocationServices.getSettingsClient(requireContext())
            .checkLocationSettings(builder.build())
        task.addOnCompleteListener { task ->
            try {
                val response = task.getResult(ApiException::class.java)
                // All location settings are satisfied. The client can initialize location
                // requests here.
                Log.d("sadasda", "onCreate: $response")
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->                             // Location settings are not satisfied. But could be fixed by showing the
                        // user a dialog.
                        try {
                            // Cast to a resolvable exception.
                            val resolvable = exception as ResolvableApiException
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().

                            resolvable.startResolutionForResult(
                                requireActivity(),
                                REQUEST_CHECK_SETTINGS
                            )
                        } catch (e: IntentSender.SendIntentException) {
                            // Ignore the error.
                        } catch (e: ClassCastException) {
                            // Ignore, should be an impossible error.
                        }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
                }
            }
        }

    }

    fun getCityName(latitude: Double, longitude: Double): String {
        try {
            val geo = Geocoder(requireContext(), Locale.getDefault())
            val addresses = geo.getFromLocation(latitude, longitude, 1)
            if (addresses.isEmpty()) {
                Toast.makeText(requireContext(), "emoty", Toast.LENGTH_SHORT).show()
            } else {
                if (addresses.size > 0) {
                    return addresses[0].featureName + ", " + addresses[0].locality + ", " + addresses[0].adminArea + ", " + addresses[0].countryName
                }
            }
        } catch (e: Exception) {
            e.printStackTrace() // getFromLocation() may sometimes fail
        }
        return ""
    }

    fun isGpsEnabled(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
}