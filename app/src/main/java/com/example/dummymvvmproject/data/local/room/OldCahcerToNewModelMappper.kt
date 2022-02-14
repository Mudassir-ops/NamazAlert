package com.example.dummymvvmproject.data.local.room

import com.example.dummymvvmproject.model.PrayerData
import com.example.dummymvvmproject.util.EntityMapper
import com.example.dummymvvmproject.util.EntityMapperOldToNewReplace
import javax.inject.Inject

class OldCahcerToNewModelMappper
@Inject
constructor() : EntityMapperOldToNewReplace<PrayerCacheEntity, PrayerData, PrayerData> {
    override fun mapFromEntity(entity: PrayerCacheEntity, newPrayerModel: PrayerData): PrayerData {

        return PrayerData(
            Fajr = newPrayerModel.Fajr,
            Sunrise = newPrayerModel.Sunrise,
            Dhuhr = newPrayerModel.Dhuhr,
            Asr = newPrayerModel.Asr,
            Sunset = newPrayerModel.Sunset,
            Maghrib = newPrayerModel.Maghrib,
            Isha = newPrayerModel.Isha,
            Imsak = newPrayerModel.Imsak,
            Midnight = newPrayerModel.Midnight,
            grogreanWeekday = newPrayerModel.grogreanWeekday,
            grogreanDate = newPrayerModel.grogreanDate,
            grogreanMonth = newPrayerModel.grogreanMonth,
            hijriWeekday = newPrayerModel.hijriWeekday,
            hijriDate = newPrayerModel.hijriDate,
            hijriMonth = newPrayerModel.hijriMonth,
            timezone = newPrayerModel.timezone,
            timestamp = newPrayerModel.timestamp,
            day = newPrayerModel.day,
            latitude = newPrayerModel.latitude!!,
            longitude = newPrayerModel.longitude,
            isAlarmSetForFajar = entity.isAlarmSetForFajar,
            isAlarmSetForDhur = entity.isAlarmSetForDhur,
            isAlarmSetForAsr = entity.isAlarmSetForAsr,
            isAlarmSetForMaghrib = entity.isAlarmSetForMaghrib,
            isAlarmSetForIsha = entity.isAlarmSetForIsha

        )

    }

    fun getOldCahcerEntitytoNewModel(
        oldcacher: List<PrayerCacheEntity>,
        newPrayerModel: List<PrayerData>
    ): List<PrayerData> {
        val arraList = ArrayList<PrayerData>()
        arraList.clear()
        for (i in 0..oldcacher.size - 1) {
            arraList.add(mapFromEntity(oldcacher[i], newPrayerModel[i]))

        }
        return arraList
    }


}











