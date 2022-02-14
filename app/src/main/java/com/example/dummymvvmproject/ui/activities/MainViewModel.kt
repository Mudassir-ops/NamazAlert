package com.example.dummymvvmproject.ui.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingwithmitch.daggerhiltplayground.util.DataState
import com.example.dummymvvmproject.model.PrayerData
import com.example.dummymvvmproject.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi

@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val mainRepository: MainRepository,

    ) : ViewModel() {


    companion object {
       private val TAG = MainViewModel::class.java.simpleName
    }
}


sealed class MainStateEvent {

    object GetBlogsEvent : MainStateEvent()

    object None : MainStateEvent()

    object NOTGET : MainStateEvent()
}


















