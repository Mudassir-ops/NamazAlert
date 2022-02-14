package com.example.dummymvvmproject.ui.fragments

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingwithmitch.daggerhiltplayground.util.DataState
import com.example.dummymvvmproject.model.PrayerData
import com.example.dummymvvmproject.repository.MainRepository
import com.example.dummymvvmproject.ui.activities.MainStateEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel
@Inject
constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _dataState: MutableLiveData<DataState<PrayerData>> = MutableLiveData()
    val dataState: LiveData<DataState<PrayerData>>
        get() = _dataState

    private val _dataState1: MutableLiveData<DataState<List<PrayerData>>> = MutableLiveData()
    val dataState1: LiveData<DataState<List<PrayerData>>>
        get() = _dataState1

    private val _dataState2: MutableLiveData<DataState<Boolean>> = MutableLiveData()
    val dataState2: LiveData<DataState<Boolean>>
        get() = _dataState2


    fun getCurrentDayPrayersData(curentDate: String) {
        viewModelScope.launch {
            mainRepository.getCurrentDayPrayersData(
                curentDate
            ).onEach { dataState ->
                _dataState.value = dataState
            }
                .launchIn(viewModelScope)
        }

    }

    fun getPrayersDataForAlarm() {
        viewModelScope.launch {
            mainRepository.getPrayersData(
            ).onEach { dataState ->
                _dataState1.value = dataState
            }
                .launchIn(viewModelScope)
        }

    }

    fun updateIsAlarm(
        isAlarmSetForFajar: Boolean,
        isAlarmSetForDhur: Boolean,
        isAlarmSetForAsr: Boolean,
        isAlarmSetForMaghrib: Boolean,
        isAlarmSetForIsha: Boolean,
        timestamp: String
    ) {
         val TAG = AlarmViewModel::class.java.simpleName
        viewModelScope.launch {
            mainRepository.updateIsAlarm(
                isAlarmSetForFajar,
                isAlarmSetForDhur,
                isAlarmSetForAsr,
                isAlarmSetForMaghrib,
                isAlarmSetForIsha,
                timestamp
            ).onEach {
                Log.d(TAG, "updateIsAlarm: $it")
            }
                .launchIn(viewModelScope)
        }

    }


}
