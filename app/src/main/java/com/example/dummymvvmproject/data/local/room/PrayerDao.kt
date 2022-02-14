package com.example.dummymvvmproject.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.sql.Timestamp

@Dao
interface PrayerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(prayerCacheEntity: PrayerCacheEntity): Long

    @Query("SELECT * FROM prayer_data_table")
    suspend fun get(): List<PrayerCacheEntity>

    @Query("SELECT * FROM prayer_data_table where grogreanDate=:currentDate")
    suspend fun getCurrentDayPrayersData(currentDate: String): PrayerCacheEntity


    @Query("UPDATE prayer_data_table SET isAlarmSetForFajar = :isAlarmSetForFajar,isAlarmSetForDhur = :isAlarmSetForDhur,isAlarmSetForAsr = :isAlarmSetForAsr,isAlarmSetForMaghrib = :isAlarmSetForMaghrib,isAlarmSetForIsha = :isAlarmSetForIsha WHERE timestamp =:timestamp")
    fun updateIsAlarm(
        isAlarmSetForFajar: Boolean,
        isAlarmSetForDhur: Boolean,
        isAlarmSetForAsr: Boolean,
        isAlarmSetForMaghrib: Boolean,
        isAlarmSetForIsha: Boolean,
        timestamp: String
    ):Int
}