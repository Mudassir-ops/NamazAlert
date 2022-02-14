package com.example.dummymvvmproject.ui.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingwithmitch.daggerhiltplayground.util.DataState
import com.example.dummymvvmproject.model.PrayerData
import com.example.dummymvvmproject.repository.MainRepository
import com.example.dummymvvmproject.ui.activities.MainStateEvent
import com.example.dummymvvmproject.ui.activities.MainViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val mainRepository: MainRepository
    ) : ViewModel() {


    private val _dataState: MutableLiveData<DataState<List<PrayerData>>> = MutableLiveData()
    val dataState: LiveData<DataState<List<PrayerData>>>
        get() = _dataState

    fun setStateEvent(mainStateEvent: MainStateEvent, lat: String, lng: String) {
        viewModelScope.launch {
            when (mainStateEvent) {
                is MainStateEvent.GetBlogsEvent -> {
                    mainRepository.getBlogsTest(
                        lat, lng
                    )
                        .onEach { dataState ->
                            _dataState.value = dataState
                        }
                        .launchIn(viewModelScope)
                }

                MainStateEvent.None -> {
                    //----who cares

                }
                else -> {}
            }
        }
    }

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }
    }



