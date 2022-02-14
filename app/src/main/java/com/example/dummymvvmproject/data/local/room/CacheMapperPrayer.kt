package com.example.dummymvvmproject.data.local.room

import com.example.dummymvvmproject.model.PrayerData
import com.example.dummymvvmproject.util.EntityMapper
import javax.inject.Inject

class CacheMapperPrayer
@Inject
constructor() : EntityMapper<PrayerCacheEntity, PrayerData> {

    override fun mapFromEntity(entity: PrayerCacheEntity): PrayerData {
        return PrayerData(
            Fajr = entity.Fajr,
            Sunrise = entity.Sunrise,
            Dhuhr = entity.Dhuhr,
            Asr = entity.Asr,
            Sunset = entity.Sunset,
            Maghrib = entity.Maghrib,
            Isha = entity.Isha,
            Imsak = entity.Imsak,
            Midnight = entity.Midnight,
            grogreanWeekday = entity.grogreanWeekday,
            grogreanDate = entity.grogreanDate,
            grogreanMonth = entity.grogreanMonth,
            hijriWeekday = entity.hijriWeekday,
            hijriDate = entity.hijriDate,
            hijriMonth = entity.hijriMonth,
            timezone = entity.timezone,
            timestamp = entity.timestamp,
            day = entity.day!!,
            latitude=entity.latitude!!,
            longitude=entity.longitude,
            isAlarmSetForFajar = entity.isAlarmSetForFajar,
            isAlarmSetForDhur = entity.isAlarmSetForDhur,
            isAlarmSetForAsr = entity.isAlarmSetForAsr,
            isAlarmSetForMaghrib = entity.isAlarmSetForMaghrib,
            isAlarmSetForIsha = entity.isAlarmSetForIsha,

            )
    }

    //map to cacheentity
    override fun mapToEntity(domainModel: PrayerData): PrayerCacheEntity {
        return PrayerCacheEntity(
            Fajr = domainModel.Fajr!!,
            Sunrise = domainModel.Sunrise,
            Dhuhr = domainModel.Dhuhr,
            Asr = domainModel.Asr,
            Sunset = domainModel.Sunset,
            Maghrib = domainModel.Maghrib,
            Isha = domainModel.Isha,
            Imsak = domainModel.Imsak,
            Midnight = domainModel.Midnight,
            grogreanWeekday = domainModel.grogreanWeekday,
            grogreanDate = domainModel.grogreanDate!!,
            grogreanMonth = domainModel.grogreanMonth,
            hijriWeekday = domainModel.hijriWeekday,
            hijriDate = domainModel.hijriDate,
            hijriMonth = domainModel.hijriMonth,
            timezone = domainModel.timezone,
            timestamp = domainModel.timestamp!!,
            day = domainModel.day!!,
            latitude=domainModel.latitude!!,
            longitude=domainModel.longitude,
            isAlarmSetForFajar = domainModel.isAlarmSetForFajar,
            isAlarmSetForDhur = domainModel.isAlarmSetForDhur,
            isAlarmSetForAsr = domainModel.isAlarmSetForAsr,
            isAlarmSetForMaghrib = domainModel.isAlarmSetForMaghrib,
            isAlarmSetForIsha = domainModel.isAlarmSetForIsha,
            )
    }

    fun mapFromEntityList(entities: List<PrayerCacheEntity>): List<PrayerData> {
        return entities.map { mapFromEntity(it) }
    }
}











