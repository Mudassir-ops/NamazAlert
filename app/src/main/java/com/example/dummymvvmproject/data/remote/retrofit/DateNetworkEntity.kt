package com.example.dummymvvmproject.data.remote.retrofit

import com.google.gson.annotations.SerializedName




data class DateNetworkEntity(

    @SerializedName("readable") val readable: String = "",
    @SerializedName("timestamp") val timestamp: String = "",
    @SerializedName("gregorian") val gregorianNetworkEntity: com.example.dummymvvmproject.data.remote.retrofit.GregorianNetworkEntity? = null,
    @SerializedName("hijri") val hijriNetworkEntity: com.example.dummymvvmproject.data.remote.retrofit.HijriNetworkEntity? = null
)