package com.example.dummymvvmproject.data.remote.retrofit

import retrofit2.http.GET
import retrofit2.http.Query

interface PrayerRetrofit {

    @GET("calendar")
    suspend fun get1(
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
        @Query("month") month: String,
        @Query("year") year: String,
        @Query("school") school: Int,
    ): com.example.dummymvvmproject.data.remote.retrofit.Json4Kotlin_Base
}