package com.example.dummymvvmproject.data.local.room

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prayer_data_table")
class PrayerCacheEntity(
//    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo(name = "id")
//    var id: Int,

    @ColumnInfo(name = "Fajr") var Fajr: String = "",
    @ColumnInfo(name = "Sunrise") var Sunrise: String?,
    @ColumnInfo(name = "Dhuhr") var Dhuhr: String?,
    @ColumnInfo(name = "Asr") var Asr: String?,
    @ColumnInfo(name = "Sunset") var Sunset: String?,
    @ColumnInfo(name = "Maghrib") var Maghrib: String?,
    @ColumnInfo(name = "Isha") var Isha: String?,
    @ColumnInfo(name = "Imsak") var Imsak: String?,
    @ColumnInfo(name = "Midnight") var Midnight: String?,
    @ColumnInfo(name = "grogreanWeekday") var grogreanWeekday: String?,
    @ColumnInfo(name = "grogreanDate") var grogreanDate: String = "",
    @ColumnInfo(name = "grogreanMonth") var grogreanMonth: String?,
    @ColumnInfo(name = "hijriWeekday") var hijriWeekday: String?,
    @ColumnInfo(name = "hijriDate") var hijriDate: String?,
    @ColumnInfo(name = "hijriMonth") var hijriMonth: String?,
    @ColumnInfo(name = "timezone") var timezone: String?,
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "timestamp") var timestamp: String = "",
    @ColumnInfo(name = "day") var day: Int?,
    @ColumnInfo(name = "latitude") var latitude: Double?,
    @ColumnInfo(name = "longitude") var longitude: Double?,
    @ColumnInfo(name = "isAlarmSetForFajar") var isAlarmSetForFajar: Boolean?,
    @ColumnInfo(name = "isAlarmSetForDhur") var isAlarmSetForDhur: Boolean?,
    @ColumnInfo(name = "isAlarmSetForAsr") var isAlarmSetForAsr: Boolean?,
    @ColumnInfo(name = "isAlarmSetForMaghrib") var isAlarmSetForMaghrib: Boolean?,
    @ColumnInfo(name = "isAlarmSetForIsha") var isAlarmSetForIsha: Boolean?,

    )



