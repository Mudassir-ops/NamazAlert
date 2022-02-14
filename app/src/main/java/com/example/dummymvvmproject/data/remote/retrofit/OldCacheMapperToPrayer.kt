package com.example.dummymvvmproject.data.remote.retrofit//package com.example.dummymvvmproject.data
//
//import com.example.dummymvvmproject.model.PrayerData
//import com.example.dummymvvmproject.data.EntityMapperReplaceOldWithNew
//import com.example.dummymvvmproject.data.local.room.PrayerCacheEntity
//import javax.inject.Inject
//
//class OldCacheMapperToPrayer
//@Inject
//constructor() : EntityMapperReplaceOldWithNew<PrayerCacheEntity, PrayerData, PrayerData> {
//    override fun mapFromEntity(
//        oldCacheEntity: PrayerCacheEntity,
//        newModelEntity: PrayerData
//    ): PrayerData {
//
//        return PrayerData(
//            Fajr = newModelEntity.Fajr,
//            Sunrise = newModelEntity.Sunrise,
//            Dhuhr = newModelEntity.Dhuhr,
//            Asr = newModelEntity.Asr,
//            Sunset = newModelEntity.Sunset,
//            Maghrib = newModelEntity.Maghrib,
//            Isha = newModelEntity.Isha,
//            Imsak = newModelEntity.Imsak,
//            Midnight = newModelEntity.Midnight,
//            grogreanWeekday = newModelEntity.grogreanWeekday,
//            grogreanDate = newModelEntity.grogreanDate,
//            grogreanMonth = newModelEntity.grogreanMonth,
//            hijriWeekday = newModelEntity.hijriWeekday,
//            hijriDate = newModelEntity.hijriDate,
//            hijriMonth = newModelEntity.hijriMonth,
//            timezone = newModelEntity.timezone,
//            timestamp = newModelEntity.timestamp,
//            isAlarmSetForFajar = oldCacheEntity.isAlarmSetForFajar,
//            isAlarmSetForDhur = oldCacheEntity.isAlarmSetForDhur,
//            isAlarmSetForAsr = oldCacheEntity.isAlarmSetForAsr,
//            isAlarmSetForMaghrib = oldCacheEntity.isAlarmSetForMaghrib,
//            isAlarmSetForIsha = oldCacheEntity.isAlarmSetForIsha
//        )
//    }
//
//    fun mapFromEntityList(
//        oldCacheEntity: List<PrayerCacheEntity>,
//        newModelEntity: List<PrayerData>
//    ): List<PrayerData> {
//        val list = ArrayList<PrayerData>()
//        list.clear()
//        for(i in oldCacheEntity.indices){
//                list.add(mapFromEntity(oldCacheEntity[i], newModelEntity[i]))
//        }
//        return list
//    }
//
//}
//
//
//
//
//
//
//
//
//
//
//
