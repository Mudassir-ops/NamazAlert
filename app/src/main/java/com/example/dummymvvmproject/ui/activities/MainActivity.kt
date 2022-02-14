package com.example.dummymvvmproject.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.dummymvvmproject.R
import com.example.dummymvvmproject.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import android.net.ParseException
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import android.content.Intent





@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController
    private val TAG = MainActivity::class.java.simpleName
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

       // isInBetween("05:23:00", "12:33:00", "05:22:00")
        isInBetween("19:43:00", "05:23:00", "05:22:00")


        // Log.d(TAG, "onCreate: ${isTimeBetweenTwoTime("05:23:24", "05:33:25", "05:28:22")}")

        binding.apply {
            val navView: BottomNavigationView = findViewById(R.id.nav_view)
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
            navController = navHostFragment.navController
            navView.setupWithNavController(navController)
            navController.addOnDestinationChangedListener { controller, destination, arguments ->
                Log.e("sadasdassa", "onDestinationChanged: " + destination.id);

            }

        }

    }

    private fun isInBetween(
        startTime: String,
        endTime: String,
        currentTime: String
    ): Boolean {
        val startMinutesSinceMidnight = calculateMinutesSinceMidnight(startTime)
        val endMinutesSinceMidnight = calculateMinutesSinceMidnight(endTime)
        val currentMinutesSinceMidnight = calculateMinutesSinceMidnight(currentTime)

        Log.d(TAG, "startMinutesSinceMidnight: $startMinutesSinceMidnight")
        Log.d(TAG, "endMinutesSinceMidnight: $endMinutesSinceMidnight")
        Log.d(TAG, "currentMinutesSinceMidnight: $currentMinutesSinceMidnight")

        if (startMinutesSinceMidnight < endMinutesSinceMidnight) {
            return (currentMinutesSinceMidnight >= startMinutesSinceMidnight) && (currentMinutesSinceMidnight < endMinutesSinceMidnight)
        } else {
            return !((currentMinutesSinceMidnight >= endMinutesSinceMidnight) && (currentMinutesSinceMidnight < startMinutesSinceMidnight))
        }
    }

    private fun calculateMinutesSinceMidnight(time_hh_mm: String): Int {
        val timeStrArray = time_hh_mm.split(":")
        var minutes = timeStrArray[1].toInt()
        minutes += 60 * timeStrArray[0].toInt()
        return minutes
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "onActivityResult: maincalled")
        for (fragment in supportFragmentManager.primaryNavigationFragment!!.childFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode,data)
        }
    }

}