package com.example.dummymvvmproject.model

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class SavedStateViewModel(private val state: SavedStateHandle) : ViewModel() {

    val liveDate = state.getLiveData("liveData", Random.nextInt().toString())

    fun saveState() {
        state.set("liveData", liveDate.value)
    }
}