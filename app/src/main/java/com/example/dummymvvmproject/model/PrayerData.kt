package com.example.dummymvvmproject.model

import com.google.gson.annotations.SerializedName


data class PrayerData(

    @SerializedName("Fajr") var Fajr: String?,
    @SerializedName("Sunrise") var Sunrise: String?,
    @SerializedName("Dhuhr") var Dhuhr: String?,
    @SerializedName("Asr") var Asr: String?,
    @SerializedName("Sunset") var Sunset: String?,
    @SerializedName("Maghrib") var Maghrib: String?,
    @SerializedName("Isha") var Isha: String?,
    @SerializedName("Imsak") var Imsak: String?,
    @SerializedName("Midnight") var Midnight: String?,
    @SerializedName("grogreanWeekday") var grogreanWeekday: String?,
    @SerializedName("grogreanDate") var grogreanDate: String?,
    @SerializedName("grogreanMonth") var grogreanMonth: String?,
    @SerializedName("hijriWeekday") var hijriWeekday: String?,
    @SerializedName("hijriDate") var hijriDate: String?,
    @SerializedName("hijriMonth") var hijriMonth: String?,
    @SerializedName("timezone") var timezone: String?,
    @SerializedName("timestamp") var timestamp: String?,
    @SerializedName("day") var day: Int?,
    @SerializedName("latitude") var latitude: Double?,
    @SerializedName("longitude") var longitude: Double?,
    var isAlarmSetForFajar: Boolean? = true,
    var isAlarmSetForDhur: Boolean? = true,
    var isAlarmSetForAsr: Boolean? = true,
    var isAlarmSetForMaghrib: Boolean? = true,
    var isAlarmSetForIsha: Boolean? = true,

    )